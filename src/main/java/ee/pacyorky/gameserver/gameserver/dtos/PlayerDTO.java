package ee.pacyorky.gameserver.gameserver.dtos;

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

    private CharacterDTO character;

    private List<CardDTO> deck = new ArrayList<>();

    private EventDayDTO currentDay;

    //TODO REMOVE IT ON RELEASE
    private boolean stepFinished;

    private boolean isLastStep;

    private boolean voted;

    private boolean isComputer;

}
