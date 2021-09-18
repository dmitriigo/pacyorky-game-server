package ee.pacyorky.gameserver.gameserver.repositories;

import ee.pacyorky.gameserver.gameserver.entities.game.Card;
import ee.pacyorky.gameserver.gameserver.entities.game.CardType;
import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void cardsLoadTest() {
        List<Card> cards = cardRepository.findAll();
        List<Card> dishes = new ArrayList<>();
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
            if (card.getCardType() == CardType.RITUALS) {
                rituals.addAll(tempList);
            }
            if (card.getCardType() == CardType.STUFF) {
                stuff.addAll(tempList);
            }
        }
        Assert.notEmpty(dishes, "Dishes is empty!");
        Assert.notEmpty(rituals, "Rituals is empty!");
        Assert.notEmpty(stuff, "Stuff is empty!");
        Assertions.assertEquals(dishes.size(), 60);
        Assertions.assertEquals(rituals.size(), 60);
        Assertions.assertEquals(stuff.size(), 60);
        Assertions.assertEquals(dishes.size() + rituals.size() + stuff.size(), 180);
    }

    @DisplayName("Game should be found by player id")
    @Test
    @Transactional
    public void findGameByPlayerId() {
        //given

        var player1 = Player.builder().id(UUID.randomUUID().toString()).build();
        entityManager.persist(player1);
        var player2 = Player.builder().id(UUID.randomUUID().toString()).build();
        entityManager.persist(player2);

        var firstGame = Game.builder()
                .name("firstGame")
                .players(Set.of(player1, player2))
                .build();
        entityManager.persist(firstGame);

        //when
        var foundGame = gameRepository.getGameByPlayersContains(player1);

        //then
        assertThat(foundGame)
                .isPresent()
                .get()
                .isEqualTo(firstGame);
    }
}
