package ee.pacyorky.gameserver.gameserver.services.game.impl.GameExecutors;

import ee.pacyorky.gameserver.gameserver.entities.game.*;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class FinishStepExecutor extends AbstractExecutor {
    public FinishStepExecutor(ExecutorSettings executorSettings) {
        super(executorSettings, false);
    }

    @Override
    protected void doStepPart() throws InterruptedException {
        var game = getGame(gameId);

        checkGameStepStatus(StepStatus.FINISHED);
        var player = game.getStep().getCurrentPlayer();
        var cards = game.getStep().getStepCards().stream().filter(card -> card.getVote() >= (game.getPlayers().size() / 2))
                .map(StepCard::getCard)
                .map(Card::getId).collect(Collectors.toList());

        player.setHappiness(calculateHappiness(player, cards));
        player.removeCards(cards);
        player.setStepFinished(true);
        playerService.savePlayer(player);
        saveGame(game);

        if (game.getPlayers().stream().allMatch(Player::isLastStep)) {
            game.finish(Status.FINISHED);
            saveGame(game);
            callback.fail(gameId);
            return;
        }
        if (game.getPlayers().size() < 2) {
            game.finish(Status.CANCELLED);
            saveGame(game);
            callback.fail(gameId);
            return;
        }
        if (!checkGameCanContinue()) {
            callback.fail(gameId);
            return;
        }

        callback.success(gameId);
    }

    private long calculateHappiness(Player player, List<Long> cards) {
        var favoriteAdditional = player.getCharacter().getFavoriteCards().stream().filter(card -> cards.contains(card.getId())).count();
        var favoriteDayAdditional = player.getCharacter().getFavoriteEventDays().stream().filter(day -> day.getId().equals(player.getCurrentDay().getId())).count();
        var holiday = 0L;
        if (player.getCurrentDay().isHoliday() && player.getCurrentDay().getHolidayCard() != null) {
            holiday = player.getCharacter().getFavoriteHolidaysCards().stream().filter(card -> card.getId().equals(player.getCurrentDay().getHolidayCard().getId())).count();
            player.getCurrentDay().setHolidayCard(null);
        }
        return player.getHappiness() + cards.size() + favoriteAdditional + favoriteDayAdditional + holiday;
    }

    @Override
    protected Logger getLogger() {
        return null;
    }
}
