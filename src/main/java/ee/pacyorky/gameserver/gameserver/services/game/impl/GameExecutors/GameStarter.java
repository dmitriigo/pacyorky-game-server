package ee.pacyorky.gameserver.gameserver.services.game.impl.GameExecutors;

import ee.pacyorky.gameserver.gameserver.entities.game.Character;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.entities.game.Status;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.Set;
import java.util.UUID;

import static ee.pacyorky.gameserver.gameserver.util.CardUtils.initPlayersCards;

@Slf4j
public class GameStarter extends AbstractExecutor{

    public GameStarter(ExecutorSettings executorSettings) {
        super(executorSettings, false, true);
    }

    @Override
    protected void doStepPart() throws InterruptedException {
        var game = getGame(gameId);
        if (game.isNotWaiting()) {
            callback.fail(gameId);
            throw new RuntimeException("Game status not waiting");
        }
        var playersCount = game.getPlayers().size();
        if (playersCount < 1 || (playersCount < 2 && !game.isWithComputer())) {
            game.finish(Status.CANCELLED);
            saveGame(game);
            callback.fail(gameId);
            return;
        }
        if (game.isWithComputer()) {
            game = getGame(gameId);
            for (int i = game.getPlayers().size(); i < game.getCapacity(); i++) {
                var player = Player.builder().id(UUID.randomUUID().toString()).isComputer(true).build();
                playerService.savePlayer(player);
                game.addPlayer(player);
            }
            saveGame(game);
        }

        game = getGame(gameId);
        Set<Player> players = game.getPlayers();
        for (Player player1 : players) {
            initPlayersCards(player1, game);
            Character character = game.getCharacters().stream().findAny().orElseThrow();
            player1.setCharacter(character);
            game.getCharacters().remove(character);
            player1.setCurrentDay(eventDayService.getStartPosition());
            playerService.savePlayer(player1);
        }
        game.start();
        saveGame(game);
        callback.success(gameId);

    }

    @Override
    protected Logger getLogger() {
        return log;
    }
}
