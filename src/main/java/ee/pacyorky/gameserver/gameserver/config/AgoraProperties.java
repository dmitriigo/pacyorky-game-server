package ee.pacyorky.gameserver.gameserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "agora")
@Getter
@Setter
public class AgoraProperties {
    private String agoraId;
    private String agoraCertificate;
    private boolean createTokenOnCreateGame;
    private boolean voiceChatInComputerGame;
}
