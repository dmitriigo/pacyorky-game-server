package ee.pacyorky.gameserver.gameserver.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlayerDTO {
    
    private String id;
    
    private Integer happiness;
    
    private CharacterDTO character;
    
    private List<CardDTO> deck = new ArrayList<>();
    
    private EventDayDTO currentDay;
    
    private boolean isLastStep;
    
    private boolean voted;
    
    private boolean isComputer;
    
}
