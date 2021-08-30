package ee.pacyorky.gameserver.gameserver.entities.game;

import lombok.*;

import javax.persistence.*;

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

    public Status getStatus() {
        return Status.getById(status);
    }

    public void setStatus(Status status) {
        this.status = status.getId();
    }
}
