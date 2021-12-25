package ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors;

import ee.pacyorky.gameserver.gameserver.entities.game.StepStatus;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.time.LocalDateTime;

@Slf4j
public class CardThrowExecutor extends AbstractExecutor {


    public CardThrowExecutor(ExecutorSettings executorSettings) {
        super(executorSettings, true);
    }
    
    @Override
    protected StepStatus getNextStatusOnPlayerNotInGame() {
        return StepStatus.WAITING_VOTE;
    }
    
    @Override
    protected void doStepPart() throws InterruptedException {
        gameStep();
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    private void gameStep() {
        var game = getGame(gameId);

        checkGameStepStatus(StepStatus.WAITING_CARD);
        game.getStep().setStatus(StepStatus.WAITING_VOTE);
        game.getStep().setCardThrownAt(LocalDateTime.now());
        saveGame(game);

        callback.success(gameId);

    }

}
