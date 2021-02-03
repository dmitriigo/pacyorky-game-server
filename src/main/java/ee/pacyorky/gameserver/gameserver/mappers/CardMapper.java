package ee.pacyorky.gameserver.gameserver.mappers;

import ee.pacyorky.gameserver.gameserver.dtos.CardDTO;
import ee.pacyorky.gameserver.gameserver.entities.Card;
import ee.pacyorky.gameserver.gameserver.entities.CardType;
import org.mapstruct.Mapper;

@Mapper
public interface CardMapper {

    CardDTO toDto(Card card);

    default Long map(CardType cardType) {
        return cardType.getId();
    }
}
