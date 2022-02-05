package ee.pacyorky.gameserver.gameserver.util;

import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;

public final class GameUtils {
    
    private GameUtils() {
    }
    
    public static void checkGameAndPlayer(Game game, Player player) {
        if (game == null) {
            throw new GlobalException("Game is null", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (player == null) {
            throw new GlobalException("Player is null", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (game.getStep() == null) {
            throw new GlobalException("Step is not created. Null.", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (!player.getId().equals(game.getStep().getCurrentPlayer().getId())) {
            throw new GlobalException("Current player != player", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }
}
