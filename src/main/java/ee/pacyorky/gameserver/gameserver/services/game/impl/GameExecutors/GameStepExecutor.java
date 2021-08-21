package ee.pacyorky.gameserver.gameserver.services.game.impl.GameExecutors;

import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.entities.game.Status;
import ee.pacyorky.gameserver.gameserver.entities.game.Step;
import ee.pacyorky.gameserver.gameserver.services.game.EventDayService;
import ee.pacyorky.gameserver.gameserver.services.game.GameManager;
import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import ee.pacyorky.gameserver.gameserver.services.game.impl.GeneralGameServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Random;

@Slf4j
public class GameStepExecutor implements Runnable {
    private static final int maxAttempt = 10;
    private final GameManager gameManager;
    private final PlayerService playerService;
    private final EventDayService eventDayService;
    private final Long gameId;


    public GameStepExecutor(GameManager gameManager, PlayerService playerService, EventDayService eventDayService, Long gameId) {
        this.gameManager = gameManager;
        this.playerService = playerService;
        this.eventDayService = eventDayService;
        this.gameId = gameId;
    }

    @Override
    public void run() {
        try {
            gameStep();
        } catch (InterruptedException ie) {
            //ignored
        } catch (Exception e) {
            log.error("Error when step in game {}", gameId, e);
        } finally {
            Thread.currentThread().interrupt();
        }
    }

    private void gameStep() throws InterruptedException {
        var game = gameManager.getGame(gameId);

        if (game.isNotStarted()) {
            game.finish(Status.CANCELLED);
            gameManager.saveGame(game);
            log.error("game {} not started", gameId);
            return;
        }

        if (game.getPlayers().size() < 2) {
            log.warn("players under then 2");
            game.finish(Status.CANCELLED);
            gameManager.saveGame(game);
            return;
        }

        if (game.getStep() == null || game.getStep().getStatus() == Status.FINISHED) {
            initNewStep();
            for (Player player : game.getPlayers()) {
                GeneralGameServiceImpl.initPlayersCards(player, game);
            }
            game = gameManager.getGame(gameId);
        }

        if (game.getPlayers().stream().allMatch(Player::isLastStep)) {
            game.finish(Status.FINISHED);
            gameManager.saveGame(game);
            return;
        }

        while (gameManager.getGame(gameId).getPlayers().size() >= 2) {
            for (int i = 0; i < maxAttempt; i++) {
                if (LocalDateTime.now().isAfter(gameManager.getGame(gameId).getNextStepAt())) {
                    game.setNextStepAt(LocalDateTime.now().plusSeconds(game.getSecondsForStep()));
                    gameManager.saveGame(game);
                } else {
                    sleep();
                }
            }
            game.removePlayer(game.getStep().getCurrentPlayer().getId());
            gameManager.saveGame(game);
            initNewStep();
        }

        game.finish(Status.CANCELLED);
        log.warn("Game {} was cancelled because players below then 2", gameId);

    }

    private void sleep() throws InterruptedException {
        var game = gameManager.getGame(gameId);
        var time = LocalDateTime.now().until(game.getNextStepAt(), ChronoUnit.MILLIS);
        Thread.sleep(Math.abs(time));
    }

    private void initNewStep() {
        var game = gameManager.getGame(gameId);
        var counter = getCounter();
        var player = calculatePlayer();
        var newDay = eventDayService.getNextDay(player, counter);
        if (newDay == null) {
            player.setLastStep(true);
            newDay = eventDayService.getLastStepDay(player, counter);
        }
        if (newDay.isHoliday()) {
            newDay.setHolidayCard(game.getHolidayCard());
        }
        player.setCurrentDay(newDay);
        playerService.savePlayer(player);
        var step = Step.builder().status(Status.WAITING).counter(counter).currentPlayer(player).build();
        game.setStep(step);
        game.setNextStepAt(LocalDateTime.now().plusSeconds(game.getSecondsForStep()));
        game.plusStep();
        gameManager.saveGame(game);
    }

    private Player calculatePlayer() {
        var game = gameManager.getGame(gameId);
        if (game.getPlayers().stream().allMatch(Player::isStepFinished)) {
            for (Player player : game.getPlayers()) {
                player.setStepFinished(false);
                playerService.savePlayer(player);
            }
        }
        return game.getPlayers().stream().filter(player -> !player.isStepFinished() && !player.isLastStep())
                .min(Comparator.comparing(Player::getId)).orElseThrow();
    }

    private Integer getCounter() {
        return new Random().nextInt(6) + 1;
    }
}
