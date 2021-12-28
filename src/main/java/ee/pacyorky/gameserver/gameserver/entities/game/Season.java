package ee.pacyorky.gameserver.gameserver.entities.game;

import java.util.Objects;

import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;

public enum Season {
    SUMMER("Summer", 3L),
    SPRING("Spring", 2L),
    AUTUMN("Autumn", 4L),
    WINTER("Winter", 1L);
    
    final String name;
    final Long id;
    
    Season(String name, Long id) {
        this.name = name;
        this.id = id;
    }
    
    public static Season getById(Long id) {
        for (Season value : Season.values()) {
            if (Objects.equals(id, value.id)) return value;
        }
        throw new GlobalException("Season with id: " + id + " not found", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
    }
    
    public String getName() {
        return name;
    }
    
    public Long getId() {
        return id;
    }
}
