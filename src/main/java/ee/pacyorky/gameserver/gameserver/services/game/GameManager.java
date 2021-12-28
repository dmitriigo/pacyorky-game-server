package ee.pacyorky.gameserver.gameserver.services.game;

import java.util.List;
import java.util.Set;

import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.entities.game.CardType;
import ee.pacyorky.gameserver.gameserver.entities.game.Game;

public interface GameManager {
    
    
    Game createGame(String playerId, GameCreationDto gameCreationDto);
    
    Game makeStep(String playerId, List<Long> cards);
    
    Game joinIntoTheGame(String playerId, Long gameId);
    
    Game leftFromTheGame(String playerId);
    
    void clearGames(Long id);
    
    Game voteCards(String playerId, Set<Long> cards);
    
    Game throwDice(String id);
    
    Game forceStart(String playerId);
    
    Game choosePrize(String playerId, CardType cardType);
}
