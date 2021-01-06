package ee.pacyorky.gameserver.gameserver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    private List<Card> DishesDeck;

    @ManyToMany
    private List<Card> HolidaysDeck;

    @ManyToMany
    private List<Card> RitualsDeck;

    @ManyToMany
    private List<Card> StuffDeck;

    @OneToMany
    private List<Player> players;

    @ManyToMany
    private List<EventDay> calendar;

}
