package ee.pacyorky.gameserver.gameserver.mappers;

import ee.pacyorky.gameserver.gameserver.dtos.StepDto;
import ee.pacyorky.gameserver.gameserver.entities.CardType;
import ee.pacyorky.gameserver.gameserver.entities.Season;
import ee.pacyorky.gameserver.gameserver.entities.Status;
import ee.pacyorky.gameserver.gameserver.entities.Step;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StepMapper {
    StepMapper INSTANCE = Mappers.getMapper(StepMapper.class);

    StepDto toDto(Step step);

    default String map(Status status) {
        return status.name();
    }

    default Long map(Season season) {
        return season.getId();
    }

    default Long map(CardType cardType) {
        return cardType.getId();
    }
}
