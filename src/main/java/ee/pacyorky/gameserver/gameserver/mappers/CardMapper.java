package ee.pacyorky.gameserver.gameserver.mappers;

import ee.pacyorky.gameserver.gameserver.dtos.CardDTO;
import ee.pacyorky.gameserver.gameserver.entities.game.Card;
import ee.pacyorky.gameserver.gameserver.entities.game.CardType;
import org.mapstruct.Mapper;

@Mapper
public interface CardMapper {

    CardDTO toDto(Card card);

    default Long map(CardType cardType) {
        return cardType.getId();
    }
}
