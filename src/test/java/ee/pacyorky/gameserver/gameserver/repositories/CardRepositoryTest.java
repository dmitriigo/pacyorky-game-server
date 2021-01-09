package ee.pacyorky.gameserver.gameserver.repositories;

import ee.pacyorky.gameserver.gameserver.entities.Card;
import ee.pacyorky.gameserver.gameserver.entities.CardType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Test
    public void cardsLoadTest() {
        List<Card> cards = cardRepository.findAll();
        List<Card> dishes = new ArrayList<>();
        List<Card> holidays = new ArrayList<>();
        List<Card> rituals = new ArrayList<>();
        List<Card> stuff = new ArrayList<>();

        for (Card card : cards) {
            List<Card> tempList = new ArrayList<>();
            for (int i = 0; i < card.getCardsInDeck(); i++) {
                tempList.add(card);
            }
            if (card.getCardType() == CardType.DISHES) {
                dishes.addAll(tempList);
            }
            if (card.getCardType() == CardType.HOLIDAY) {
                holidays.addAll(tempList);
            }
            if (card.getCardType() == CardType.RITUALS) {
                rituals.addAll(tempList);
            }
            if (card.getCardType() == CardType.STUFF) {
                stuff.addAll(tempList);
            }
        }
        Assert.notEmpty(dishes, "Dishes is empty!");
        Assert.notEmpty(holidays, "Holidays is empty!");
        Assert.notEmpty(rituals, "Rituals is empty!");
        Assert.notEmpty(stuff, "Stuff is empty!");
        Assertions.assertEquals(dishes.size(), 60);
        Assertions.assertEquals(holidays.size(), 60);
        Assertions.assertEquals(rituals.size(), 60);
        Assertions.assertEquals(stuff.size(), 60);
        Assertions.assertEquals(dishes.size() + holidays.size() + rituals.size() + stuff.size(), 240);
    }
}
