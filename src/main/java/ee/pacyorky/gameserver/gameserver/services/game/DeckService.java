package ee.pacyorky.gameserver.gameserver.services.game;

import java.util.List;

import ee.pacyorky.gameserver.gameserver.entities.game.Card;
import ee.pacyorky.gameserver.gameserver.entities.game.Character;
import ee.pacyorky.gameserver.gameserver.entities.game.HolidayCard;

public interface DeckService {
    
    List<Card> getDishesDeck();
    
    List<HolidayCard> getHolidaysDeck();
    
    List<Card> getRitualsDeck();
    
    List<Card> getStuffDeck();
    
    List<Character> getCharacterDeck();
    
}
