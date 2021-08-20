package ee.pacyorky.gameserver.gameserver.mappers;

import ee.pacyorky.gameserver.gameserver.dtos.HolidayCardDTO;
import ee.pacyorky.gameserver.gameserver.entities.game.HolidayCard;
import org.mapstruct.Mapper;

@Mapper
public interface HolidayCardMapper {

    HolidayCardDTO toDto(HolidayCard holidayCard);
}
