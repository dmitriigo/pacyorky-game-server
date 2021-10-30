package ee.pacyorky.gameserver.gameserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "agora")
@Getter
@Setter
public class AgoraProperties {
    private String agoraId;
    private String agoraCertificate;
    private boolean createTokenOnCreateGame;
    private boolean voiceChatInComputerGame;
}
