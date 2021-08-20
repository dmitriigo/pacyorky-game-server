package ee.pacyorky.gameserver.gameserver.services.impl;

import ee.pacyorky.gameserver.gameserver.config.AppProperties;
import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.entities.Card;
import ee.pacyorky.gameserver.gameserver.entities.Game;
import ee.pacyorky.gameserver.gameserver.entities.Player;
import ee.pacyorky.gameserver.gameserver.entities.Status;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;
import ee.pacyorky.gameserver.gameserver.repositories.EventDayRepository;
import ee.pacyorky.gameserver.gameserver.repositories.GameRepository;
import ee.pacyorky.gameserver.gameserver.services.DeckService;
import ee.pacyorky.gameserver.gameserver.services.GameManager;
import ee.pacyorky.gameserver.gameserver.services.GeneralGameService;
import ee.pacyorky.gameserver.gameserver.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameManagerImpl implements GameManager {

    private final GameRepository gameRepository;

    private final DeckService deckService;

    private final EventDayRepository eventDayRepository;

    private final PlayerService playerService;

    private final GeneralGameService generalGameService;

    private final AppProperties properties;

    @Override
    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    @Override
    public Game getGame(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow();
    }

    @Override
    public Game getGame(String playerId) {
        return getGames().stream().filter(game -> game.getPlayers().stream().map(Player::getId).anyMatch(id -> id.equals(playerId))).findFirst().orElse(null);
    }

    @Override
    public Game createGame(String playerId, GameCreationDto gameCreationDto) {

        if (gameRepository.count() >= properties.getMaxGames()) {
            throw new GlobalException("Games count more than capacity", GlobalExceptionCode.GAMES_LIMIT_REACHED);
        }

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
                .status(Status.WAITING)
                .secondsBeforeStart(Optional.ofNullable(gameCreationDto.getSecondsBeforeStart()).orElse(properties.getSecondsBeforeStart()))
                .secondsForStep(Optional.ofNullable(gameCreationDto.getSecondsForStep()).orElse(properties.getSecondsBeforeStep()))
                .build();
        Player player = playerService.getOrCreatePlayer(playerId);
        checkPlayerInGame(player);
        game.addPlayer(player);
        var savedGame = gameRepository.saveAndFlush(game);
        generalGameService.setUpStart(savedGame, this);
        return savedGame;
    }

    @Override
    public Game makeStep(String playerId, List<Long> cards) {
        var game = getGame(playerId);
        var player = playerService.getOrCreatePlayer(playerId);
        if (game.getStep().getStatus() != Status.WAITING) {
            throw new GlobalException("Step not waiting", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (!player.equals(game.getStep().getCurrentPlayer())) {
            throw new GlobalException("Step not waiting", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (!player.getDeck().stream().map(Card::getId).collect(Collectors.toList()).containsAll(cards)) {
            throw new GlobalException("Player dont have cards from collection", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }

        player.setHappiness(calculateHappiness(player, cards));
        player.removeCards(cards);
        player.setStepFinished(true);
        game.getStep().setStatus(Status.FINISHED);
        playerService.savePlayer(player);
        saveGame(game);
        generalGameService.nextStep(game.getId(), this);
        return getGame(playerId);
    }

    private long calculateHappiness(Player player, List<Long> cards) {
        var favoriteAdditional = player.getCharacter().getFavoriteCards().stream().filter(card -> cards.contains(card.getId())).count();
        var favoriteDayAdditional = player.getCharacter().getFavoriteEventDays().stream().filter(day -> day.getId().equals(player.getCurrentDay().getId())).count();
        var holiday = 0L;
        if (player.getCurrentDay().isHoliday() && player.getCurrentDay().getHolidayCard() != null) {
            holiday = player.getCharacter().getFavoriteHolidaysCards().stream().filter(card -> card.getId().equals(player.getCurrentDay().getHolidayCard().getId())).count();
            player.getCurrentDay().setHolidayCard(null);
        }
        return player.getHappiness() + cards.size() + favoriteAdditional + favoriteDayAdditional + holiday;
    }

    @Override
    public Game joinIntoTheGame(String playerId, Long gameId) {
        Game game = gameRepository.getOne(gameId);
        if (game.getPlayers().size() >= game.getCapacity()) {
            throw new GlobalException("Players count more than capacity", GlobalExceptionCode.CAPACITY_LIMIT_REACHED);
        }
        Player player = playerService.getOrCreatePlayer(playerId);
        checkPlayerInGame(player);
        game.addPlayer(player);
        return gameRepository.save(game);
    }

    @Override
    public Game leftFromTheGame(String playerId) {
        var game = getGame(playerId);
        if (game == null) {
            throw new GlobalException("Game not found", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        game.removePlayer(playerId);
        return gameRepository.save(game);
    }

    @Override
    public Game saveGame(Game game) {
        return gameRepository.saveAndFlush(game);
    }

    @Override
    public void clearGames(Long id) {
        if (id == null || id.equals(0L)) {
            gameRepository.deleteAll();
        } else {
            Game one = gameRepository.getOne(id);
            gameRepository.delete(one);
        }
    }

    private void checkPlayerInGame(Player player) {
        Set<Player> playersInGame = gameRepository.findAll().stream().flatMap(repoGame -> repoGame.getPlayers().stream()).collect(Collectors.toSet());
        if (playersInGame.contains(player)) {
            throw new GlobalException("Player already in game", GlobalExceptionCode.PLAYER_ALREADY_IN_GAME);
        }
    }
}
