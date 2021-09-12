package ee.pacyorky.gameserver.gameserver.entities.game;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class StepCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Setter(AccessLevel.NONE)
    private Card card;

    @Setter(AccessLevel.NONE)
    @Builder.Default
    private long vote = 0;

    public void addVote() {
        vote++;
    }
}
