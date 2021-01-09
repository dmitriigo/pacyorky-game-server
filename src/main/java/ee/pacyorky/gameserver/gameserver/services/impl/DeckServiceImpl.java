package ee.pacyorky.gameserver.gameserver.services.impl;

import ee.pacyorky.gameserver.gameserver.entities.Card;
import ee.pacyorky.gameserver.gameserver.entities.CardType;
import ee.pacyorky.gameserver.gameserver.entities.Character;
import ee.pacyorky.gameserver.gameserver.repositories.CardRepository;
import ee.pacyorky.gameserver.gameserver.repositories.CharacterRepository;
import ee.pacyorky.gameserver.gameserver.services.DeckService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class DeckServiceImpl implements DeckService {

    private final CardRepository cardRepository;

    private final CharacterRepository characterRepository;

    @Override
    public List<Card> getDishesDeck() {
         return initShuffleAndReturn(getDishesFromRepo());
    }

    @Override
    public List<Card> getHolidaysDeck() {
        return initShuffleAndReturn(getHolidaysFromRepo());
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
        return characterRepository.findAll();
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

    private List<Card> getDishesFromRepo() {
        return cardRepository.getAllByCardType(CardType.DISHES.getId());
    }

    private List<Card> getHolidaysFromRepo() {
        return cardRepository.getAllByCardType(CardType.HOLIDAY.getId());
    }

    private List<Card> getRitualsFromRepo() {
        return cardRepository.getAllByCardType(CardType.RITUALS.getId());
    }

    private List<Card> getStuffsFromRepo() {
        return cardRepository.getAllByCardType(CardType.STUFF.getId());
    }
}
