package ee.pacyorky.gameserver.gameserver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Integer happiness;

    @ManyToOne
    private Character character;

    @ManyToMany
    @Builder.Default
    private List<Card> deck = new ArrayList<>();

    public List<Card> getCardsByType(CardType cardType) {
        return deck.stream().filter(card -> card.getCardType() == cardType).collect(Collectors.toList());
    }

}
