package ee.pacyorky.gameserver.gameserver.entities;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private List<Card> holidaysDeck;

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

    private Long capacity;

    private LocalDateTime startAt;

    private boolean withComputer;

    private boolean privateRoom;

    private String password;

    private String name;

    public void addPlayer(Player player) {
        if (players == null) {
            players = new HashSet<>();
        }
        players.add(player);
    }

    public void removePlayer(String playerId) {
        this.players = players.stream().filter(player -> !player.getId().equals(playerId)).collect(Collectors.toSet());
    }
}
