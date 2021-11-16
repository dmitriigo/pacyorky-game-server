package ee.pacyorky.gameserver.gameserver.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ee.pacyorky.gameserver.gameserver.dtos.EventDayDTO;
import ee.pacyorky.gameserver.gameserver.entities.game.EventDay;
import ee.pacyorky.gameserver.gameserver.entities.game.Season;

@Mapper
public interface EventDayMapper {

    EventDayMapper INSTANCE = Mappers.getMapper(EventDayMapper.class);

    EventDayDTO toDto(EventDay eventDay);

    default Long map(Season season) {
        return season.getId();
    }
}
