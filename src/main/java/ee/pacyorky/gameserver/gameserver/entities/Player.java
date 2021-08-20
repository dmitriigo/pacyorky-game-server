package ee.pacyorky.gameserver.gameserver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
    private Integer happiness = 0;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    private Character character;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Builder.Default
    private List<Card> deck = new ArrayList<>();

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    private EventDay currentDay;

    private boolean stepFinished;

    private boolean isLastStep;

    public List<Card> getCardsByType(CardType cardType) {
        return deck.stream().filter(card -> card.getCardType() == cardType).collect(Collectors.toList());
    }

    public void removeCards(List<Long> cardIds) {
        this.deck = this.deck.stream().filter(card -> !cardIds.contains(card.getId())).collect(Collectors.toList());
    }

}
