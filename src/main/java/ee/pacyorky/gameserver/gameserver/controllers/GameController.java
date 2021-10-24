package ee.pacyorky.gameserver.gameserver.controllers;

import ee.pacyorky.gameserver.gameserver.dtos.GameDTO;
import ee.pacyorky.gameserver.gameserver.mappers.GameMapper;
import ee.pacyorky.gameserver.gameserver.repositories.dao.GameDao;
import ee.pacyorky.gameserver.gameserver.services.game.GameManager;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameManager gameManager;
    private final GameDao gameDao;

    @GetMapping("")
    public ResponseEntity<GameDTO> game(HttpSession httpSession) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameDao.getGame(httpSession.getId())));
    }

    @PostMapping("/step")
    public ResponseEntity<GameDTO> step(HttpSession httpSession, @RequestBody List<Long> cards) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameManager.makeStep(httpSession.getId(), cards)));
    }

    @PostMapping("/throw")
    public ResponseEntity<GameDTO> throwDice(HttpSession httpSession) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameManager.throwDice(httpSession.getId())));
    }

    @PostMapping("/vote")
    public ResponseEntity<GameDTO> voteCards(HttpSession httpSession, @RequestBody Set<Long> cards) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameManager.voteCards(httpSession.getId(), cards)));
    }


}
