package ee.pacyorky.gameserver.gameserver.services.impl;

import ee.pacyorky.gameserver.gameserver.entities.*;
import ee.pacyorky.gameserver.gameserver.entities.Character;
import ee.pacyorky.gameserver.gameserver.services.DeckService;
import ee.pacyorky.gameserver.gameserver.services.GameService;
import ee.pacyorky.gameserver.gameserver.services.GeneralGameService;
import ee.pacyorky.gameserver.gameserver.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GeneralGameServiceImpl implements GeneralGameService {

    private final GameService gameService;
    private final PlayerService playerService;

    @Override
    public synchronized Player startGame(Long gameId, String playerId) {
        Player player = playerService.getOrCreatePlayer(playerId);
        Game game = gameService.getGame(gameId);
        if (game == null || player == null) throw new RuntimeException();
        if (!game.getPlayers().contains(player)) return null;

        if (game.isStarted()) {
            return player;
        }
        if (game.getStartAt().isAfter(LocalDateTime.now())) throw new RuntimeException();
        game.setStarted(true);
        Character character = game.getCharacters().stream().findAny().orElseThrow();
        player.setCharacter(character);
        game.getCharacters().remove(character);
        initPlayersCards(player, game);
        gameService.saveGame(game);
        return playerService.savePlayer(player);
    }

    private void initPlayersCards(Player player, Game game) {
        Map<CardType, List<Card>> cardTypeListMap = game.getAllDecks();

        for (Map.Entry<CardType, List<Card>> cardTypeListEntry : cardTypeListMap.entrySet()) {
            if (cardTypeListEntry.getKey() == CardType.HOLIDAY) continue;
            List<Card> playersCards = player.getCardsByType(cardTypeListEntry.getKey());
            for (int i = playersCards.size(); i < 2; i++) {
                Card card = cardTypeListEntry.getValue().stream().findFirst().orElseThrow();
                player.getDeck().add(card);
                cardTypeListEntry.getValue().remove(card);
            }
        }

    }

}
