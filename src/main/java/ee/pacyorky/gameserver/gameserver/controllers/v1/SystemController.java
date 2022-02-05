package ee.pacyorky.gameserver.gameserver.controllers.v1;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ee.pacyorky.gameserver.gameserver.services.security.GameSecurityService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@Hidden
public class SystemController {
    private final ConfigurableApplicationContext applicationContext;
    private final GameSecurityService gameSecurityService;
    
    @GetMapping("/shutdown")
    public void shutDown() {
        applicationContext.close();
    }
    
    @GetMapping("/ban/{id}")
    public void ban(@PathVariable("id") String id) {
        gameSecurityService.banInternal(id, true);
    }
}
