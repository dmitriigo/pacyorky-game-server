package ee.pacyorky.gameserver.gameserver.services;

import ee.pacyorky.gameserver.gameserver.entities.Game;

public interface GeneralGameService {

    void nextStep(Long gameId, GameManager gameManager);

    void setUpStart(Game savedGame, GameManager gameManager);
}
