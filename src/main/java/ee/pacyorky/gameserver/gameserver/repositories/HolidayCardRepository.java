package ee.pacyorky.gameserver.gameserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ee.pacyorky.gameserver.gameserver.entities.game.HolidayCard;

public interface HolidayCardRepository extends JpaRepository<HolidayCard, Long> {
}
