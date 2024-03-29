package ee.pacyorky.gameserver.gameserver.services.game.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.LongConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ee.pacyorky.gameserver.gameserver.config.AgoraProperties;
import ee.pacyorky.gameserver.gameserver.config.AppProperties;
import ee.pacyorky.gameserver.gameserver.entities.game.Status;
import ee.pacyorky.gameserver.gameserver.entities.game.StepStatus;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;
import ee.pacyorky.gameserver.gameserver.repositories.dao.GameDao;
import ee.pacyorky.gameserver.gameserver.services.game.EventDayService;
import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors.AICardThrowExecutor;
import ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors.CardThrowExecutor;
import ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors.DiceThrowExecutor;
import ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors.ExecutorCallback;
import ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors.ExecutorSettings;
import ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors.FinishStepExecutor;
import ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors.GameStartExecutor;
import ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors.GameStarter;
import ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors.PrepareStepExecutor;
import ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors.VoteExecutor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GeneralGameService {
    
    private final PlayerService playerService;
    private final EventDayService eventDayService;
    private final ExecutorService executorService;
    private final Map<Long, Future<?>> games = new ConcurrentHashMap<>();
    private final GameDao gameDao;
    private final AppProperties appProperties;
    private final AgoraProperties agoraProperties;
    
    @Autowired
    public GeneralGameService(PlayerService playerService, EventDayService eventDayService, AppProperties properties, GameDao gameDao, AppProperties appProperties, AgoraProperties agoraProperties) {
        this.playerService = playerService;
        this.eventDayService = eventDayService;
        this.executorService = Executors.newFixedThreadPool(properties.getMaxGames());
        this.gameDao = gameDao;
        this.appProperties = appProperties;
        this.agoraProperties = agoraProperties;
    }
    
    public void startGame(Long gameId) {
        var future = executorService.submit(new GameStartExecutor(buildSettings(gameId, gameStarter(), l -> games.remove(gameId))));
        games.put(gameId, future);
    }
    
    public void forceStart(Long gameId) {
        var future = games.get(gameId);
        if (future == null || future.isDone() || future.isCancelled()) {
            throw new GlobalException("Game not ready for start", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        future.cancel(true);
        gameStarter().accept(gameId);
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
        if (status == StepStatus.FINISHED) {
            games.get(gameId).cancel(true);
            finishStepConsumer().accept(gameId);
            return;
        }
        throw new GlobalException("Step Status not supported " + status, GlobalExceptionCode.INTERNAL_SERVER_ERROR);
    }
    
    private LongConsumer gameStarter() {
        return gameId -> {
            var future = executorService.submit(new GameStarter(buildSettings(gameId, startStepConsumer(), l -> games.remove(gameId))));
            games.put(gameId, future);
        };
    }
    
    private LongConsumer startStepConsumer() {
        return gameId -> {
            var future = executorService.submit(new PrepareStepExecutor(buildSettings(gameId, throwDiceConsumer(), finishStepConsumer())));
            games.put(gameId, future);
        };
    }
    
    private LongConsumer throwDiceConsumer() {
        return gameId -> {
            var future = executorService.submit(new DiceThrowExecutor(buildSettings(gameId, throwCardAIConsumer(), finishStepConsumer())));
            games.put(gameId, future);
        };
    }
    
    private LongConsumer throwCardAIConsumer() {
        return gameId -> {
            var future = executorService.submit(new AICardThrowExecutor(buildSettings(gameId, throwCardConsumer(), finishStepConsumer())));
            games.put(gameId, future);
        };
    }
    
    private LongConsumer throwCardConsumer() {
        return gameId -> {
            var future = executorService.submit(new CardThrowExecutor(buildSettings(gameId, voteConsumer(), finishStepConsumer())));
            games.put(gameId, future);
        };
    }
    
    private LongConsumer voteConsumer() {
        return gameId -> {
            var future = executorService.submit(new VoteExecutor(buildSettings(gameId, finishStepConsumer(), finishStepConsumer())));
            games.put(gameId, future);
        };
    }
    
    private LongConsumer finishStepConsumer() {
        return gameId -> {
            var future = executorService.submit(new FinishStepExecutor(buildSettings(gameId, startStepConsumer(), this::finishGame)));
            games.put(gameId, future);
        };
    }
    
    private void finishGame(Long gameId) {
        var game = gameDao.getGame(gameId);
        game.finish(Status.CANCELLED);
        gameDao.saveGame(game);
    }
    
    private ExecutorSettings buildSettings(Long gameId, LongConsumer success, LongConsumer fail) {
        var callback = ExecutorCallback.builder()
                .success(success)
                .fail(fail)
                .forceFinish(this::finishGame)
                .build();
        return ExecutorSettings.builder()
                .gameId(gameId)
                .eventDayService(eventDayService)
                .playerService(playerService)
                .gameDao(gameDao)
                .executorCallback(callback)
                .appProperties(appProperties)
                .agoraProperties(agoraProperties)
                .build();
    }
    
    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 60)
    public void clearUnused() {
        games.entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue().isCancelled() || entry.getValue().isDone());
    }
}
