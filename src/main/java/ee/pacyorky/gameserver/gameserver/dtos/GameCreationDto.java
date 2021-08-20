package ee.pacyorky.gameserver.gameserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameCreationDto {

    private Long capacity;

    private boolean withComputer;

    private boolean privateRoom;

    private String password;

    private String name;

    private Long secondsBeforeStart;

    private Long secondsForStep;

}
