package ee.pacyorky.gameserver.gameserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import ee.pacyorky.gameserver.gameserver.config.AgoraProperties;
import ee.pacyorky.gameserver.gameserver.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class, AgoraProperties.class})
public class GameServerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(GameServerApplication.class, args);
    }
    
}
