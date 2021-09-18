package ee.pacyorky.gameserver.gameserver.services.game.impl.GameExecutors;

import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.entities.game.Status;
import ee.pacyorky.gameserver.gameserver.entities.game.StepStatus;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;
import ee.pacyorky.gameserver.gameserver.repositories.GameRepository;
import ee.pacyorky.gameserver.gameserver.services.game.EventDayService;
import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public abstract class AbstractExecutor implements Runnable {

    protected static final int maxAttempt = 10;
    protected final GameRepository gameRepository;
    protected final PlayerService playerService;
    protected final EventDayService eventDayService;
    protected final Long gameId;
    protected final ExecutorCallback callback;
    protected final boolean silently;

    public AbstractExecutor(ExecutorSettings executorSettings, boolean silently) {
        this.gameRepository = executorSettings.getGameRepository();
        this.playerService = executorSettings.getPlayerService();
        this.eventDayService = executorSettings.getEventDayService();
        this.gameId = executorSettings.getGameId();
        this.callback = executorSettings.getExecutorCallback();
        this.silently = silently;
    }

    @Override
    public void run() {
        try {
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

    protected void sleepGame() throws InterruptedException {
        var game = getGame(gameId);
        for (int i = 0; i < maxAttempt; i++) {
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

    private void sleep() throws InterruptedException {
        var game = getGame(gameId);
        var time = LocalDateTime.now().until(game.getNextStepAt(), ChronoUnit.MILLIS);
        Thread.sleep(Math.abs(time));
    }

    protected abstract void doStepPart() throws InterruptedException;

    protected abstract Logger getLogger();

    protected boolean checkGameCanContinue() {
        var game = getGame(gameId);
        if (game.isNotStarted()) {
            game.finish(Status.CANCELLED);
            saveGame(game);
            getLogger().error("game {} not started", gameId);
            return false;
        }

        if (game.getPlayers().size() < 2) {
            getLogger().warn("players under then 2");
            game.finish(Status.CANCELLED);
            saveGame(game);
            return false;
        }
        return true;
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

    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    public Game getGame(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new GlobalException("Game not found " + gameId, GlobalExceptionCode.INTERNAL_SERVER_ERROR));
    }

    public Game getGame(String playerId) {
        return getGames().stream().filter(game -> game.getPlayers().stream().map(Player::getId).anyMatch(id -> id.equals(playerId))).findFirst().orElse(null);
    }

    @Transactional
    public Game saveGame(Game game) {
        return gameRepository.saveAndFlush(game);
    }

}
