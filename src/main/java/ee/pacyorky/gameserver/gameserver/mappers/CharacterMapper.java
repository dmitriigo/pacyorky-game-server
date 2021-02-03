package ee.pacyorky.gameserver.gameserver.mappers;

import ee.pacyorky.gameserver.gameserver.dtos.CharacterDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CharacterMapper {

    CharacterDTO toDto(Character character);
}
