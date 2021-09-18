package ee.pacyorky.gameserver.gameserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StepCardDto {
    private CardDTO card;
    private long vote;
}
