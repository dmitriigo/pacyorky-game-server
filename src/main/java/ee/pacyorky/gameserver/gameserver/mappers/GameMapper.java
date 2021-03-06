package ee.pacyorky.gameserver.gameserver.mappers;

import ee.pacyorky.gameserver.gameserver.dtos.GameDTO;
import ee.pacyorky.gameserver.gameserver.entities.CardType;
import ee.pacyorky.gameserver.gameserver.entities.Game;
import ee.pacyorky.gameserver.gameserver.entities.Season;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GameMapper {

    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    GameDTO toGameDto(Game game);

    default Long map(CardType cardType) {
        return cardType.getId();
    }

    default Long map(Season season) {
        return season.getId();
    }
}
