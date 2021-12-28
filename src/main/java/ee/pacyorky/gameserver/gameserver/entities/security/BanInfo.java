package ee.pacyorky.gameserver.gameserver.entities.security;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.Hibernate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Date: 30.11.2021
 * Time: 12:17
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BanInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private Long id;
    private String ip;
    private byte bansCounter;
    private boolean permanentlyBanned;
    private LocalDateTime bannedAt;
    
    public void newBan() {
        this.bansCounter++;
        this.bannedAt = LocalDateTime.now();
        if (this.bansCounter >= 3) {
            this.permanentlyBanned = true;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BanInfo banInfo = (BanInfo) o;
        return id != null && Objects.equals(id, banInfo.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
