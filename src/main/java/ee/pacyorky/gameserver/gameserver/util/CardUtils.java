package ee.pacyorky.gameserver.gameserver.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;

import ee.pacyorky.gameserver.gameserver.entities.game.Card;
import ee.pacyorky.gameserver.gameserver.entities.game.CardType;
import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;

public final class CardUtils {
    
    public static final List<String> PRIZE_DAYS = List.of("yangel", "urodini");
    
    private static final Random RANDOM = new Random();
    
    private CardUtils() {
    }
    
    public static void initPlayersCards(Player player, Game game) {
        Map<CardType, List<Card>> cardTypeListMap = game.getAllDecks();
        for (Map.Entry<CardType, List<Card>> cardTypeListEntry : cardTypeListMap.entrySet()) {
            List<Card> playersCards = player.getCardsByType(cardTypeListEntry.getKey());
            for (int i = playersCards.size(); i < 2; i++) {
                var list = new ArrayList<>(cardTypeListEntry.getValue());
                Collections.shuffle(list);
                Card card = list.stream().findFirst().orElseThrow();
                player.getDeck().add(card);
            }
        }
    }
    
    public static int[] getRandomCardIndexes(int deckSize) {
        return RANDOM.ints(RandomUtils.nextInt(1, deckSize + 1), 0, deckSize).distinct().toArray();
    }
}
