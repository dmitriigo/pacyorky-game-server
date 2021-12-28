package ee.pacyorky.gameserver.gameserver.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import ee.pacyorky.gameserver.gameserver.dtos.EventDayDTO;
import ee.pacyorky.gameserver.gameserver.dtos.PlayerDTO;
import ee.pacyorky.gameserver.gameserver.entities.game.CardType;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.entities.game.Season;

@Mapper
public interface PlayerMapper {
    
    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);
    
    @Mapping(target = "currentDay", source = "player", qualifiedByName = "currentDayMapper")
    PlayerDTO toDto(Player player);
    
    @Named(value = "currentDayMapper")
    default EventDayDTO map(Player player) {
        if (player == null) {
            return null;
        }
        var dto = EventDayMapper.INSTANCE.toDto(player.getCurrentDay());
        if (dto == null) {
            return null;
        }
        if (dto.isHoliday() && player.getHolidayCard() != null) {
            dto.setName(player.getHolidayCard().getName());
        }
        return dto;
    }
    
    default Long map(CardType cardType) {
        return cardType.getId();
    }
    
    default Long map(Season season) {
        return season.getId();
    }
    
}
