package ee.pacyorky.gameserver.gameserver.services.game.impl;

import org.springframework.stereotype.Service;

import ee.pacyorky.gameserver.gameserver.entities.game.EventDay;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.repositories.EventDayRepository;
import ee.pacyorky.gameserver.gameserver.services.game.EventDayService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EventDayServiceImpl implements EventDayService {
    
    private final EventDayRepository eventDayRepository;
    
    private static final Long START_ID = 1L;
    
    private static final Long LAST_ID = 48L;
    
    @Override
    public EventDay getStartPosition() {
        return eventDayRepository.findById(START_ID).orElseThrow();
    }
    
    @Override
    public EventDay getNextDay(Player player, int count) {
        return eventDayRepository.findById(player.getCurrentDay().getId() + count).orElse(null);
    }
    
    @Override
    public EventDay getLastStepDay(Player player, int count) {
        // 48 - 43 + 6 = 11 wrong
        var newId = count - (LAST_ID - player.getCurrentDay().getId());
        if (newId == 0) {
            newId = 1;
        }
        return eventDayRepository.findById(newId).orElseThrow(() -> new RuntimeException("Id not found"));
    }
}
