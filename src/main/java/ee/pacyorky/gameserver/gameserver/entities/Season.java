package ee.pacyorky.gameserver.gameserver.entities;

import java.util.Objects;

public enum Season {
    SUMMER("Summer", 3L),
    SPRING("Spring", 2L),
    AUTUMN("Autumn", 4L),
    WINTER("Winter", 1L);

    String name;
    Long id;

    Season(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public static Season getById(Long id) {
        for (Season value : Season.values()) {
            if (Objects.equals(id, value.id)) return value;
        }
        throw new RuntimeException("Season with id: " + id + " not found");
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
