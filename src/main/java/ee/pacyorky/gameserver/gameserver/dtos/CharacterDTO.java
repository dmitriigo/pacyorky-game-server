package ee.pacyorky.gameserver.gameserver.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CharacterDTO {
    
    private Long id;
    
    private String name;
    
    private List<EventDayDTO> favoriteEventDays;
    
    private List<CardDTO> favoriteEventCards;
    
    private List<CardDTO> favoriteCards;
    
    private List<CardDTO> unlovedCards;
    
}
