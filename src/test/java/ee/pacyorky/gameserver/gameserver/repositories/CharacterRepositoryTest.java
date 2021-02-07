package ee.pacyorky.gameserver.gameserver.repositories;

import ee.pacyorky.gameserver.gameserver.entities.Character;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
public class CharacterRepositoryTest {

    @Autowired
    private CharacterRepository characterRepository;

    @Test
    public void characterRepositoryTest() {
        List<Character> characters = characterRepository.findAll();
        Assert.notEmpty(characters, "Characters is empty");
        Assertions.assertEquals(characters.size(), 8);
    }

    @Test
    public void loadFavoriteEventDaysTest() {
        List<Character> characters = characterRepository.findAll();
        Assert.notEmpty(characters, "Characters is empty");
        Assertions.assertEquals(characters.size(), 8);

        for (Character character : characters) {
            Assert.notEmpty(character.getFavoriteEventDays(), "Favorite events days is empty");
        }
    }

    @Test
    public void loadFavoriteEventCardsTest() {
        List<Character> characters = characterRepository.findAll();
        Assert.notEmpty(characters, "Characters is empty");
        Assertions.assertEquals(characters.size(), 8);

        for (Character character : characters) {
            if (character.getId().equals(4L) || character.getId().equals(5L)) {
                Assertions.assertTrue(character.getFavoriteHolidaysCards().isEmpty());
                continue;
            }
            Assert.notEmpty(character.getFavoriteHolidaysCards(), "Favorite events cards is empty");
        }
    }

    @Test
    public void loadFavoriteCardsTest() {
        List<Character> characters = characterRepository.findAll();
        Assert.notEmpty(characters, "Characters is empty");
        Assertions.assertEquals(characters.size(), 8);

        for (Character character : characters) {
            Assert.notEmpty(character.getFavoriteCards(), "Favorite cards is empty");
        }
    }

    @Test
    public void loadUnlovedCardsTest() {
        List<Character> characters = characterRepository.findAll();
        Assert.notEmpty(characters, "Characters is empty");
        Assertions.assertEquals(characters.size(), 8);

        for (Character character : characters) {
            Assert.notEmpty(character.getUnlovedCards(), "Unloved cards is empty");
        }
    }

}
