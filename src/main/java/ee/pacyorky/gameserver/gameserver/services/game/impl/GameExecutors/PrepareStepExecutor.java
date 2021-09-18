package ee.pacyorky.gameserver.gameserver.services.game.impl.GameExecutors;

import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.entities.game.Step;
import ee.pacyorky.gameserver.gameserver.entities.game.StepStatus;
import ee.pacyorky.gameserver.gameserver.services.game.impl.GameManagerImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.Comparator;

@Slf4j
public class PrepareStepExecutor extends AbstractExecutor {


    public PrepareStepExecutor(ExecutorSettings executorSettings) {
        super(executorSettings, true);
    }

    private void initNewStep() throws InterruptedException {
        var game = getGame(gameId);
        for (Player player : game.getPlayers()) {
            GameManagerImpl.initPlayersCards(player, game);
            player.setVoted(false);
            playerService.savePlayer(player);
        }
        var player = calculatePlayer();
        playerService.savePlayer(player);
        var step = Step.builder()
                .status(StepStatus.WAITING_DICE.getId())
                .currentPlayer(player)
                .build();
        game.setStep(step);
        game.setNextStepAt(LocalDateTime.now().plusSeconds(game.getSecondsForStep()));
        game.plusStep();
        saveGame(game);
        sleepGame();
    }

    private Player calculatePlayer() {
        var game = getGame(gameId);
        if (game.getPlayers().stream().allMatch(Player::isStepFinished)) {
            for (Player player : game.getPlayers()) {
                player.setStepFinished(false);
                playerService.savePlayer(player);
            }
        }
        return game.getPlayers().stream().filter(player -> !player.isStepFinished() && !player.isLastStep())
                .min(Comparator.comparing(Player::getId)).orElseThrow();
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