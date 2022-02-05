package ee.pacyorky.gameserver.gameserver.agoraio.client;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 30.11.2021
 * Time: 15:49
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BanParams {
    private String appid;
    private Long uid;
    private String ip;
    private Integer time;
    @Builder.Default
    private List<String> privileges = List.of("join_channel", "publish_audio", "publish_video");
}
