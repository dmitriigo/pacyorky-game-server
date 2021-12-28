package ee.pacyorky.gameserver.gameserver.repositories;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import ee.pacyorky.gameserver.gameserver.entities.game.EventDay;
import ee.pacyorky.gameserver.gameserver.entities.game.Season;

@SpringBootTest
@ActiveProfiles("test")
class EventDayRepositoryTest {
    
    @Autowired
    private EventDayRepository eventDayRepository;
    
    @Test
    void eventDayRepositoryTest() {
        List<EventDay> eventDays = eventDayRepository.findAll();
        Assert.notEmpty(eventDays, "event days is empty");
        List<EventDay> simpleDaysByHoliday = eventDays.stream().filter(day -> !day.isHoliday()).collect(Collectors.toList());
        List<EventDay> simpleDaysByName = eventDays.stream().filter(day -> day.getName().equals("day")).collect(Collectors.toList());
        simpleDaysByHoliday.sort(Comparator.comparingLong(EventDay::getDeskOrder));
        simpleDaysByHoliday.sort(Comparator.comparingLong(EventDay::getDeskOrder));
        Assertions.assertEquals(simpleDaysByName, simpleDaysByHoliday);
        Assertions.assertEquals(18, simpleDaysByHoliday.size());
        Assertions.assertEquals(48, eventDays.size());
        Assertions.assertEquals(15, eventDays.stream().filter(day -> day.getSeason() == Season.WINTER).count());
        Assertions.assertEquals(12, eventDays.stream().filter(day -> day.getSeason() == Season.SPRING).count());
        Assertions.assertEquals(10, eventDays.stream().filter(day -> day.getSeason() == Season.SUMMER).count());
        Assertions.assertEquals(11, eventDays.stream().filter(day -> day.getSeason() == Season.AUTUMN).count());
    }
}
