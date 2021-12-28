package ee.pacyorky.gameserver.gameserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ee.pacyorky.gameserver.gameserver.entities.game.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    
    List<Card> getAllByCardType(Long cardType);
    
}
