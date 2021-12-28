package ee.pacyorky.gameserver.gameserver.repositories;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import ee.pacyorky.gameserver.gameserver.entities.game.Character;

@SpringBootTest
@ActiveProfiles("test")
class CharacterRepositoryTest {
    
    @Autowired
    private CharacterRepository characterRepository;
    
    @Test
    void characterRepositoryTest() {
        List<Character> characters = characterRepository.findAll();
        Assert.notEmpty(characters, "Characters is empty");
        Assertions.assertEquals(8, characters.size());
    }
    
    @Test
    void loadFavoriteEventDaysTest() {
        List<Character> characters = characterRepository.findAll();
        Assert.notEmpty(characters, "Characters is empty");
        Assertions.assertEquals(8, characters.size());
        
        for (Character character : characters) {
            Assert.notEmpty(character.getFavoriteEventDays(), "Favorite events days is empty");
        }
    }
    
    @Test
    void loadFavoriteEventCardsTest() {
        List<Character> characters = characterRepository.findAll();
        Assert.notEmpty(characters, "Characters is empty");
        Assertions.assertEquals(8, characters.size());
        
        for (Character character : characters) {
            if (character.getId().equals(4L) || character.getId().equals(5L)) {
                Assertions.assertTrue(character.getFavoriteHolidaysCards().isEmpty());
                continue;
            }
            Assert.notEmpty(character.getFavoriteHolidaysCards(), "Favorite events cards is empty");
        }
    }
    
    @Test
    void loadFavoriteCardsTest() {
        List<Character> characters = characterRepository.findAll();
        Assert.notEmpty(characters, "Characters is empty");
        Assertions.assertEquals(8, characters.size());
        
        for (Character character : characters) {
            Assert.notEmpty(character.getFavoriteCards(), "Favorite cards is empty");
        }
    }
    
    @Test
    void loadUnlovedCardsTest() {
        List<Character> characters = characterRepository.findAll();
        Assert.notEmpty(characters, "Characters is empty");
        Assertions.assertEquals(8, characters.size());
        
        for (Character character : characters) {
            Assert.notEmpty(character.getUnlovedCards(), "Unloved cards is empty");
        }
    }
    
}
