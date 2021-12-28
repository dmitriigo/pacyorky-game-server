package ee.pacyorky.gameserver.gameserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventDayDTO {
    
    private Long id;
    
    private boolean holiday;
    
    private Long season;
    
    private String name;
    
    private Long deskOrder;
}
