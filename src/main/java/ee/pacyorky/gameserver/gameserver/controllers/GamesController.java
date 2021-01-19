package ee.pacyorky.gameserver.gameserver.controllers;

import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.entities.Game;
import ee.pacyorky.gameserver.gameserver.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@AllArgsConstructor
public class GamesController {

    private final GameService gameService;

    @GetMapping("/games")
    public ResponseEntity<List<Game>> getGames() {
        return ResponseEntity.ok(gameService.getGames());
    }

    @PostMapping("/games")
    public ResponseEntity<Game> addGame(@RequestBody GameCreationDto gameCreationDto, HttpSession httpSession) {
        return ResponseEntity.ok(gameService.createGame(httpSession.getId(), gameCreationDto));
    }

    @GetMapping("/games/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable("gameId") Long gameId) {
        return ResponseEntity.ok(gameService.getGame(gameId));
    }

    @PostMapping("/game/{gameId}")
    public ResponseEntity<Game> joinIntoTheGame(@PathVariable("gameId") Long gameId, HttpSession httpSession) {
        gameService.joinIntoTheGame(httpSession.getId(), gameId);
        return ResponseEntity.ok(gameService.joinIntoTheGame(httpSession.getId(), gameId));
    }

    @DeleteMapping("/game/{gameId}")
    public ResponseEntity<Game> leftFromTheGame(@PathVariable("gameId") Long gameId, HttpSession httpSession) {
        return ResponseEntity.ok(gameService.leftFromTheGame(httpSession.getId(), gameId));
    }
}
