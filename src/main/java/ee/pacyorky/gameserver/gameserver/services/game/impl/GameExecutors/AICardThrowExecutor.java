package ee.pacyorky.gameserver.gameserver.services.game.impl.GameExecutors;

import ee.pacyorky.gameserver.gameserver.entities.game.StepCard;
import ee.pacyorky.gameserver.gameserver.util.CardUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AICardThrowExecutor extends AbstractExecutor {

    public AICardThrowExecutor(ExecutorSettings executorSettings) {
        super(executorSettings, false);
    }

    @Override
    protected void doStepPart() throws InterruptedException {
        var game = getGame(gameId);
        if (!game.isWithComputer()) {
            throw new RuntimeException("Game is not with computer");
        }
        var player = game.getStep().getCurrentPlayer();
        if (!player.isComputer()) {
            throw new RuntimeException("Player is not computer");
        }

        var cardsIndex = CardUtils.getRandomCardIndexes(player.getDeck().size());
        List<StepCard> cards = new ArrayList<>();
        for (int card : cardsIndex) {
            cards.add(StepCard.builder().card(player.getDeck().get(card)).build());
        }
        game.getStep().setStepCards(cards);
        game.setNextStepAt(LocalDateTime.now().plusSeconds(game.getSecondsForStep()));
        gameDao.saveGame(game);
        sleep();
        callback.success(gameId);

    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
