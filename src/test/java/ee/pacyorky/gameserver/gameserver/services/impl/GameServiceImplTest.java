package ee.pacyorky.gameserver.gameserver.services.impl;

import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.entities.Game;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.repositories.GameRepository;
import ee.pacyorky.gameserver.gameserver.services.GameService;
import ee.pacyorky.gameserver.gameserver.services.PlayerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@SpringBootTest
class GameServiceImplTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerService playerService;


    @Test
    void createGame() {
        String playerId = UUID.randomUUID().toString();

        Game game = gameService.createGame(playerId, new GameCreationDto());

        Assertions.assertNotNull(game);

        Assertions.assertEquals(game.getPlayers().stream().findFirst().orElseThrow().getId(), playerId);
    }

    @Test
    void joinIntoTheGame() {
        String playerId = UUID.randomUUID().toString();
        String playerId2 = UUID.randomUUID().toString();
        String playerId3 = UUID.randomUUID().toString();
        Game game = gameService.createGame(playerId, new GameCreationDto());
        game.setCapacity(2L);
        Assertions.assertNotNull(gameService.joinIntoTheGame(playerId2, game.getId()));
        Assertions.assertThrows(GlobalException.class, () -> gameService.joinIntoTheGame(playerId3, game.getId()));
        Game anotherGame = gameService.createGame(UUID.randomUUID().toString(), new GameCreationDto());
        anotherGame.setCapacity(2L);
        Assertions.assertThrows(GlobalException.class, () -> gameService.joinIntoTheGame(playerId2, anotherGame.getId()));
    }

    @Test
    void leftFromTheGame() {
        String playerId = UUID.randomUUID().toString();
        Game game = gameService.createGame(playerId, new GameCreationDto());
        Assertions.assertEquals(game.getPlayers().size(), 1);
        gameService.leftFromTheGame(playerId);
        Assertions.assertEquals(game.getPlayers().size(), 0);
    }
}
