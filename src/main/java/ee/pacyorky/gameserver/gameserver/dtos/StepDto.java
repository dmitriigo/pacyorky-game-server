package ee.pacyorky.gameserver.gameserver.dtos;

import ee.pacyorky.gameserver.gameserver.entities.game.StepStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StepDto {

    private PlayerDTO currentPlayer;

    private Integer counter;

    private StepStatus status;

    private List<StepCardDto> stepCards;
}
