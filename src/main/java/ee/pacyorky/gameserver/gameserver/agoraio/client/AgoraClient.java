package ee.pacyorky.gameserver.gameserver.agoraio.client;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.web.client.RestTemplate;

import ee.pacyorky.gameserver.gameserver.config.AgoraProperties;
import ee.pacyorky.gameserver.gameserver.entities.game.Player;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Date: 30.11.2021
 * Time: 13:20
 */
@AllArgsConstructor
@Slf4j
public class AgoraClient {
    
    private final ObjectFactory<RestTemplate> restTemplateFactory;
    private static final String BASE_URL = "https://api.agora.io/dev/v1/kicking-rule";
    private final AgoraProperties agoraProperties;
    
    public void ban(Player player) {
        if (player.getAgoraId() == null) {
            return;
        }
        var restTemplate = restTemplateFactory.getObject();
        var rules = restTemplate.getForObject(BASE_URL+"?appid="+agoraProperties.getAgoraId(), RulesObject.class);
        if (rules != null && "success".equals(rules.getStatus())) {
        var banInfo = player.getBanInfo();
        var banTime = banInfo.isPermanentlyBanned() ? 1440 : 120;
            var builder = BanParams.builder().time(banTime).appid(agoraProperties.getAgoraId());
            if (rules.getRules().stream().map(RulesObject.BanRule::getUid)
                    .filter(Objects::nonNull)
                    .noneMatch(ruleId -> ruleId.equals(player.getAgoraId()))) {
                restTemplate.postForEntity(BASE_URL, builder.uid(player.getAgoraId()).build(), Object.class);
            }
            if (banInfo.isPermanentlyBanned() && StringUtils.isNotBlank(banInfo.getIp()) &&
                    rules.getRules().stream().map(RulesObject.BanRule::getIp).filter(Objects::nonNull).noneMatch(ip -> ip.equals(banInfo.getIp()))) {
                builder.uid(null);
                restTemplate.postForEntity(BASE_URL, builder.ip(banInfo.getIp()).build(), Object.class);
            }
        }
    }
}
