package ee.pacyorky.gameserver.gameserver.entities.game;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
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

}
