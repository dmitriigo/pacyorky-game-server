package ee.pacyorky.gameserver.gameserver.services.game;

import ee.pacyorky.gameserver.gameserver.entities.game.EventDay;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;

public interface EventDayService {

    EventDay getStartPosition();

    EventDay getNextDay(Player player, int count);

    EventDay getLastStepDay(Player player, int count);
}
