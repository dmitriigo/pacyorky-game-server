package ee.pacyorky.gameserver.gameserver.services.security;

import org.springframework.stereotype.Service;

import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GameSecurityService {
    
    private final PlayerService playerService;
    
    public void attachAgoraId(Long agoraId, String id) {
        var player = playerService.getOrCreatePlayer(id);
        player.setAgoraId(agoraId);
        playerService.savePlayer(player);
    }
}
