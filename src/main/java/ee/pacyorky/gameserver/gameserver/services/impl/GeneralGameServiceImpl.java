package ee.pacyorky.gameserver.gameserver.services.impl;

import ee.pacyorky.gameserver.gameserver.entities.Character;
import ee.pacyorky.gameserver.gameserver.entities.*;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;
import ee.pacyorky.gameserver.gameserver.services.EventDayService;
import ee.pacyorky.gameserver.gameserver.services.GameService;
import ee.pacyorky.gameserver.gameserver.services.GeneralGameService;
import ee.pacyorky.gameserver.gameserver.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class GeneralGameServiceImpl implements GeneralGameService {

    private final GameService gameService;
    private final PlayerService playerService;
    private final EventDayService eventDayService;

    @Override
    public synchronized Player startGame(Long gameId, String playerId) {
        Player player = playerService.getOrCreatePlayer(playerId);
        Game game = gameService.getGame(gameId);
        if (game == null || player == null)
            throw new GlobalException("Internal server error", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        if (!game.getPlayers().contains(player)) return null;

        if (game.isStarted()) {
            return player;
        }
        if (game.getStartAt().isAfter(LocalDateTime.now()))
            throw new GlobalException("Game already started", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        game.setStarted(true);
        Set<Player> players = game.getPlayers();
        for (Player player1 : players) {
            initPlayersCards(player1, game);
            Character character = game.getCharacters().stream().findAny().orElseThrow();
            player1.setCharacter(character);
            game.getCharacters().remove(character);
            player1.setCurrentDay(eventDayService.getStartPosition());
            playerService.savePlayer(player1);
        }
        nextStep(gameId, playerId);
        gameService.saveGame(game);
        return playerService.getOrCreatePlayer(playerId);
    }

    @Override
    public synchronized Game nextStep(Long gameId, String playerId) {
        Player player = playerService.getOrCreatePlayer(playerId);
        Game game = gameService.getGame(gameId);
        if (player == null || game == null) {
            throw new GlobalException("Internal server error", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (!game.isStarted()) {
            throw new GlobalException("Game not started", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (!game.getPlayers().contains(player)) return null;

        if (game.getNextStepAt() != null && game.getNextStepAt().isAfter(LocalDateTime.now())) {
            return game;
        }

        game.setNextStepAt(LocalDateTime.now().plusSeconds(20));

        List<Player> players = new ArrayList<>(game.getPlayers());
        players.sort(Comparator.comparing(Player::getId));
        game.setCounter(new Random().nextInt(6) + 1);

        if (game.getCurrentPlayer() == null) {
            game.setCurrentPlayer(players.get(0));
        } else {
            initPlayersCards(game.getCurrentPlayer(), game);
            playerService.savePlayer(game.getCurrentPlayer());

            //TODO така фигня. Надо переписать это все нахрен
            for (int i = 0; i < players.size(); i++) {
                Player player1 = players.get(i);
                if (player1.equals(game.getCurrentPlayer())) {
                    int order = i > players.size() ? 0 : i;
                    game.setCurrentPlayer(players.get(order));
                    break;
                }
            }
        }

        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.setCurrentDay(eventDayService.getNextDay(player, game.getCounter()));
        playerService.savePlayer(currentPlayer);

        return gameService.saveGame(game);
    }

    private void initPlayersCards(Player player, Game game) {
        Map<CardType, List<Card>> cardTypeListMap = game.getAllDecks();

        for (Map.Entry<CardType, List<Card>> cardTypeListEntry : cardTypeListMap.entrySet()) {
            List<Card> playersCards = player.getCardsByType(cardTypeListEntry.getKey());
            for (int i = playersCards.size(); i < 2; i++) {
                Card card = cardTypeListEntry.getValue().stream().findFirst().orElseThrow();
                player.getDeck().add(card);
                cardTypeListEntry.getValue().remove(card);
            }
        }

    }

}
