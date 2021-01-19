package ee.pacyorky.gameserver.gameserver.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@AllArgsConstructor

public class PlayerController {

    @GetMapping("/player/id")
    public ResponseEntity<String> getPlayerID(HttpSession httpSession) {
        return ResponseEntity.ok(httpSession.getId());
    }
}
