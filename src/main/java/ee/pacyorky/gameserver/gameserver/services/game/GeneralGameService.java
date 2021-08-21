package ee.pacyorky.gameserver.gameserver.services.game;

import ee.pacyorky.gameserver.gameserver.entities.game.Game;

public interface GeneralGameService {

    void nextStep(Long gameId, GameManager gameManager);

    void setUpStart(Game savedGame, GameManager gameManager);

    void clearUnused();
}
