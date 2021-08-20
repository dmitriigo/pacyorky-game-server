package ee.pacyorky.gameserver.gameserver.services.game;

import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.entities.game.Game;

import java.util.List;

public interface GameManager {

    List<Game> getGames();

    Game getGame(Long gameId);

    Game getGame(String playerId);

    Game createGame(String playerId, GameCreationDto gameCreationDto);

    Game makeStep(String playerId, List<Long> cards);

    Game joinIntoTheGame(String playerId, Long gameId);

    Game leftFromTheGame(String playerId);

    Game saveGame(Game game);

    void clearGames(Long id);
}
