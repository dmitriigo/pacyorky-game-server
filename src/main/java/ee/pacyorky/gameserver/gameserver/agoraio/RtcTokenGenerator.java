package ee.pacyorky.gameserver.gameserver.agoraio;

import ee.pacyorky.gameserver.gameserver.config.AgoraProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RtcTokenGenerator {
    
    private RtcTokenGenerator() {
    }
    
    public static String buildTokenWithUserAccount(AgoraProperties agoraProperties,
                                                   Long gameId) {
        
        // Assign appropriate access privileges to each role.
        int privilegeTs = (int) (System.currentTimeMillis() / 1000 + 7200);
        AccessToken builder = new AccessToken(agoraProperties.getAgoraId(), agoraProperties.getAgoraCertificate(), String.valueOf(gameId), "");
        builder.addPrivilege(AccessToken.Privileges.K_JOIN_CHANNEL, privilegeTs);
        builder.addPrivilege(AccessToken.Privileges.K_PUBLISH_AUDIO_STREAM, privilegeTs);
        try {
            return builder.build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }
}
