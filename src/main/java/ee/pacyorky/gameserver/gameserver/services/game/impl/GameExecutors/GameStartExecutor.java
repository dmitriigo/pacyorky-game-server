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
        super(executorSettings, false, true);
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
        for (int i = 0; i < maxAttempt; i++) {
            if (isNeedStartGame() && startGame()) {
                break;
            } else {
                sleepStart();
            }
        }
        var game = getGame(gameId);
        if (game.isNotStarted()) {
            game.finish(Status.CANCELLED);
            saveGame(game);
            callback.fail(gameId);
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

    private boolean startGame() throws InterruptedException {
        var game = getGame(gameId);
        if (game.isNotWaiting()) {
            throw new RuntimeException("Game status not waiting");
        }
        if (!game.isWithComputer() && game.getPlayers().size() < 2) {
            game.setStartAt(game.getStartAt().plusSeconds(game.getSecondsBeforeStart()));
            saveGame(game);
            return false;
        } else if (game.isWithComputer()) {
            sleepStart();
            game = getGame(gameId);
            for (int i = game.getPlayers().size(); i < game.getCapacity(); i++) {
                var player = Player.builder().id(UUID.randomUUID().toString()).isComputer(true).build();
                playerService.savePlayer(player);
                game.addPlayer(player);
            }
            saveGame(game);
        }
        game = getGame(gameId);
        game.start();
        saveGame(game);
        Set<Player> players = game.getPlayers();
        for (Player player1 : players) {
            initPlayersCards(player1, game);
            Character character = game.getCharacters().stream().findAny().orElseThrow();
            player1.setCharacter(character);
            game.getCharacters().remove(character);
            player1.setCurrentDay(eventDayService.getStartPosition());
            playerService.savePlayer(player1);
        }
        saveGame(game);
        callback.success(gameId);
        return true;
    }

}
