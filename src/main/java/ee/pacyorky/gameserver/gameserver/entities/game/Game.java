package ee.pacyorky.gameserver.gameserver.entities.game;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
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
    private List<Card> dishesDeck;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter(AccessLevel.NONE)
    private List<HolidayCard> holidaysDeck;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter(AccessLevel.NONE)
    private List<Card> ritualsDeck;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter(AccessLevel.NONE)
    private List<Card> stuffDeck;

    @OneToMany(fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE)
    private Set<Player> players;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter(AccessLevel.NONE)
    private List<EventDay> calendar;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter(AccessLevel.NONE)
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
        Map<CardType, List<Card>> result = new HashMap<>();
        result.put(CardType.DISHES, dishesDeck);
        result.put(CardType.RITUALS, ritualsDeck);
        result.put(CardType.STUFF, stuffDeck);
        return result;
    }

    public HolidayCard getHolidayCard() {
        Collections.shuffle(holidaysDeck);
        var holiday = holidaysDeck.stream().findAny().orElseThrow();
        holidaysDeck.remove(holiday);
        return holiday;
    }

    public void start() {
        this.status = Status.STARTED.getId();
    }

    public void finish(Status reason) {
        this.status = reason.getId();
        this.step = null;
        this.players = new HashSet<>();
    }
}
