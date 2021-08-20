package ee.pacyorky.gameserver.gameserver.services.jobs;

import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CleanupGamesJob {
    private PlayerService playerService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void clean() {
        playerService.clearPlayers();
    }
}
