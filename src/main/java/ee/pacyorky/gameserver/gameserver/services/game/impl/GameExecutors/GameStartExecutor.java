package ee.pacyorky.gameserver.gameserver.services.game.impl.GameExecutors;

import ee.pacyorky.gameserver.gameserver.entities.game.Character;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.entities.game.Status;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

import static ee.pacyorky.gameserver.gameserver.util.CardUtils.initPlayersCards;

@Slf4j
public class GameStartExecutor extends AbstractExecutor {


    public GameStartExecutor(ExecutorSettings executorSettings) {
        super(executorSettings, true, true);
    }

    @Override
    protected void doStepPart() throws InterruptedException {
        checkAndStartGame();
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    private void checkAndStartGame() throws InterruptedException {
        boolean started = false;
        for (int i = 0; i < maxAttemptStart; i++) {
            started = startGame();
            if (isNeedStartGame() && started) {
                break;
            } else {
                sleepStart();
            }
        }
        var game = getGame(gameId);
        if (!started) {
            if (game.isWithComputer() && game.getPlayers().size() > 0) {
                callback.success(gameId);
            } else {
                game.finish(Status.CANCELLED);
                saveGame(game);
                callback.fail(gameId);
            }
        }
    }

    private void sleepStart() throws InterruptedException {
        var game = getGame(gameId);
        var time = LocalDateTime.now().until(game.getStartAt(), ChronoUnit.MILLIS);
        Thread.sleep(Math.abs(time));
    }

    private boolean isNeedStartGame() {
        return LocalDateTime.now().isAfter(getGame(gameId).getStartAt());
    }

    private boolean startGame() {
        var game = getGame(gameId);
        if (game.isNotWaiting()) {
            throw new RuntimeException("Game status not waiting");
        }
        if (game.getPlayers().size() < game.getCapacity()) {
            game.setStartAt(LocalDateTime.now().plusSeconds(game.getSecondsBeforeStart()));
            saveGame(game);
            return false;
        }
        callback.success(gameId);
        return true;
    }

}
