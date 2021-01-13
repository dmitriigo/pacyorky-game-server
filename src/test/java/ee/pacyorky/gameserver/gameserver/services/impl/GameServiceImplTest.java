package ee.pacyorky.gameserver.gameserver.services.impl;

import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.entities.Game;
import ee.pacyorky.gameserver.gameserver.services.GameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class GameServiceImplTest {

    @Autowired
    private GameService gameService;

    @Test
    void createGame() {
        String playerId = UUID.randomUUID().toString();

        Game game = gameService.createGame(playerId,  new GameCreationDto());

        Assertions.assertNotNull(game);

        Assertions.assertEquals(game.getPlayers().stream().findFirst().orElseThrow().getId(), playerId);
    }

    @Test
    void joinIntoTheGame() {

    }
}