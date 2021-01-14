package ee.pacyorky.gameserver.gameserver.services;

import ee.pacyorky.gameserver.gameserver.entities.Game;
import ee.pacyorky.gameserver.gameserver.entities.Player;

public interface GeneralGameService {
    Player startGame(Long gameId, String playerId);

    Game nextStep(Long gameId, String playerId);
}
