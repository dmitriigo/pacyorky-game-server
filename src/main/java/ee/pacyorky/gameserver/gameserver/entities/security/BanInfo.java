package ee.pacyorky.gameserver.gameserver.entities.security;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Date: 30.11.2021
 * Time: 12:17
 */
@Entity
@Data
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
        this.bansCounter ++;
        this.bannedAt = LocalDateTime.now();
        if (this.bansCounter >= 3) {
            this.permanentlyBanned = true;
        }
    }
}
