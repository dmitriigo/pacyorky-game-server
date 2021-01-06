package ee.pacyorky.gameserver.gameserver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long capacity;

    @OneToOne
    private Game game;

    @OneToMany
    private List<Player> players;

    private Long timeToStart;

    private boolean withComputer;

    private boolean privateRoom;

    private String password;

}
