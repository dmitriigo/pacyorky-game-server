package ee.pacyorky.gameserver.gameserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "game")
@Getter
@Setter
public class AppProperties {
    private Long secondsBeforeStart;
    private Long secondsBeforeStep;
    private Integer maxAttemptsForStep;
    private Integer maxAttemptsForStart;
    private Integer maxGames;
    private String secretKey;
}
