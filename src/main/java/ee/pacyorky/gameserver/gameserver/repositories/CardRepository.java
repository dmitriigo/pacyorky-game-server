package ee.pacyorky.gameserver.gameserver.repositories;

import ee.pacyorky.gameserver.gameserver.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
