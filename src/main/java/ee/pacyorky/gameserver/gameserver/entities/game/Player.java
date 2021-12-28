package ee.pacyorky.gameserver.gameserver.entities.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import ee.pacyorky.gameserver.gameserver.entities.security.BanInfo;
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
    @ToString.Exclude
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
    
    @Column(length = 4096)
    private String voiceToken;
    
    private Long agoraId;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private BanInfo banInfo = new BanInfo();
    
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
        setVoiceToken(null);
        setAgoraId(null);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Player player = (Player) o;
        return id != null && Objects.equals(id, player.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
