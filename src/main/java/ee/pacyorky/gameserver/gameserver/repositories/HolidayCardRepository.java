package ee.pacyorky.gameserver.gameserver.repositories;

import ee.pacyorky.gameserver.gameserver.entities.game.HolidayCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayCardRepository extends JpaRepository<HolidayCard, Long> {
}
