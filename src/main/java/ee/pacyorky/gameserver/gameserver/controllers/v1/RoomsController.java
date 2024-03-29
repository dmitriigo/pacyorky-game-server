package ee.pacyorky.gameserver.gameserver.controllers.v1;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ee.pacyorky.gameserver.gameserver.controllers.facade.RoomsControllerFacade;
import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.dtos.GameDTO;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/rooms")
public class RoomsController {
    
    private final RoomsControllerFacade facade;
    
    @GetMapping("/all")
    public ResponseEntity<List<GameDTO>> getGamesAll() {
        return facade.getGamesAll();
    }
    
    @GetMapping("/get")
    public ResponseEntity<List<GameDTO>> getGames() {
        return facade.getGames();
    }
    
    @PostMapping("/add")
    public ResponseEntity<GameDTO> addGame(@RequestBody GameCreationDto gameCreationDto, HttpSession httpSession) {
        return facade.addGame(gameCreationDto, httpSession);
    }
    
    @PostMapping("/join/{gameId}")
    public ResponseEntity<GameDTO> joinIntoTheGame(@PathVariable("gameId") Long gameId, HttpSession httpSession) {
        return facade.joinIntoTheGame(gameId, httpSession, null);
    }
    
    @DeleteMapping("/left")
    public ResponseEntity<GameDTO> leftFromTheGame(HttpSession httpSession) {
        return facade.leftFromTheGame(httpSession);
    }
    
    @GetMapping("/get/{gameId}")
    public ResponseEntity<GameDTO> getGame(@PathVariable("gameId") Long gameId) {
        return facade.getGame(gameId);
    }
    
    @DeleteMapping("/clear/{id}")
    public ResponseEntity<List<GameDTO>> clearGames(@PathVariable("id") Long id) {
        return facade.clearGames(id);
    }
}
