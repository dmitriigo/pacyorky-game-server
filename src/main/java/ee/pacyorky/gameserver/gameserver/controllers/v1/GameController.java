package ee.pacyorky.gameserver.gameserver.controllers.v1;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.pacyorky.gameserver.gameserver.dtos.GameDTO;
import ee.pacyorky.gameserver.gameserver.entities.game.CardType;
import ee.pacyorky.gameserver.gameserver.mappers.GameMapper;
import ee.pacyorky.gameserver.gameserver.repositories.dao.GameDao;
import ee.pacyorky.gameserver.gameserver.services.game.GameManager;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/game")
public class GameController {
    
    private final GameManager gameManager;
    private final GameDao gameDao;
    
    @GetMapping("")
    public ResponseEntity<GameDTO> game(HttpSession httpSession) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameDao.getGame(httpSession.getId()).orElse(null)));
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
    
    @PostMapping("/start")
    public ResponseEntity<GameDTO> startGame(HttpSession httpSession) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameManager.forceStart(httpSession.getId())));
    }
    
    @PostMapping("/choosePrize")
    public ResponseEntity<GameDTO> choosePrize(HttpSession httpSession, @RequestBody CardType cardType) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameManager.choosePrize(httpSession.getId(), cardType)));
    }
    
}
