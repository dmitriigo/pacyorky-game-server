package ee.pacyorky.gameserver.gameserver.services.game.impl;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.repositories.GameRepository;
import ee.pacyorky.gameserver.gameserver.services.game.GameManager;
import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class GameManagerImplTest {
    
    @Autowired
    private GameManager gameManager;
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private PlayerService playerService;
    
    
    @Test
    void createGame() {
        String playerId = UUID.randomUUID().toString();
        Game game = gameManager.createGame(playerId, getCreationDto());
        Assertions.assertNotNull(game);
        Assertions.assertEquals(game.getPlayers().stream().findFirst().orElseThrow().getId(), playerId);
    }
    
    @Test
    void joinIntoTheGame() {
        String playerId = UUID.randomUUID().toString();
        String playerId2 = UUID.randomUUID().toString();
        String playerId3 = UUID.randomUUID().toString();
        Game game = gameManager.createGame(playerId, getCreationDto());
        var gameId = game.getId();
        game.setCapacity(2L);
        Assertions.assertNotNull(gameManager.joinIntoTheGame(playerId2, gameId, null));
        Assertions.assertThrows(GlobalException.class, () -> gameManager.joinIntoTheGame(playerId3, gameId, null));
        Game anotherGame = gameManager.createGame(UUID.randomUUID().toString(), getCreationDto());
        var gameId2 = anotherGame.getId();
        anotherGame.setCapacity(2L);
        Assertions.assertThrows(GlobalException.class, () -> gameManager.joinIntoTheGame(playerId2, gameId2, null));
    }
    
    @Test
    void leftFromTheGame() {
        String playerId = UUID.randomUUID().toString();
        Game game = gameManager.createGame(playerId, getCreationDto());
        Assertions.assertEquals(1, game.getPlayers().size());
        gameManager.leftFromTheGame(playerId);
        Assertions.assertEquals(0, game.getPlayers().size());
    }
    
    private GameCreationDto getCreationDto() {
        var result = new GameCreationDto();
        result.setCapacity(5L);
        return result;
    }
}
