package ee.pacyorky.gameserver.gameserver.mappers;

import ee.pacyorky.gameserver.gameserver.dtos.EventDayDTO;
import ee.pacyorky.gameserver.gameserver.entities.EventDay;
import ee.pacyorky.gameserver.gameserver.entities.Season;
import org.mapstruct.Mapper;

@Mapper
public interface EventDayMapper {

    EventDayDTO toDto(EventDay eventDay);

    default Long map(Season season) {
        return season.getId();
    }
}
