package ee.pacyorky.gameserver.gameserver.entities.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {
    @Id
    private String id;

    @Builder.Default
    private Long happiness = 0L;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    private Character character;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Builder.Default
    private List<Card> deck = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    private EventDay currentDay;

    @ManyToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    private HolidayCard holidayCard;

    private boolean stepFinished;

    private boolean isLastStep;

    private boolean voted;

    private boolean isComputer;

    public List<Card> getCardsByType(CardType cardType) {
        return deck.stream().filter(card -> card.getCardType() == cardType).collect(Collectors.toList());
    }

    public void removeCards(List<Long> cardIds) {
        this.deck = this.deck.stream().filter(card -> !cardIds.contains(card.getId())).collect(Collectors.toList());
    }

    public void resetPlayer() {
        setHappiness(0L);
        setStepFinished(false);
        setVoted(false);
        setCharacter(null);
        setCurrentDay(null);
        setHolidayCard(null);
        setDeck(new ArrayList<>());
        setLastStep(false);
    }

}
