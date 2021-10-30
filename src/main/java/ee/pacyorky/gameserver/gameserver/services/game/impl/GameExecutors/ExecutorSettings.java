package ee.pacyorky.gameserver.gameserver.services.game.impl.GameExecutors;

import ee.pacyorky.gameserver.gameserver.config.AgoraProperties;
import ee.pacyorky.gameserver.gameserver.config.AppProperties;
import ee.pacyorky.gameserver.gameserver.repositories.dao.GameDao;
import ee.pacyorky.gameserver.gameserver.services.game.EventDayService;
import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ExecutorSettings {
    private final GameDao gameDao;
    private final PlayerService playerService;
    private final EventDayService eventDayService;
    private final Long gameId;
    private final ExecutorCallback executorCallback;
    private final AppProperties appProperties;
    private final AgoraProperties agoraProperties;
}
