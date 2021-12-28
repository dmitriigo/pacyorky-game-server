package ee.pacyorky.gameserver.gameserver.mappers;

import org.mapstruct.Mapper;

import ee.pacyorky.gameserver.gameserver.dtos.StepCardDto;
import ee.pacyorky.gameserver.gameserver.entities.game.Card;
import ee.pacyorky.gameserver.gameserver.entities.game.CardType;
import ee.pacyorky.gameserver.gameserver.entities.game.StepCard;

@Mapper
public interface StepCardMapper {
    
    StepCardDto toDto(StepCard stepCard);
    
    default Long map(CardType cardType) {
        return cardType.getId();
    }
    
    default Long map(Card card) {
        return card.getId();
    }
}
