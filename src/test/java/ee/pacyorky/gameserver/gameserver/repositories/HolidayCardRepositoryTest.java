package ee.pacyorky.gameserver.gameserver.repositories;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import ee.pacyorky.gameserver.gameserver.entities.game.HolidayCard;

@SpringBootTest
@ActiveProfiles("test")
class HolidayCardRepositoryTest {
    
    @Autowired
    private HolidayCardRepository holidayCardRepository;
    
    @Test
    void cardsLoadTest() {
        List<HolidayCard> holidayCards = holidayCardRepository.findAll();
        List<HolidayCard> holidays = new ArrayList<>();
        for (HolidayCard holidayCard : holidayCards) {
            for (int i = 0; i < holidayCard.getCardsInDeck(); i++) {
                holidays.add(holidayCard);
            }
        }
        Assert.notEmpty(holidays, "Holidays is empty!");
        Assertions.assertEquals(60, holidays.size());
    }
}
