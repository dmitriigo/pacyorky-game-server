package ee.pacyorky.gameserver.gameserver.services;

import ee.pacyorky.gameserver.gameserver.entities.EventDay;
import ee.pacyorky.gameserver.gameserver.entities.Player;

public interface EventDayService {

    EventDay getStartPosition();

    EventDay getNextDay(Player player, int count);
}
