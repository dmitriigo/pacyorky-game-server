package ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;

import ee.pacyorky.gameserver.gameserver.entities.game.Card;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.entities.game.Status;
import ee.pacyorky.gameserver.gameserver.entities.game.StepCard;
import ee.pacyorky.gameserver.gameserver.entities.game.StepStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FinishStepExecutor extends AbstractExecutor {
    public FinishStepExecutor(ExecutorSettings executorSettings) {
        super(executorSettings, false);
    }
    
    @Override
    protected void doStepPart() throws InterruptedException {
        var game = getGame(gameId);
        
        checkGameStepStatus(StepStatus.FINISHED);
        var player = game.getStep().getCurrentPlayer();
        
        if (game.getPlayers().stream().map(Player::getId).anyMatch(id -> id.equals(game.getStep().getCurrentPlayer().getId()))) {
            var cards = game.getStep().getStepCards().stream().filter(card -> card.getVote() >= (game.getPlayers().size() / 2))
                    .map(StepCard::getCard)
                    .map(Card::getId).collect(Collectors.toList());
            
            player.setHappiness(calculateHappiness(player, cards));
            player.removeCards(cards);
            player.setStepFinished(true);
            playerService.savePlayer(player);
        }
        saveGame(game);
        
        if (game.getPlayers().stream().allMatch(Player::isLastStep)) {
            game.finish(Status.FINISHED);
            saveGame(game);
            return;
        }
        
        callback.success(gameId);
    }
    
    private long calculateHappiness(Player player, List<Long> cards) {
        var favoriteAdditional = player.getCharacter().getFavoriteCards().stream().filter(card -> cards.contains(card.getId())).count();
        var favoriteDayAdditional = player.getCharacter().getFavoriteEventDays().stream().filter(day -> day.getId().equals(player.getCurrentDay().getId())).count();
        var holiday = 0L;
        if (player.getCurrentDay().isHoliday() && player.getHolidayCard() != null) {
            holiday = player.getCharacter().getFavoriteHolidaysCards().stream().filter(card -> card.getId().equals(player.getHolidayCard().getId())).count();
        }
        return player.getHappiness() + cards.size() + favoriteAdditional + favoriteDayAdditional + holiday;
    }
    
    @Override
    protected Logger getLogger() {
        return log;
    }
}
