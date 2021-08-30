package ee.pacyorky.gameserver.gameserver.services.game.impl.GameExecutors;

import ee.pacyorky.gameserver.gameserver.entities.game.Character;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.entities.game.Status;
import ee.pacyorky.gameserver.gameserver.services.game.EventDayService;
import ee.pacyorky.gameserver.gameserver.services.game.GameManager;
import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.function.BiConsumer;

import static ee.pacyorky.gameserver.gameserver.services.game.impl.GeneralGameServiceImpl.initPlayersCards;

@Slf4j
public class GameStartExecutor implements Runnable {

    private static final int maxAttempt = 10;
    private final GameManager gameManager;
    private final PlayerService playerService;
    private final EventDayService eventDayService;
    private final Long gameId;
    private final BiConsumer<Long, GameManager> gameConsumer;

    public GameStartExecutor(GameManager gameManager, PlayerService playerService, EventDayService eventDayService, Long gameId, BiConsumer<Long, GameManager> gameConsumer) {
        this.gameManager = gameManager;
        this.playerService = playerService;
        this.eventDayService = eventDayService;
        this.gameId = gameId;
        this.gameConsumer = gameConsumer;
    }

    @Override
    public void run() {
        try {
            checkAndStartGame();
        } catch (InterruptedException ie) {
            log.warn("Exception then start game {} interrupted", gameId, ie);
        } catch (Exception e) {
            log.error("Exception then start game {}", gameId, e);
        } finally {
            Thread.currentThread().interrupt();
        }
    }

    private void checkAndStartGame() throws InterruptedException {
        for (int i = 0; i < maxAttempt; i++) {
            if (isNeedStartGame() && startGame()) {
                break;
            } else {
                sleep();
            }
        }
        var game = gameManager.getGame(gameId);
        if (game.isNotStarted()) {
            game.finish(Status.CANCELLED);
            gameManager.saveGame(game);
        }
    }

    private void sleep() throws InterruptedException {
        var game = gameManager.getGame(gameId);
        var time = LocalDateTime.now().until(game.getStartAt(), ChronoUnit.MILLIS);
        Thread.sleep(Math.abs(time));
    }

    private boolean isNeedStartGame() {
        return LocalDateTime.now().isAfter(gameManager.getGame(gameId).getStartAt());
    }

    private boolean startGame() {
        var game = gameManager.getGame(gameId);
        if (game.isNotWaiting()) {
            throw new RuntimeException("Game status not waiting");
        }
        if (game.getPlayers().size() < 2) {
            game.setStartAt(game.getStartAt().plusSeconds(game.getSecondsBeforeStart()));
            gameManager.saveGame(game);
            return false;
        }
        game.start();
        gameManager.saveGame(game);
        Set<Player> players = game.getPlayers();
        for (Player player1 : players) {
            initPlayersCards(player1, game);
            Character character = game.getCharacters().stream().findAny().orElseThrow();
            player1.setCharacter(character);
            game.getCharacters().remove(character);
            player1.setCurrentDay(eventDayService.getStartPosition());
            playerService.savePlayer(player1);
        }
        gameManager.saveGame(game);
        gameConsumer.accept(gameId, gameManager);
        return true;
    }

}
