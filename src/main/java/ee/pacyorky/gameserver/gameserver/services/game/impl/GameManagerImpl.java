package ee.pacyorky.gameserver.gameserver.services.game.impl;

import ee.pacyorky.gameserver.gameserver.config.AppProperties;
import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.entities.game.*;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;
import ee.pacyorky.gameserver.gameserver.repositories.EventDayRepository;
import ee.pacyorky.gameserver.gameserver.repositories.dao.GameDao;
import ee.pacyorky.gameserver.gameserver.services.game.DeckService;
import ee.pacyorky.gameserver.gameserver.services.game.GameManager;
import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameManagerImpl implements GameManager {

    private final GameDao gameDao;

    private final DeckService deckService;

    private final EventDayRepository eventDayRepository;

    private final PlayerService playerService;

    private final GeneralGameService generalGameService;

    private final AppProperties properties;



    public static void initPlayersCards(Player player, Game game) {
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

    @Override
    public Game createGame(String playerId, GameCreationDto gameCreationDto) {

        checkGameCreation(gameCreationDto);
        Game game = Game.builder()
                .calendar(eventDayRepository.findAll())
                .dishesDeck(deckService.getDishesDeck())
                .holidaysDeck(deckService.getHolidaysDeck())
                .ritualsDeck(deckService.getRitualsDeck())
                .stuffDeck(deckService.getStuffDeck())
                .startAt(LocalDateTime.now().plusSeconds(Optional.ofNullable(gameCreationDto.getSecondsBeforeStart()).orElse(properties.getSecondsBeforeStart())))
                .capacity(gameCreationDto.getCapacity())
                .password(gameCreationDto.getPassword())
                .privateRoom(gameCreationDto.isPrivateRoom())
                .withComputer(gameCreationDto.isWithComputer())
                .name(gameCreationDto.getName())
                .characters(deckService.getCharacterDeck())
                .status(Status.WAITING.getId())
                .secondsBeforeStart(Optional.ofNullable(gameCreationDto.getSecondsBeforeStart()).orElse(properties.getSecondsBeforeStart()))
                .secondsForStep(Optional.ofNullable(gameCreationDto.getSecondsForStep()).orElse(properties.getSecondsBeforeStep()))
                .build();
        Player player = playerService.getOrCreatePlayer(playerId);
        checkPlayerInGame(player);
        game.addPlayer(player);
        var savedGame = gameDao.saveGame(game);
        generalGameService.startGame(savedGame.getId());
        return savedGame;
    }

    private void checkGameCreation(GameCreationDto gameCreationDto) {
        if (gameCreationDto == null) {
            throw new GlobalException("Creation dto is null", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (gameCreationDto.getCapacity() == null || gameCreationDto.getCapacity() > 8 || gameCreationDto.getCapacity() < 2) {
            throw new GlobalException("Game capacity incorrect " + gameCreationDto.getCapacity(), GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Game makeStep(String playerId, List<Long> cards) {
        var game = gameDao.getGame(playerId);
        var player = playerService.getOrCreatePlayer(playerId);
        checkGameAndPlayer(game, player);
        if (!player.getDeck().stream().map(Card::getId).collect(Collectors.toList()).containsAll(cards)) {
            throw new GlobalException("Player dont have cards from collection", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        var stepCards = player.getDeck().stream()
                .filter(card -> cards.contains(card.getId()))
                .map(card -> StepCard.builder().card(card).build()).collect(Collectors.toList());
        game.getStep().setStepCards(stepCards);
        gameDao.saveGame(game);
        generalGameService.doStepPart(game.getId(), StepStatus.WAITING_CARD);
        return gameDao.getGame(playerId);
    }

    @Override
    public Game voteCards(String playerId, Set<Long> cards) {
        var game = gameDao.getGame(playerId);
        var player = playerService.getOrCreatePlayer(playerId);
        if (game == null) {
            throw new GlobalException("Game not found for player", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (game.getStep() == null) {
            throw new GlobalException("Step is null", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (game.getStep().getStatus() != StepStatus.WAITING_VOTE) {
            throw new GlobalException("Step is not waiting vote", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (player.isVoted()) {
            throw new GlobalException("Player already voted", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (player.getId().equals(game.getStep().getCurrentPlayer().getId())) {
            throw new GlobalException("Player can not vote for himself", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        int votedCards = 0;

        for (StepCard stepCard : game.getStep().getStepCards()) {
            if (cards.contains(stepCard.getCard().getId())) {
                stepCard.addVote();
                votedCards++;
            }
        }
        if (votedCards != cards.size()) {
            throw new GlobalException("Voted cards is wrong", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        player.setVoted(true);
        playerService.savePlayer(player);
        gameDao.saveGame(game);
        return gameDao.getGame(playerId);
    }

    private void checkGameAndPlayer(Game game, Player player) {
        if (game == null) {
            throw new GlobalException("Game is null", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (player == null) {
            throw new GlobalException("Player is null", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (game.getStep() == null) {
            throw new GlobalException("Step is not created. Null.", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (!player.equals(game.getStep().getCurrentPlayer())) {
            throw new GlobalException("Current player != player", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Game joinIntoTheGame(String playerId, Long gameId) {
        Game game = gameDao.getGame(gameId);
        if (game.isNotWaiting()) {
            throw new GlobalException("Game not waiting.", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (game.getPlayers().size() >= game.getCapacity()) {
            throw new GlobalException("Players count more than capacity", GlobalExceptionCode.CAPACITY_LIMIT_REACHED);
        }
        Player player = playerService.getOrCreatePlayer(playerId);
        checkPlayerInGame(player);
        player.resetPlayer();
        playerService.savePlayer(player);
        game.addPlayer(player);
        return gameDao.saveGame(game);
    }

    @Override
    public Game leftFromTheGame(String playerId) {
        var game = gameDao.getGame(playerId);
        if (game == null) {
            throw new GlobalException("Game not found", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        game.removePlayer(playerId);
        return gameDao.saveGame(game);
    }

    @PostConstruct
    public void clearGames() {
        clearGames(null);
    }

    @Override
    public void clearGames(Long id) {
        if (id == null || id.equals(0L)) {
            gameDao.getActiveGames().forEach(game -> {
                game.finish(Status.CANCELLED);
                gameDao.saveGame(game);
            });
        } else {
            var game = gameDao.getGame(id);
            game.finish(Status.CANCELLED);
            gameDao.saveGame(game);
        }
    }

    private void checkPlayerInGame(Player player) {
        Set<Player> playersInGame = gameDao.getGames().stream().flatMap(repoGame -> repoGame.getPlayers().stream()).collect(Collectors.toSet());
        if (playersInGame.contains(player)) {
            throw new GlobalException("Player already in game", GlobalExceptionCode.PLAYER_ALREADY_IN_GAME);
        }
    }

    @Override
    public Game throwDice(String playerId) {
        var game = gameDao.getGame(playerId);
        var player = playerService.getOrCreatePlayer(playerId);
        checkGameAndPlayer(game, player);
        generalGameService.doStepPart(game.getId(), StepStatus.WAITING_DICE);
        return gameDao.getGame(playerId);
    }
}
