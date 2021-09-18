package ee.pacyorky.gameserver.gameserver.repositories.dao;

import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.entities.game.Status;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;
import ee.pacyorky.gameserver.gameserver.repositories.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class GameDao {

    private final GameRepository gameRepository;


    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    public List<Game> getActiveGames() {
        return gameRepository.getGamesByStatusIn(Set.of(Status.WAITING.getId(), Status.STARTED.getId()));
    }


    public Game getGame(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new GlobalException("Game not found " + gameId, GlobalExceptionCode.INTERNAL_SERVER_ERROR));
    }


    public Game saveGame(Game game) {
        return gameRepository.saveAndFlush(game);
    }

    public Game getGame(String playerId) {
        return gameRepository.getGamesByPlayerId(playerId).orElseThrow(() -> new GlobalException("Player not in game " + playerId, GlobalExceptionCode.INTERNAL_SERVER_ERROR));
    }

}
