package ee.pacyorky.gameserver.gameserver.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    private HolidayCard holidayCard;

    public void setSeason(Season season) {
        this.season = season.getId();
    }

    public Season getSeason() {
        return Season.getById(this.season);
    }

}
