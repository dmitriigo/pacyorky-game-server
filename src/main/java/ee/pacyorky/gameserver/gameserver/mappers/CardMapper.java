package ee.pacyorky.gameserver.gameserver.mappers;

import org.mapstruct.Mapper;

import ee.pacyorky.gameserver.gameserver.dtos.CardDTO;
import ee.pacyorky.gameserver.gameserver.entities.game.Card;
import ee.pacyorky.gameserver.gameserver.entities.game.CardType;

@Mapper
public interface CardMapper {
    
    CardDTO toDto(Card card);
    
    default Long map(CardType cardType) {
        return cardType.getId();
    }
}
