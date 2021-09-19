package ee.pacyorky.gameserver.gameserver.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Hidden
public class SystemController {
    private final ConfigurableApplicationContext applicationContext;

    @GetMapping("/shutdown")
    public void shutDown() {
        applicationContext.close();
    }
}
