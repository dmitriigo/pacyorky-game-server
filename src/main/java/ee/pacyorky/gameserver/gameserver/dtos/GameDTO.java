package ee.pacyorky.gameserver.gameserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GameDTO {

    private Long id;

    private Set<PlayerDTO> players;

    private Long capacity;

    private LocalDateTime startAt;

    private LocalDateTime nextStepAt;

    private boolean withComputer;

    private boolean privateRoom;

    private String password;

    private String name;

    private StepDto step;

    private String status;

    private long stepCounter;

}
