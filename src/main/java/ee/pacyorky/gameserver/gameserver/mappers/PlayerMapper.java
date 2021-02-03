package ee.pacyorky.gameserver.gameserver.mappers;

import ee.pacyorky.gameserver.gameserver.dtos.PlayerDTO;
import ee.pacyorky.gameserver.gameserver.entities.CardType;
import ee.pacyorky.gameserver.gameserver.entities.Player;
import ee.pacyorky.gameserver.gameserver.entities.Season;
import org.mapstruct.Mapper;

@Mapper
public interface PlayerMapper {

    PlayerDTO toDto(Player player);

    default Long map(CardType cardType) {
        return cardType.getId();
    }

    default Long map(Season season) {
        return season.getId();
    }

}
