package ee.pacyorky.gameserver.gameserver.controllers;

import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.dtos.GameDTO;
import ee.pacyorky.gameserver.gameserver.mappers.GameMapper;
import ee.pacyorky.gameserver.gameserver.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class GamesController {

    private final GameService gameService;

    @GetMapping("/games")
    public ResponseEntity<List<GameDTO>> getGames() {
        return ResponseEntity.ok(gameService.getGames().stream().map(GameMapper.INSTANCE::toGameDto).collect(Collectors.toList()));
    }

    @PostMapping("/games")
    public ResponseEntity<GameDTO> addGame(@RequestBody GameCreationDto gameCreationDto, HttpSession httpSession) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameService.createGame(httpSession.getId(), gameCreationDto)));
    }

    @GetMapping("/games/{gameId}")
    public ResponseEntity<GameDTO> getGame(@PathVariable("gameId") Long gameId) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameService.getGame(gameId)));
    }

    @PostMapping("/game/{gameId}")
    public ResponseEntity<GameDTO> joinIntoTheGame(@PathVariable("gameId") Long gameId, HttpSession httpSession) {
        gameService.joinIntoTheGame(httpSession.getId(), gameId);
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameService.joinIntoTheGame(httpSession.getId(), gameId)));
    }

    @DeleteMapping("/game/{gameId}")
    public ResponseEntity<GameDTO> leftFromTheGame(@PathVariable("gameId") Long gameId, HttpSession httpSession) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameService.leftFromTheGame(httpSession.getId(), gameId)));
    }
}
