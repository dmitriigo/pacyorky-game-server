package ee.pacyorky.gameserver.gameserver.services.game.impl;

import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.entities.game.Game;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.repositories.GameRepository;
import ee.pacyorky.gameserver.gameserver.services.game.GameManager;
import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
        Game game = gameManager.createGame(playerId, new GameCreationDto());
        Assertions.assertNotNull(game);
        Assertions.assertEquals(game.getPlayers().stream().findFirst().orElseThrow().getId(), playerId);
    }

    @Test
    void joinIntoTheGame() {
        String playerId = UUID.randomUUID().toString();
        String playerId2 = UUID.randomUUID().toString();
        String playerId3 = UUID.randomUUID().toString();
        Game game = gameManager.createGame(playerId, new GameCreationDto());
        game.setCapacity(2L);
        Assertions.assertNotNull(gameManager.joinIntoTheGame(playerId2, game.getId()));
        Assertions.assertThrows(GlobalException.class, () -> gameManager.joinIntoTheGame(playerId3, game.getId()));
        Game anotherGame = gameManager.createGame(UUID.randomUUID().toString(), new GameCreationDto());
        anotherGame.setCapacity(2L);
        Assertions.assertThrows(GlobalException.class, () -> gameManager.joinIntoTheGame(playerId2, anotherGame.getId()));
    }

    @Test
    void leftFromTheGame() {
        String playerId = UUID.randomUUID().toString();
        Game game = gameManager.createGame(playerId, new GameCreationDto());
        Assertions.assertEquals(game.getPlayers().size(), 1);
        gameManager.leftFromTheGame(playerId);
        Assertions.assertEquals(game.getPlayers().size(), 0);
    }
}
