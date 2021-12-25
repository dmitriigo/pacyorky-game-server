package ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors;

import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.entities.game.Step;
import ee.pacyorky.gameserver.gameserver.entities.game.StepStatus;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

import static ee.pacyorky.gameserver.gameserver.util.CardUtils.initPlayersCards;

@Slf4j
public class PrepareStepExecutor extends AbstractExecutor {


    public PrepareStepExecutor(ExecutorSettings executorSettings) {
        super(executorSettings, true);
    }

    private void initNewStep() throws InterruptedException {
        var game = getGame(gameId);
        for (Player player : game.getPlayers()) {
            initPlayersCards(player, game);
            player.setVoted(false);
            playerService.savePlayer(player);
        }
        var playerOptional = calculatePlayer();
        if (playerOptional.isEmpty()) {
            log.error(game.getPlayers().toString());
            callback.forceFinish(gameId);
            return;
        }
        var player = playerOptional.get();
        playerService.savePlayer(player);
        var step = Step.builder()
                .status(StepStatus.WAITING_DICE.getId())
                .currentPlayer(player)
                .build();
        game.setStep(step);
        game.setNextStepAt(LocalDateTime.now().plusSeconds(player.isComputer() ? COMPUTER_TIMEOUT : game.getSecondsForStep()));
        game.plusStep();
        saveGame(game);
        if (player.isComputer()) {
            sleep();
            callback.success(gameId);
        } else {
            sleepGame();
        }

    }

    private Optional<Player> calculatePlayer() {
        var game = getGame(gameId);
        if (game.getPlayers().stream().allMatch(player -> player.isStepFinished() || player.isLastStep())) {
            for (Player player : game.getPlayers()) {
                player.setStepFinished(false);
                playerService.savePlayer(player);
            }
        }
        return game.getPlayers().stream().filter(player -> !player.isStepFinished() && !player.isLastStep())
                .min(Comparator.comparing(Player::getId));
    }
    
    @Override
    protected StepStatus getNextStatusOnPlayerNotInGame() {
        return StepStatus.WAITING_DICE;
    }
    
    @Override
    protected void doStepPart() throws InterruptedException {
        initNewStep();
    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
