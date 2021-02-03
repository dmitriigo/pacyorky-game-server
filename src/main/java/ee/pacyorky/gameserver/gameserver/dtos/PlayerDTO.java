package ee.pacyorky.gameserver.gameserver.dtos;

import ee.pacyorky.gameserver.gameserver.entities.Character;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlayerDTO {

    private String id;

    private Integer happiness;

    private Character character;

    private List<CardDTO> deck = new ArrayList<>();

    private EventDayDTO currentDay;

}
