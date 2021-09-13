package ee.pacyorky.gameserver.gameserver.services.game.impl;

import ee.pacyorky.gameserver.gameserver.config.AppProperties;
import ee.pacyorky.gameserver.gameserver.entities.game.Status;
import ee.pacyorky.gameserver.gameserver.entities.game.StepStatus;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;
import ee.pacyorky.gameserver.gameserver.repositories.GameRepository;
import ee.pacyorky.gameserver.gameserver.services.game.EventDayService;
import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import ee.pacyorky.gameserver.gameserver.services.game.impl.GameExecutors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

@Service
@Slf4j
public class GeneralGameService {

    private final PlayerService playerService;
    private final EventDayService eventDayService;
    private final ExecutorService executorService;
    private final Map<Long, Future<?>> games = new ConcurrentHashMap<>();
    private final GameRepository gameRepository;

    @Autowired
    public GeneralGameService(PlayerService playerService, EventDayService eventDayService, AppProperties properties, GameRepository gameRepository) {
        this.playerService = playerService;
        this.eventDayService = eventDayService;
        this.executorService = Executors.newFixedThreadPool(properties.getMaxGames());
        this.gameRepository = gameRepository;
    }

    public void startGame(Long gameId) {
        var future = executorService.submit(new GameStartExecutor(buildSettings(gameId, startStepConsumer(), l -> games.remove(gameId))));
        games.put(gameId, future);
    }

    public void doStepPart(Long gameId, StepStatus status) {
        if (!games.containsKey(gameId)) {
            throw new GlobalException("Game not wait player decision", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (status == StepStatus.WAITING_DICE) {
            games.get(gameId).cancel(true);
            throwDiceConsumer().accept(gameId);
            return;
        }
        if (status == StepStatus.WAITING_CARD) {
            games.get(gameId).cancel(true);
            throwCardConsumer().accept(gameId);
            return;
        }
        throw new GlobalException("Step Status not supported " + status, GlobalExceptionCode.INTERNAL_SERVER_ERROR);
    }

    private Consumer<Long> startStepConsumer() {
        return (gameId) -> {
            var future = executorService.submit(new PrepareStepExecutor(buildSettings(gameId, throwDiceConsumer(), finishStepConsumer())));
            games.put(gameId, future);
        };
    }

    private Consumer<Long> throwDiceConsumer() {
        return (gameId) -> {
            var future = executorService.submit(new DiceThrowExecutor(buildSettings(gameId, throwCardConsumer(), finishStepConsumer())));
            games.put(gameId, future);
        };
    }

    private Consumer<Long> throwCardConsumer() {
        return (gameId) -> {
            var future = executorService.submit(new CardThrowExecutor(buildSettings(gameId, voteConsumer(), finishStepConsumer())));
            games.put(gameId, future);
        };
    }

    private Consumer<Long> voteConsumer() {
        return (gameId) -> {
            var future = executorService.submit(new VoteExecutor(buildSettings(gameId, finishStepConsumer(), finishStepConsumer())));
            games.put(gameId, future);
        };
    }

    private Consumer<Long> finishStepConsumer() {
        return (gameId) -> {
            var future = executorService.submit(new FinishStepExecutor(buildSettings(gameId, startStepConsumer(), this::finishGame)));
            games.put(gameId, future);
        };
    }

    private void finishGame(Long gameId) {
        var game = gameRepository.findById(gameId).orElseThrow();
        game.finish(Status.CANCELLED);
        gameRepository.saveAndFlush(game);
    }

    private ExecutorSettings buildSettings(Long gameId, Consumer<Long> success, Consumer<Long> fail) {
        var callback = ExecutorCallback.builder()
                .success(success)
                .fail(fail)
                .build();
        return ExecutorSettings.builder()
                .gameId(gameId)
                .eventDayService(eventDayService)
                .playerService(playerService)
                .gameRepository(gameRepository)
                .executorCallback(callback)
                .build();
    }

    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 60)
    public void clearUnused() {
        games.entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue().isCancelled() || entry.getValue().isDone());
    }
}
