package ee.pacyorky.gameserver.gameserver.controllers;

import ee.pacyorky.gameserver.gameserver.dtos.GameDTO;
import ee.pacyorky.gameserver.gameserver.mappers.GameMapper;
import ee.pacyorky.gameserver.gameserver.services.game.GameManager;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameManager gameManager;

    @GetMapping("")
    public ResponseEntity<GameDTO> game(HttpSession httpSession) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameManager.getGame(httpSession.getId())));
    }

    @PostMapping("/step")
    public ResponseEntity<GameDTO> step(HttpSession httpSession, @RequestBody List<Long> cards) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameManager.makeStep(httpSession.getId(), cards)));
    }


}
