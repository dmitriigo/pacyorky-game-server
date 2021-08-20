package ee.pacyorky.gameserver.gameserver.services.impl;

import ee.pacyorky.gameserver.gameserver.entities.EventDay;
import ee.pacyorky.gameserver.gameserver.entities.Player;
import ee.pacyorky.gameserver.gameserver.repositories.EventDayRepository;
import ee.pacyorky.gameserver.gameserver.services.EventDayService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventDayServiceImpl implements EventDayService {

    private final EventDayRepository eventDayRepository;

    private static final Long startId = 0L;

    private static final Long lastId = 48L;

    @Override
    public EventDay getStartPosition() {
        return eventDayRepository.findById(startId).orElseThrow();
    }

    @Override
    public EventDay getNextDay(Player player, int count) {
        return eventDayRepository.findById(player.getCurrentDay().getId() + count).orElse(null);
    }

    @Override
    public EventDay getLastStepDay(Player player, int count) {
        return eventDayRepository.findById(lastId - player.getCurrentDay().getId() + count).orElseThrow();
    }
}
