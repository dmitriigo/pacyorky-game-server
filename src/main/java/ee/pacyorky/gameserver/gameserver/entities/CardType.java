package ee.pacyorky.gameserver.gameserver.entities;

import java.util.Objects;

public enum CardType {
    DISHES("Dishes", 1L),
    RITUALS("Rituals", 2L),
    STUFF("Stuff", 3L);

    String name;
    Long id;

    CardType(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public static CardType getById(Long id) {
        for (CardType value : CardType.values()) {
            if (Objects.equals(id, value.id)) return value;
        }
        throw new RuntimeException("Card type with id: " + id + " not found");
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
