package ee.pacyorky.gameserver.gameserver.services.game;

import ee.pacyorky.gameserver.gameserver.entities.game.Card;
import ee.pacyorky.gameserver.gameserver.entities.game.Character;
import ee.pacyorky.gameserver.gameserver.entities.game.HolidayCard;

import java.util.List;

public interface DeckService {

    List<Card> getDishesDeck();

    List<HolidayCard> getHolidaysDeck();

    List<Card> getRitualsDeck();

    List<Card> getStuffDeck();

    List<Character> getCharacterDeck();

}
