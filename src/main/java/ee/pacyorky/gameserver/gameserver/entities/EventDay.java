package ee.pacyorky.gameserver.gameserver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

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

    public void setSeason(Season season) {
        this.season = season.getId();
    }

    public Season getSeason() {
        return Season.getById(this.season);
    }

}
