package ee.pacyorky.gameserver.gameserver.controllers;

import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/player")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/id")
    public ResponseEntity<String> getPlayerID(HttpSession httpSession) {
        return ResponseEntity.ok(httpSession.getId());
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Boolean> clearPlayers() {
        playerService.clearPlayers();
        return ResponseEntity.ok(true);
    }
}
