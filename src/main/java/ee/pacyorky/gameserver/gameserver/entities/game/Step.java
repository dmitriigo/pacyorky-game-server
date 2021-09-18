package ee.pacyorky.gameserver.gameserver.entities.game;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne
    private Player currentPlayer;

    private Integer counter;

    @Getter(AccessLevel.NONE)
    private Long status;

    private LocalDateTime cardThrownAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Builder.Default
    private List<StepCard> stepCards = new ArrayList<>();

    public StepStatus getStatus() {
        return StepStatus.getById(status);
    }

    public void setStatus(StepStatus status) {
        this.status = status.getId();
    }
}
