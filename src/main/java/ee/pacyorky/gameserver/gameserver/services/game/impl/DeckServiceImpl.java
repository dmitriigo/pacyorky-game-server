package ee.pacyorky.gameserver.gameserver.services.game.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import ee.pacyorky.gameserver.gameserver.entities.game.Card;
import ee.pacyorky.gameserver.gameserver.entities.game.CardType;
import ee.pacyorky.gameserver.gameserver.entities.game.Character;
import ee.pacyorky.gameserver.gameserver.entities.game.HolidayCard;
import ee.pacyorky.gameserver.gameserver.repositories.CardRepository;
import ee.pacyorky.gameserver.gameserver.repositories.CharacterRepository;
import ee.pacyorky.gameserver.gameserver.repositories.HolidayCardRepository;
import ee.pacyorky.gameserver.gameserver.services.game.DeckService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DeckServiceImpl implements DeckService {
    
    private final CardRepository cardRepository;
    
    private final CharacterRepository characterRepository;
    
    private final HolidayCardRepository holidayCardRepository;
    
    @Override
    public List<Card> getDishesDeck() {
        return initShuffleAndReturn(getDishesFromRepo());
    }
    
    @Override
    public List<HolidayCard> getHolidaysDeck() {
        return initShuffleAndReturnHolidayCard(getHolidaysFromRepo());
    }
    
    @Override
    public List<Card> getRitualsDeck() {
        return initShuffleAndReturn(getRitualsFromRepo());
    }
    
    @Override
    public List<Card> getStuffDeck() {
        return initShuffleAndReturn(getStuffsFromRepo());
    }
    
    @Override
    public List<Character> getCharacterDeck() {
        List<Character> characters = characterRepository.findAll();
        Collections.shuffle(characters);
        return characters;
    }
    
    private List<Card> initShuffleAndReturn(List<Card> cardsInRepo) {
        List<Card> targetList = new ArrayList<>();
        
        for (Card card : cardsInRepo) {
            for (int i = 0; i < card.getCardsInDeck(); i++) {
                targetList.add(card);
            }
        }
        Collections.shuffle(targetList);
        return targetList;
    }
    
    private List<HolidayCard> initShuffleAndReturnHolidayCard(List<HolidayCard> cardsInRepo) {
        List<HolidayCard> targetList = new ArrayList<>();
        
        for (HolidayCard card : cardsInRepo) {
            for (int i = 0; i < card.getCardsInDeck(); i++) {
                targetList.add(card);
            }
        }
        Collections.shuffle(targetList);
        return targetList;
    }
    
    private List<Card> getDishesFromRepo() {
        return cardRepository.getAllByCardType(CardType.DISHES.getId());
    }
    
    private List<HolidayCard> getHolidaysFromRepo() {
        return holidayCardRepository.findAll();
    }
    
    private List<Card> getRitualsFromRepo() {
        return cardRepository.getAllByCardType(CardType.RITUALS.getId());
    }
    
    private List<Card> getStuffsFromRepo() {
        return cardRepository.getAllByCardType(CardType.STUFF.getId());
    }
}
