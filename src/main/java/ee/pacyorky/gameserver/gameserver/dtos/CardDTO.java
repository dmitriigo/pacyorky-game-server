package ee.pacyorky.gameserver.gameserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardDTO {
    
    private Long id;
    
    private String name;
    
    private Long cardType;
}
