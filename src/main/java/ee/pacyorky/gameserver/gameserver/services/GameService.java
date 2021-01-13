package ee.pacyorky.gameserver.gameserver.services;

import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.entities.Game;

import java.util.List;

public interface GameService {

    List<Game> getGames();

    Game getGame(Long gameId);

    Game createGame(String playerId, GameCreationDto gameCreationDto);

    Game joinIntoTheGame(String playerId, Long gameId);

    Game leftFromTheGame(String playerId, Long gameId);

    Game saveGame(Game game);
}
