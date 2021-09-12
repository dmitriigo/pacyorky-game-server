package ee.pacyorky.gameserver.gameserver.dtos;

import ee.pacyorky.gameserver.gameserver.entities.game.StepStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StepDto {

    private PlayerDTO currentPlayer;

    private Integer counter;

    private StepStatus status;
}
