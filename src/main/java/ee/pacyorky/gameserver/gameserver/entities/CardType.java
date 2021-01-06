package ee.pacyorky.gameserver.gameserver.entities;

import java.util.Objects;

public enum CardType {
    DISHES ("Dishes", 1L),
    HOLIDAY ("Holiday", 2L),
    RITUALS ("Rituals", 3L),
    STUFF ("Stuff", 4L);

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
        throw new RuntimeException("Card type with id: "+id+" not found");
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
