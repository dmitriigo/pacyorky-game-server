package ee.pacyorky.gameserver.gameserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
