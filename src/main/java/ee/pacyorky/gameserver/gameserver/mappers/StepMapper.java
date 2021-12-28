package ee.pacyorky.gameserver.gameserver.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ee.pacyorky.gameserver.gameserver.dtos.StepDto;
import ee.pacyorky.gameserver.gameserver.entities.game.CardType;
import ee.pacyorky.gameserver.gameserver.entities.game.Season;
import ee.pacyorky.gameserver.gameserver.entities.game.Step;
import ee.pacyorky.gameserver.gameserver.entities.game.StepStatus;

@Mapper
public interface StepMapper {
    StepMapper INSTANCE = Mappers.getMapper(StepMapper.class);
    
    StepDto toDto(Step step);
    
    default String map(StepStatus status) {
        return status.name();
    }
    
    default Long map(Season season) {
        return season.getId();
    }
    
    default Long map(CardType cardType) {
        return cardType.getId();
    }
}
