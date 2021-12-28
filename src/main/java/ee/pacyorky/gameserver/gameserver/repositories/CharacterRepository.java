package ee.pacyorky.gameserver.gameserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ee.pacyorky.gameserver.gameserver.entities.game.Character;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
}
