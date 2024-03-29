package ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import ee.pacyorky.gameserver.gameserver.entities.game.StepCard;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;
import ee.pacyorky.gameserver.gameserver.util.CardUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AICardThrowExecutor extends AbstractExecutor {
    
    public AICardThrowExecutor(ExecutorSettings executorSettings) {
        super(executorSettings, false);
    }
    
    @Override
    protected void doStepPart() throws InterruptedException {
        var game = getGame(gameId);
        if (!game.isWithComputer()) {
            throw new GlobalException("Game is not with computer", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        var player = game.getStep().getCurrentPlayer();
        if (!player.isComputer()) {
            throw new GlobalException("Player is not computer", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        
        var cardsIndex = CardUtils.getRandomCardIndexes(player.getDeck().size());
        List<StepCard> cards = new ArrayList<>();
        for (int card : cardsIndex) {
            cards.add(StepCard.builder().card(player.getDeck().get(card)).build());
        }
        game.getStep().setStepCards(cards);
        game.setNextStepAt(LocalDateTime.now().plusSeconds(COMPUTER_TIMEOUT));
        gameDao.saveGame(game);
        sleep();
        callback.success(gameId);
        
    }
    
    @Override
    protected Logger getLogger() {
        return log;
    }
}
