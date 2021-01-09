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
public class Character {

    @Id
    private Long id;

    private String name;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<EventDay> favoriteEventDays;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Card> favoriteEventCards;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Card> favoriteCards;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Card> unlovedCards;

}
