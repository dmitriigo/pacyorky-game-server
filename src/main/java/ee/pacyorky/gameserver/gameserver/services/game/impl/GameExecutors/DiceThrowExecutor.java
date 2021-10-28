package ee.pacyorky.gameserver.gameserver.services.game.impl.GameExecutors;

import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.entities.game.StepStatus;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
public class DiceThrowExecutor extends AbstractExecutor {
    public DiceThrowExecutor(ExecutorSettings executorSettings) {
        super(executorSettings, true);
    }

    @Override
    protected void doStepPart() throws InterruptedException {
        var game = getGame(gameId);
        checkGameStepStatus(StepStatus.WAITING_DICE);

        var counter = getCounter(game);
        var player = game.getStep().getCurrentPlayer();
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
        var step = game.getStep();
        step.setCounter(counter);
        step.setCurrentPlayer(player);
        step.setStatus(StepStatus.WAITING_CARD);
        game.setStep(step);
        game.setNextStepAt(LocalDateTime.now().plusSeconds(player.isComputer() ? computerTimeout : game.getSecondsForStep()));
        saveGame(game);
        if (player.isComputer()) {
            sleep();
            callback.success(gameId);
        } else {
            sleepGame();
        }
    }

    private Integer getCounter(Game game) {
        return new Random().nextInt(game.getCapacity() > 4 ? 11 : 6) + (game.getCapacity() > 4 ? 2 : 1);
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
