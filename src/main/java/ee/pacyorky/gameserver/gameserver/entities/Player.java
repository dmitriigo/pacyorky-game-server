package ee.pacyorky.gameserver.gameserver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Character character;

    @ManyToMany
    @Builder.Default
    private List<Card> deck = new ArrayList<>();

    @ManyToOne
    private EventDay currentDay;

    public List<Card> getCardsByType(CardType cardType) {
        return deck.stream().filter(card -> card.getCardType() == cardType).collect(Collectors.toList());
    }

}
