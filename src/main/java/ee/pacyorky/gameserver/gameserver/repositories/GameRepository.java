package ee.pacyorky.gameserver.gameserver.repositories;

import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> getGamesByStatusIn(Collection<Long> status);

    @Query(value = "SELECT * FROM game g WHERE :playerId in (SELECT p.players_id FROM game_players p WHERE p.game_id = g.id)", nativeQuery = true)
    Optional<Game> getGameByPlayerId(String playerId);

}
