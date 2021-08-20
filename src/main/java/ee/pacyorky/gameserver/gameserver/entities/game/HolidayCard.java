package ee.pacyorky.gameserver.gameserver.entities.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HolidayCard {

    @Id
    private Long id;

    private String name;

    private Long cardsInDeck;

}

