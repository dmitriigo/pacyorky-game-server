package ee.pacyorky.gameserver.gameserver.entities.game;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.Hibernate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Card card = (Card) o;
        return id != null && Objects.equals(id, card.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
