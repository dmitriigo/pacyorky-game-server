package ee.pacyorky.gameserver.gameserver.services.game;

import ee.pacyorky.gameserver.gameserver.entities.game.Player;

public interface PlayerService {

    Player getOrCreatePlayer(String id);

    Player savePlayer(Player player);

    void clearPlayers();
}
