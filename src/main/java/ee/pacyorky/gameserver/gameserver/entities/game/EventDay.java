package ee.pacyorky.gameserver.gameserver.entities.game;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.Hibernate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDay {
    
    @Id
    private Long id;
    
    private boolean holiday;
    
    private Long season;
    
    private String name;
    
    private Long deskOrder;
    
    public Season getSeason() {
        return Season.getById(this.season);
    }
    
    public void setSeason(Season season) {
        this.season = season.getId();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventDay eventDay = (EventDay) o;
        return id != null && Objects.equals(id, eventDay.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
