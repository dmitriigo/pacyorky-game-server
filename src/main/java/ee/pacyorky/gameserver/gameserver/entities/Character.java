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
public class Character {

    @Id
    private Long id;

    private String name;

    @ManyToMany
    private List<EventDay> favoriteEvent;

    @ManyToMany
    private List<Card> favoriteCards;

    @ManyToMany
    private List<Card> unlovedCards;

}
