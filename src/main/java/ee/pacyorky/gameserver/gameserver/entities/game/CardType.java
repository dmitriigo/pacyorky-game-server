package ee.pacyorky.gameserver.gameserver.entities.game;

import java.util.Objects;

import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;

public enum CardType {
    DISHES("Dishes", 1L),
    RITUALS("Rituals", 2L),
    STUFF("Stuff", 3L);
    
    final String name;
    final Long id;
    
    CardType(String name, Long id) {
        this.name = name;
        this.id = id;
    }
    
    public static CardType getById(Long id) {
        for (CardType value : CardType.values()) {
            if (Objects.equals(id, value.id)) return value;
        }
        throw new GlobalException("Card type with id: " + id + " not found", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
    }
    
    public String getName() {
        return name;
    }
    
    public Long getId() {
        return id;
    }
}
