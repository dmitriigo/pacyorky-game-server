package ee.pacyorky.gameserver.gameserver.controllers.facade;

import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.dtos.GameDTO;
import ee.pacyorky.gameserver.gameserver.entities.optimized.SimpleGameInfo;
import ee.pacyorky.gameserver.gameserver.mappers.GameMapper;
import ee.pacyorky.gameserver.gameserver.repositories.dao.GameDao;
import ee.pacyorky.gameserver.gameserver.services.game.GameManager;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@AllArgsConstructor
@Service
public class RoomsControllerFacade {

    private final GameManager gameManager;
    private final GameDao gameDao;

    public ResponseEntity<List<GameDTO>> getGamesAll() {
        return ResponseEntity.ok(gameDao.getGames().stream().map(GameMapper.INSTANCE::toGameDto).collect(Collectors.toList()));
    }


    public ResponseEntity<List<GameDTO>> getGames() {
        return ResponseEntity.ok(gameDao.getActiveGames().stream().map(GameMapper.INSTANCE::toGameDto).collect(Collectors.toList()));
    }

    public ResponseEntity<List<SimpleGameInfo>> getGamesV2() {
        return ResponseEntity.ok(gameDao.getActiveGamesSimple());
    }


    public ResponseEntity<GameDTO> addGame(GameCreationDto gameCreationDto, HttpSession httpSession) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameManager.createGame(httpSession.getId(), gameCreationDto)));
    }


    public ResponseEntity<GameDTO> joinIntoTheGame(Long gameId, HttpSession httpSession, String password) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameManager.joinIntoTheGame(httpSession.getId(), gameId, password)));
    }


    public ResponseEntity<GameDTO> leftFromTheGame(HttpSession httpSession) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameManager.leftFromTheGame(httpSession.getId())));
    }


    public ResponseEntity<GameDTO> getGame(@PathVariable("gameId") Long gameId) {
        return ResponseEntity.ok(GameMapper.INSTANCE.toGameDto(gameDao.getGame(gameId)));
    }


    public ResponseEntity<List<GameDTO>> clearGames(Long id) {
        gameManager.clearGames(id);
        return getGames();
    }

}
