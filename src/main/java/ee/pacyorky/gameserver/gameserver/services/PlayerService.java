package ee.pacyorky.gameserver.gameserver.services;

import ee.pacyorky.gameserver.gameserver.entities.Player;

public interface PlayerService {

    Player getOrCreatePlayer(String id);

    Player savePlayer(Player player);
}
