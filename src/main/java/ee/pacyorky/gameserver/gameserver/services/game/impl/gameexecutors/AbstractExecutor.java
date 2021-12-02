package ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors;

import ee.pacyorky.gameserver.gameserver.config.AgoraProperties;
import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.entities.game.Status;
import ee.pacyorky.gameserver.gameserver.entities.game.StepStatus;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;
import ee.pacyorky.gameserver.gameserver.repositories.dao.GameDao;
import ee.pacyorky.gameserver.gameserver.services.game.EventDayService;
import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;

public abstract class AbstractExecutor implements Runnable {

    protected final int maxAttemptStart;
    protected final int maxAttemptStep;
    protected static final long COMPUTER_TIMEOUT = 5L;
    protected final GameDao gameDao;
    protected final PlayerService playerService;
    protected final EventDayService eventDayService;
    protected final Long gameId;
    protected final ExecutorCallback callback;
    private final boolean silently;
    private final boolean skipGameContinueCheck;
    protected final AgoraProperties agoraProperties;

    protected AbstractExecutor(ExecutorSettings executorSettings, boolean silently, boolean skipGameContinueCheck) {
        this.gameDao = executorSettings.getGameDao();
        this.playerService = executorSettings.getPlayerService();
        this.eventDayService = executorSettings.getEventDayService();
        this.gameId = executorSettings.getGameId();
        this.callback = executorSettings.getExecutorCallback();
        this.silently = silently;
        this.skipGameContinueCheck = skipGameContinueCheck;
        this.maxAttemptStart = executorSettings.getAppProperties().getMaxAttemptsForStart();
        this.maxAttemptStep = executorSettings.getAppProperties().getMaxAttemptsForStep();
        this.agoraProperties = executorSettings.getAgoraProperties();

    }

    protected AbstractExecutor(ExecutorSettings executorSettings, boolean silently) {
        this(executorSettings, silently, false);
    }

    @Override
    public void run() {
        try {
            if (!skipGameContinueCheck && gameCanNotContinue()) {
                return;
            }
            var game = getGame(gameId);
            if (game.getStatus() == Status.STARTED && game.getStep() != null && getNextStatusOnPlayerNotInGame() != null &&
                    game.getPlayers().stream().map(Player::getId).noneMatch(id -> id.equals(game.getStep().getCurrentPlayer().getId()))) {
                game.getStep().setStatus(getNextStatusOnPlayerNotInGame());
                saveGame(game);
                callback.success(gameId);
                return;
            }
            doStepPart();
        } catch (InterruptedException ie) {
            if (!silently) {
                getLogger().error("Thread was interrupted", ie);
            }
        } catch (Exception e) {
            getLogger().error("Error when step in game {}", gameId, e);
            callback.forceFinish(gameId);
        } finally {
            Thread.currentThread().interrupt();
        }
    }
    
    protected abstract StepStatus getNextStatusOnPlayerNotInGame();

    protected void sleepGame() throws InterruptedException {
        var game = getGame(gameId);
        for (int i = 0; i < maxAttemptStep; i++) {
            if (LocalDateTime.now().isAfter(getGame(gameId).getNextStepAt())) {
                game.setNextStepAt(LocalDateTime.now().plusSeconds(game.getSecondsForStep()));
                saveGame(game);
            } else {
                sleep();
            }
        }
        getLogger().warn("Player {} war removed because inactive", game.getStep().getCurrentPlayer().getId());
        var player = game.getStep().getCurrentPlayer();
        game.removePlayer(player.getId());
        game.getStep().setStatus(StepStatus.FINISHED);
        saveGame(game);
        callback.fail(gameId);
    }

    protected void sleep() throws InterruptedException {
        var game = getGame(gameId);
        var time = LocalDateTime.now().until(game.getNextStepAt(), ChronoUnit.MILLIS);
        Thread.sleep(Math.abs(time));
    }

    protected abstract void doStepPart() throws InterruptedException;

    protected abstract Logger getLogger();

    protected boolean gameCanNotContinue() {
        var game = getGame(gameId);
        if (game.isNotStarted()) {
            getLogger().error("game {} not started", gameId);
            callback.forceFinish(gameId);
            return true;
        }

        if (!game.isWithComputer() && game.getPlayers().size() < 2) {
            getLogger().warn("players under then 2 and computer not");
            callback.forceFinish(gameId);
            return true;
        }
        if (game.isWithComputer() && game.getPlayers().stream().allMatch(Player::isComputer)) {
            getLogger().warn("players under then 1 and computers");
            callback.forceFinish(gameId);
            return true;
        }
        return false;
    }

    protected void checkGameStepStatus(StepStatus status) {
        var game = getGame(gameId);
        if (game == null) {
            throw new GlobalException("Game is null", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (game.getStep() == null) {
            throw new GlobalException("Step is null", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (game.getStep().getStatus() != status) {
            throw new GlobalException("status is not " + status, GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }


    protected Game saveGame(Game game) {
        return gameDao.saveGame(game);
    }

    protected Game getGame(Long gameId) {
        return gameDao.getGame(gameId);
    }

}
