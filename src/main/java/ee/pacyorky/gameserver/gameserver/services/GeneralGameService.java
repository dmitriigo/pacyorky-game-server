package ee.pacyorky.gameserver.gameserver.services;

import ee.pacyorky.gameserver.gameserver.entities.Player;
import org.springframework.http.ResponseEntity;

public interface GeneralGameService {
    Player startGame(Long gameId, String playerId);
}
