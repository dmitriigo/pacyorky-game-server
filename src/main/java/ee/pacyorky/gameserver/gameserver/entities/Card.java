package ee.pacyorky.gameserver.gameserver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    private Long id;

    private String name;

    private Long cardType;

    private Long cardsInDeck;

    public void setCardType(CardType cardType) {
        this.cardType = cardType.getId();
    }

    public CardType getCardType() {
        return CardType.getById(this.cardType);
    }

}
