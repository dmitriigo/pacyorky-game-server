package ee.pacyorky.gameserver.gameserver.controllers.socket;

import ee.pacyorky.gameserver.gameserver.dtos.GameCreationDto;
import ee.pacyorky.gameserver.gameserver.dtos.GameDTO;
import ee.pacyorky.gameserver.gameserver.mappers.GameMapper;
import ee.pacyorky.gameserver.gameserver.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "*")
public class RoomsSocketController {

    @Autowired
    private GameService gameService;

    @MessageMapping("/add-game")
    @SendTo("/topic/games")
    public ResponseEntity<List<GameDTO>> addGame(GameCreationDto gameCreationDto, HttpSession httpSession) {
        gameService.createGame(httpSession.getId(), gameCreationDto);
        return ResponseEntity.ok(gameService.getGames().stream().map(GameMapper.INSTANCE::toGameDto).collect(Collectors.toList()));
    }
}
