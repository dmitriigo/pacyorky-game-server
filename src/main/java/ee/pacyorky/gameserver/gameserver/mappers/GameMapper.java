package ee.pacyorky.gameserver.gameserver.mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import ee.pacyorky.gameserver.gameserver.dtos.GameDTO;
import ee.pacyorky.gameserver.gameserver.dtos.PlayerDTO;
import ee.pacyorky.gameserver.gameserver.entities.game.CardType;
import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import ee.pacyorky.gameserver.gameserver.entities.game.Season;

@Mapper
public interface GameMapper {

    GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

    @Mapping(target = "startAt", source = "startAt", qualifiedByName = "startMapper")
    @Mapping(target = "nextStepAt", source = "nextStepAt", qualifiedByName = "startMapper")
    GameDTO toGameDto(Game game);

    default Long map(CardType cardType) {
        return cardType.getId();
    }

    default Long map(Season season) {
        return season.getId();
    }

    default PlayerDTO map(Player player) {
        return PlayerMapper.INSTANCE.toDto(player);
    }

    @Named(value = "startMapper")
    default String mapStartAt(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(date.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")));
    }
}
