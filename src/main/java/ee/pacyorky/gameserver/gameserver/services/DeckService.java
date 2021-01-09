package ee.pacyorky.gameserver.gameserver.services;

import ee.pacyorky.gameserver.gameserver.entities.Card;
import ee.pacyorky.gameserver.gameserver.entities.Character;

import java.util.List;

public interface DeckService {

    List<Card> getDishesDeck();

    List<Card> getHolidaysDeck();

    List<Card> getRitualsDeck();

    List<Card> getStuffDeck();

    List<Character> getCharacterDeck();

}
