package ee.pacyorky.gameserver.gameserver.mappers;

import org.mapstruct.Mapper;

import ee.pacyorky.gameserver.gameserver.dtos.HolidayCardDTO;
import ee.pacyorky.gameserver.gameserver.entities.game.HolidayCard;

@Mapper
public interface HolidayCardMapper {
    
    HolidayCardDTO toDto(HolidayCard holidayCard);
}
