package ee.pacyorky.gameserver.gameserver.repositories;

import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.entities.optimized.SimpleGameInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> getGamesByStatusIn(Collection<Long> status);

    Long countGamesByStatusIn(Collection<Long> status);

    @Query(value = "SELECT * FROM game g WHERE :playerId in (SELECT p.players_id FROM game_players p WHERE p.game_id = g.id)", nativeQuery = true)
    Optional<Game> getGameByPlayerId(String playerId);

    @Query(
            value = "SELECT g.id as id, g.name as name, g.capacity as capacity, COUNT(p.players_id) as playersCount " +
                    " FROM game g LEFT JOIN game_players p ON p.game_id = g.id WHERE g.status IN (:statuses) GROUP BY g.id",
            nativeQuery = true
    )
    List<SimpleGameInfo> getSimpleGameInfos(Collection<Long> statuses);

}
