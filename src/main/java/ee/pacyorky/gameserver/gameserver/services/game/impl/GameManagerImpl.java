package ee.pacyorky.gameserver.gameserver.services.game.impl;

import ee.pacyorky.gameserver.gameserver.agoraio.RtcTokenGenerator;
import ee.pacyorky.gameserver.gameserver.config.AgoraProperties;
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

    private final AgoraProperties agoraProperties;

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
        player.resetPlayer();
        playerService.savePlayer(player);
        game.addPlayer(player);
        var savedGame = gameDao.saveGame(game);
        if (agoraProperties.isCreateTokenOnCreateGame()) {
            if (!game.isWithComputer() || agoraProperties.isVoiceChatInComputerGame()) {
                savedGame.setToken(RtcTokenGenerator.buildTokenWithUserAccount(agoraProperties, game.getId()));
                gameDao.saveGame(savedGame);
            }
        }
        generalGameService.startGame(savedGame.getId());
        return savedGame;
    }

    private void checkGameCreation(GameCreationDto gameCreationDto) {
        if (gameDao.getActiveGamesCount() >= properties.getMaxGames()) {
            throw new GlobalException("Games more than " + properties.getMaxGames(), GlobalExceptionCode.GAMES_LIMIT_REACHED);
        }
        if (gameCreationDto == null) {
            throw new GlobalException("Creation dto is null", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (gameCreationDto.getCapacity() == null || gameCreationDto.getCapacity() > 8 || gameCreationDto.getCapacity() < 2) {
            throw new GlobalException("Game capacity incorrect " + gameCreationDto.getCapacity(), GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (gameCreationDto.getSecondsBeforeStart() != null && gameCreationDto.getSecondsBeforeStart() < 1L) {
            throw new GlobalException("Game before start incorrect " + gameCreationDto.getSecondsBeforeStart(), GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (gameCreationDto.getSecondsForStep() != null && gameCreationDto.getSecondsForStep() < 1L) {
            throw new GlobalException("Game before step incorrect " + gameCreationDto.getSecondsForStep(), GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Game makeStep(String playerId, List<Long> cards) {
        var game = getGame(playerId);
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
        return getGame(playerId);
    }

    @Override
    public Game voteCards(String playerId, Set<Long> cards) {
        var game = getGame(playerId);
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

        if (!game.getStep().getStepCards().stream().map(stepCard -> stepCard.getCard().getId()).collect(Collectors.toList()).containsAll(cards)) {
            throw new GlobalException("Voted cards is wrong. Voted cards: " + cards + " step cards: " + game.getStep().getStepCards(), GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }

        for (StepCard stepCard : game.getStep().getStepCards()) {
            if (cards.contains(stepCard.getCard().getId())) {
                stepCard.addVote();
            }
        }
        player.setVoted(true);
        playerService.savePlayer(player);
        gameDao.saveGame(game);
        return getGame(playerId);
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
        if (!player.getId().equals(game.getStep().getCurrentPlayer().getId())) {
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
        var game = getGame(playerId);
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
        var game = gameDao.getGame(player.getId());
        if (game.isPresent()) {
            throw new GlobalException("Player already in game. GameId: " + game.get().getId() + " game status: " + game.get().getStatus().name(), GlobalExceptionCode.PLAYER_ALREADY_IN_GAME);
        }
    }

    @Override
    public Game throwDice(String playerId) {
        var game = getGame(playerId);
        var player = playerService.getOrCreatePlayer(playerId);
        checkGameAndPlayer(game, player);
        generalGameService.doStepPart(game.getId(), StepStatus.WAITING_DICE);
        return getGame(playerId);
    }

    @Override
    public Game forceStart(String playerId) {
        var game = getGame(playerId);
        if (game.isNotWaiting()) {
            throw new GlobalException("Game is not waiting", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (!game.isWithComputer() && game.getPlayers().size() < 2) {
            throw new GlobalException("Players less then 2", GlobalExceptionCode.PLAYERS_COUNT_LESS);
        }
        generalGameService.forceStart(game.getId());
        return getGame(playerId);
    }

    @Override
    public Game choosePrize(String playerId, CardType cardType) {
        var game = getGame(playerId);
        var player = playerService.getOrCreatePlayer(playerId);
        checkGameAndPlayer(game, player);
        //TODO checks
        player.getDeck().add(game.getRandomCard(cardType));
        game.getStep().setPrizeReceived(true);
        playerService.savePlayer(player);
        return gameDao.saveGame(game);
    }

    private Game getGame(String playerId) {
        return gameDao.getGame(playerId).orElseThrow(() -> new GlobalException("Player not in game", GlobalExceptionCode.INTERNAL_SERVER_ERROR));
    }
}
