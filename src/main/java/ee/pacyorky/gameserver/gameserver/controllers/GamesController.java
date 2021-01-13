package ee.pacyorky.gameserver.gameserver.controllers;

import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.entities.Game;
import ee.pacyorky.gameserver.gameserver.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/games")
@AllArgsConstructor
public class GamesController {

    private final GameService gameService;

    @GetMapping
    public ResponseEntity<List<Game>> getGames() {
        return ResponseEntity.ok(gameService.getGames());
    }

    @PostMapping
    public ResponseEntity<Game> addGame(@RequestBody GameCreationDto gameCreationDto, HttpSession httpSession) {
        return ResponseEntity.ok(gameService.createGame(httpSession.getId(), gameCreationDto));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable("gameId") Long gameId){
        return ResponseEntity.ok(gameService.getGame(gameId));
    }

}
