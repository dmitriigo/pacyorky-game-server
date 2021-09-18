package ee.pacyorky.gameserver.gameserver.controllers;

import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.dtos.GameDTO;
import ee.pacyorky.gameserver.gameserver.mappers.GameMapper;
import ee.pacyorky.gameserver.gameserver.repositories.dao.GameDao;
import ee.pacyorky.gameserver.gameserver.services.game.GameManager;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/rooms")
public class GamesController {

    private final GameManager gameManager;
    private final GameDao gameDao;

    @GetMapping("/all")
    public ResponseEntity<List<GameDTO>> getGamesAll() {
        return ResponseEntity.ok(gameDao.getGames().stream().map(GameMapper.INSTANCE::toGameDto).collect(Collectors.toList()));
    }

    @GetMapping("/get")
    public ResponseEntity<List<GameDTO>> getGames() {
        return ResponseEntity.ok(gameDao.getActiveGames().stream().map(GameMapper.INSTANCE::toGameDto).collect(Collectors.toList()));
    }

    @PostMapping("/add")
    public ResponseEntity<GameDTO> addGame(@RequestBody GameCreationDto gameCreationDto, HttpSession httpSession) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameManager.createGame(httpSession.getId(), gameCreationDto)));
    }

    @PostMapping("/join/{gameId}")
    public ResponseEntity<GameDTO> joinIntoTheGame(@PathVariable("gameId") Long gameId, HttpSession httpSession) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameManager.joinIntoTheGame(httpSession.getId(), gameId)));
    }

    @DeleteMapping("/left")
    public ResponseEntity<GameDTO> leftFromTheGame(HttpSession httpSession) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameManager.leftFromTheGame(httpSession.getId())));
    }

    @GetMapping("/get/{gameId}")
    public ResponseEntity<GameDTO> getGame(@PathVariable("gameId") Long gameId) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameDao.getGame(gameId)));
    }

    @DeleteMapping("/clear/{id}")
    public ResponseEntity<List<GameDTO>> clearGames(@PathVariable("id") Long id) {
        gameManager.clearGames(id);
        return getGames();
    }
}
