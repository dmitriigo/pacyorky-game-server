package ee.pacyorky.gameserver.gameserver.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ee.pacyorky.gameserver.gameserver.entities.security.BanInfo;

/**
 * Date: 30.11.2021
 * Time: 14:34
 */
public interface BanInfoRepository extends JpaRepository<BanInfo, Long> {
    
    @Query(value = "SELECT * FROM ban_info b WHERE EXISTS (SELECT 1 FROM player p WHERE p.ban_info_id=b.id AND p.id=:playerId) AND b.bans_counter > 0", nativeQuery = true)
    Optional<BanInfo> getBanInfoByPlayerId(String playerId);
}
