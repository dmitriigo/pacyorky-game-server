package ee.pacyorky.gameserver.gameserver.repositories.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.entities.game.Status;
import ee.pacyorky.gameserver.gameserver.entities.optimized.SimpleGameInfo;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;
import ee.pacyorky.gameserver.gameserver.repositories.GameRepository;
import lombok.AllArgsConstructor;

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
    
    public Long getActiveGamesCount() {
        return gameRepository.countGamesByStatusIn(Set.of(Status.WAITING.getId(), Status.STARTED.getId()));
    }
    
    
    public Game getGame(Long gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new GlobalException("Game not found " + gameId, GlobalExceptionCode.INTERNAL_SERVER_ERROR));
    }
    
    
    public Game saveGame(Game game) {
        return gameRepository.saveAndFlush(game);
    }
    
    public Optional<Game> getGame(String playerId) {
        if (playerId == null) {
            throw new GlobalException("Player id is null", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        return gameRepository.getGameByPlayerId(playerId);
    }
    
    public List<SimpleGameInfo> getActiveGamesSimple() {
        return gameRepository.getSimpleGameInfos(Set.of(Status.WAITING.getId(), Status.STARTED.getId()));
    }
}
