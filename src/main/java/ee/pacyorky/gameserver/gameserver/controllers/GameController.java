package ee.pacyorky.gameserver.gameserver.controllers;

import ee.pacyorky.gameserver.gameserver.dtos.GameDTO;
import ee.pacyorky.gameserver.gameserver.entities.Player;
import ee.pacyorky.gameserver.gameserver.mappers.GameMapper;
import ee.pacyorky.gameserver.gameserver.services.GeneralGameService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GeneralGameService gameService;

    @GetMapping("/{id}/start")
    public ResponseEntity<Player> startGame(@PathVariable("id") Long gameId, HttpSession httpSession) {
        return ResponseEntity.ok(gameService.startGame(gameId, httpSession.getId()));
    }

    @GetMapping("/{id}/next")
    public ResponseEntity<GameDTO> nextStep(@PathVariable("id") Long gameId, HttpSession httpSession) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameService.nextStep(gameId, httpSession.getId())));
    }
}
