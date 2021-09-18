package ee.pacyorky.gameserver.gameserver.repositories;

import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> getGamesByStatusIn(Collection<Long> status);

    /*@Query(value = "SELECT g FROM Game g WHERE g.players in (SELECT p from Player p WHERE p.id = :playerId)")
    Optional<Game> getGamesByPlayerId(String playerId);*/

    Optional<Game> getGameByPlayersContains(Player player);
}
