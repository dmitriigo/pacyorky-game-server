package ee.pacyorky.gameserver.gameserver.dtos;

import ee.pacyorky.gameserver.gameserver.entities.game.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GameDTO {

    private Long id;

    private Set<PlayerDTO> players;

    private Long capacity;

    private String startAt;

    private String nextStepAt;

    private boolean withComputer;

    private boolean privateRoom;

    private String password;

    private String name;

    private StepDto step;

    private long stepCounter;

    private Status status;

}
