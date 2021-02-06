package ee.pacyorky.gameserver.gameserver.services.impl;

import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.entities.Game;
import ee.pacyorky.gameserver.gameserver.entities.Player;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;
import ee.pacyorky.gameserver.gameserver.repositories.EventDayRepository;
import ee.pacyorky.gameserver.gameserver.repositories.GameRepository;
import ee.pacyorky.gameserver.gameserver.services.DeckService;
import ee.pacyorky.gameserver.gameserver.services.GameService;
import ee.pacyorky.gameserver.gameserver.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    private final DeckService deckService;

    private final EventDayRepository eventDayRepository;

    private final PlayerService playerService;

    private static final Long maxGames = 10L;

    private static final Long secondsBeforeStart = 20L;

    private static final Long secondsBeforeNextStep = 60L;

    @Override
    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    @Override
    public Game getGame(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow();
    }

    @Override
    public Game createGame(String playerId, GameCreationDto gameCreationDto) {

        if (gameRepository.count() >= maxGames) {
            return null;
        }

        Game game = Game.builder()
                .calendar(eventDayRepository.findAll())
                .dishesDeck(deckService.getDishesDeck())
                .holidaysDeck(deckService.getHolidaysDeck())
                .ritualsDeck(deckService.getRitualsDeck())
                .stuffDeck(deckService.getStuffDeck())
                .startAt(LocalDateTime.now().plusSeconds(secondsBeforeStart))
                .capacity(gameCreationDto.getCapacity())
                .password(gameCreationDto.getPassword())
                .privateRoom(gameCreationDto.isPrivateRoom())
                .withComputer(gameCreationDto.isWithComputer())
                .name(gameCreationDto.getName())
                .characters(deckService.getCharacterDeck())
                .build();
        game.addPlayer(playerService.getOrCreatePlayer(playerId));
        return gameRepository.save(game);
    }

    @Override
    public Game joinIntoTheGame(String playerId, Long gameId) {
        Game game = gameRepository.getOne(gameId);
        if (game.getPlayers().size() >= game.getCapacity())
            throw new GlobalException("Players count more than capacity", GlobalExceptionCode.CAPACITY_LIMIT_REACHED);
        Player player = playerService.getOrCreatePlayer(playerId);
        Set<Player> playersInGame = gameRepository.findAll().stream().flatMap(repoGame -> repoGame.getPlayers().stream()).collect(Collectors.toSet());
        if (playersInGame.contains(player))
            throw new GlobalException("Player already in game", GlobalExceptionCode.PLAYER_ALREADY_IN_GAME);
        game.addPlayer(player);
        return gameRepository.save(game);
    }

    @Override
    public Game leftFromTheGame(String playerId, Long gameId) {
        Game game = gameRepository.getOne(gameId);
        game.removePlayer(playerId);
        return gameRepository.save(game);
    }

    @Override
    public Game saveGame(Game game) {
        return gameRepository.saveAndFlush(game);
    }
}
