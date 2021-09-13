package ee.pacyorky.gameserver.gameserver.mappers;

import ee.pacyorky.gameserver.gameserver.dtos.StepCardDto;
import ee.pacyorky.gameserver.gameserver.entities.game.Card;
import ee.pacyorky.gameserver.gameserver.entities.game.CardType;
import ee.pacyorky.gameserver.gameserver.entities.game.StepCard;
import org.mapstruct.Mapper;

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
