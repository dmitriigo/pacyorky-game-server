package ee.pacyorky.gameserver.gameserver.entities;

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

    @OneToOne
    private Player currentPlayer;

    private Long capacity;

    private LocalDateTime startAt;

    private LocalDateTime nextStepAt;

    private boolean started;

    private boolean withComputer;

    private boolean privateRoom;

    private String password;

    private String name;

    private Integer counter;

    public void addPlayer(Player player) {
        if (players == null) {
            players = new HashSet<>();
        }
        players.add(player);
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
}
