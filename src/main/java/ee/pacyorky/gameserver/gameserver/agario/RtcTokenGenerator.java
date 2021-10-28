package ee.pacyorky.gameserver.gameserver.agario;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RtcTokenGenerator {

    private RtcTokenGenerator() {
    }

    public static String buildTokenWithUserAccount(String appId, String appCertificate,
                                                   String channelName) {

        // Assign appropriate access privileges to each role.
        int privilegeTs = (int)(System.currentTimeMillis() / 1000 + 1080);
        AccessToken builder = new AccessToken(appId, appCertificate, channelName, "");
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
