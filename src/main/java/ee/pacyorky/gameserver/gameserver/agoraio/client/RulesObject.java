package ee.pacyorky.gameserver.gameserver.agoraio.client;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 30.11.2021
 * Time: 14:15
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RulesObject {
    private String status;
    private List<BanRule> rules;
    
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class BanRule {
        private Long id;
        private Long uid;
        private String ip;
    }
    
}
