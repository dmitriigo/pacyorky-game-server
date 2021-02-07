package ee.pacyorky.gameserver.gameserver.repositories;

import ee.pacyorky.gameserver.gameserver.entities.HolidayCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class HolidayCardRepositoryTest {

    @Autowired
    private HolidayCardRepository holidayCardRepository;

    @Test
    public void cardsLoadTest() {
        List<HolidayCard> holidayCards = holidayCardRepository.findAll();
        List<HolidayCard> holidays = new ArrayList<>();
        for (HolidayCard holidayCard : holidayCards) {
            for (int i = 0; i < holidayCard.getCardsInDeck(); i++) {
                holidays.add(holidayCard);
            }
        }
        Assert.notEmpty(holidays, "Holidays is empty!");
        Assertions.assertEquals(holidays.size(), 60);
    }
}
