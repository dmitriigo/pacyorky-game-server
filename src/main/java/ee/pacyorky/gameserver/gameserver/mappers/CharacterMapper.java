package ee.pacyorky.gameserver.gameserver.mappers;

import org.mapstruct.Mapper;

import ee.pacyorky.gameserver.gameserver.dtos.CharacterDTO;

@Mapper
public interface CharacterMapper {
    
    CharacterDTO toDto(Character character);
}
