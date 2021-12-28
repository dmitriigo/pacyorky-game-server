package ee.pacyorky.gameserver.gameserver.entities.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.AccessLevel;
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
public class Game {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private Long id;
    
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private List<Card> dishesDeck;
    
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private List<HolidayCard> holidaysDeck;
    
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private List<Card> ritualsDeck;
    
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private List<Card> stuffDeck;
    
    @OneToMany(fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE)
    private Set<Player> players;
    
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private List<EventDay> calendar;
    
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private List<Character> characters;
    
    private Long capacity;
    
    private LocalDateTime startAt;
    
    private LocalDateTime nextStepAt;
    
    private boolean withComputer;
    
    private boolean privateRoom;
    
    private String password;
    
    private String name;
    
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Long status;
    
    private Long secondsBeforeStart;
    
    private Long secondsForStep;
    
    @Setter(AccessLevel.NONE)
    private long stepCounter;
    
    @OneToOne(cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Step step;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter(AccessLevel.NONE)
    @Builder.Default
    private Set<Step> history = new HashSet<>();
    
    @OneToOne(fetch = FetchType.EAGER)
    private Player owner;
    
    public void addPlayer(Player player) {
        if (players == null) {
            players = new HashSet<>();
        }
        players.add(player);
    }
    
    public Status getStatus() {
        return Status.getById(this.status);
    }
    
    public void setStep(Step step) {
        this.step = step;
        if (this.history == null) {
            this.history = new HashSet<>();
        }
        this.history.add(step);
    }
    
    public boolean isNotStarted() {
        return !Objects.equals(this.status, Status.STARTED.getId());
    }
    
    public boolean isNotWaiting() {
        return !Objects.equals(this.status, Status.WAITING.getId());
    }
    
    public boolean isNotFinished() {
        return Objects.equals(this.status, Status.WAITING.getId()) || Objects.equals(this.status, Status.STARTED.getId());
    }
    
    public void plusStep() {
        this.stepCounter++;
    }
    
    public void removePlayer(String playerId) {
        this.players = players.stream().filter(player -> !player.getId().equals(playerId)).collect(Collectors.toSet());
    }
    
    public Map<CardType, List<Card>> getAllDecks() {
        Map<CardType, List<Card>> result = new EnumMap<>(CardType.class);
        result.put(CardType.DISHES, dishesDeck);
        result.put(CardType.RITUALS, ritualsDeck);
        result.put(CardType.STUFF, stuffDeck);
        return result;
    }
    
    public Card getRandomCard(CardType cardType) {
        var list = new ArrayList<>(getAllDecks().get(cardType));
        Collections.shuffle(list);
        return list.stream().findAny().orElseThrow();
    }
    
    public HolidayCard getHolidayCard() {
        Collections.shuffle(holidaysDeck);
        return holidaysDeck.stream().findAny().orElseThrow();
    }
    
    public void start() {
        this.status = Status.STARTED.getId();
    }
    
    public void finish(Status reason) {
        this.status = reason.getId();
        this.step = null;
        this.owner = null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Game game = (Game) o;
        return id != null && Objects.equals(id, game.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
