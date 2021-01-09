package ee.pacyorky.gameserver.gameserver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Card> dishesDeck;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Card> holidaysDeck;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Card> ritualsDeck;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Card> stuffDeck;

    @OneToMany
    private List<Player> players;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<EventDay> calendar;

}
