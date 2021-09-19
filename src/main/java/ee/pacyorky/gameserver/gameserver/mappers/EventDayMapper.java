package ee.pacyorky.gameserver.gameserver.mappers;

import ee.pacyorky.gameserver.gameserver.dtos.EventDayDTO;
import ee.pacyorky.gameserver.gameserver.entities.game.EventDay;
import ee.pacyorky.gameserver.gameserver.entities.game.Season;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface EventDayMapper {

    @Mapping(target = "name", source = "eventDay", qualifiedByName = "eventDayName")
    EventDayDTO toDto(EventDay eventDay);


    @Named(value = "eventDayName")
    default String map(EventDay eventDay) {
        if (eventDay.isHoliday() && eventDay.getHolidayCard() == null) {
            throw new GlobalException("Day is holiday, but holiday card is null", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        return eventDay.isHoliday() ? eventDay.getHolidayCard().getName() : eventDay.getName();
    }

    default Long map(Season season) {
        return season.getId();
    }
}
