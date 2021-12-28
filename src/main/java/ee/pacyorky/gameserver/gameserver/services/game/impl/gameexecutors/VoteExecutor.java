package ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.entities.game.StepStatus;
import ee.pacyorky.gameserver.gameserver.util.CardUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VoteExecutor extends AbstractExecutor {
    // two minutes by 5 seconds
    private static final int RETRY_VALUE = 120 / 5;
    
    
    public VoteExecutor(ExecutorSettings executorSettings) {
        super(executorSettings, true);
    }
    
    @Override
    protected void doStepPart() throws InterruptedException {
        var game = getGame(gameId);
        
        checkGameStepStatus(StepStatus.WAITING_VOTE);
        
        if (game.getStep().getStepCards().isEmpty()) {
            game.getStep().setStatus(StepStatus.FINISHED);
            saveGame(game);
            callback.success(gameId);
            return;
        }
        
        if (game.isWithComputer()) {
            var currentPlayer = game.getStep().getCurrentPlayer();
            for (Player player : game.getPlayers().stream().filter(Player::isComputer).filter(Predicate.not(currentPlayer::equals)).collect(Collectors.toList())) {
                var cardIndexes = CardUtils.getRandomCardIndexes(game.getStep().getStepCards().size());
                for (int cardIndex : cardIndexes) {
                    game.getStep().getStepCards().get(cardIndex).addVote();
                    player.setVoted(true);
                    playerService.savePlayer(player);
                }
            }
            saveGame(game);
        }
        
        for (int i = 0; i < RETRY_VALUE; i++) {
            game = getGame(gameId);
            var step = game.getStep();
            var biggestPartVoted = game.getPlayers().stream().filter(Predicate.not(Player::isComputer)).filter(Player::isVoted).count() > (game.getPlayers().stream().filter(Predicate.not(Player::isComputer)).count() / 2);
            var allVoted = game.getPlayers().stream().filter(Player::isVoted).count() == (game.getPlayers().size() - 1);
            if ((step.getCardThrownAt().until(LocalDateTime.now(), ChronoUnit.MINUTES) > 2 && biggestPartVoted) || allVoted) {
                break;
            }
            Thread.sleep(5000L);
        }
        game.getStep().setStatus(StepStatus.FINISHED);
        saveGame(game);
        callback.success(gameId);
        
    }
    
    @Override
    protected Logger getLogger() {
        return log;
    }
}
