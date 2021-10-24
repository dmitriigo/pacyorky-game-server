package ee.pacyorky.gameserver.gameserver.util;

import ee.pacyorky.gameserver.gameserver.entities.game.Card;
import ee.pacyorky.gameserver.gameserver.entities.game.CardType;
import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import org.apache.commons.lang3.RandomUtils;

import java.util.*;

public final class CardUtils {
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
        return new Random().ints(RandomUtils.nextInt(1, deckSize + 1), 0, deckSize).distinct().toArray();
    }
}
