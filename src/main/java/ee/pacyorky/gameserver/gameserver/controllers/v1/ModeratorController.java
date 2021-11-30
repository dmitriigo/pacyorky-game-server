package ee.pacyorky.gameserver.gameserver.controllers.v1;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ee.pacyorky.gameserver.gameserver.dtos.ReportDto;
import ee.pacyorky.gameserver.gameserver.services.security.GameSecurityService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/moderate")
@AllArgsConstructor
public class ModeratorController {
    
    private final GameSecurityService gameSecurityService;
    
    @PostMapping("/agoraId")
    public ResponseEntity<Object> attachAgora(@RequestBody Long agoraId, HttpSession httpSession) {
        gameSecurityService.attachAgoraId(agoraId, httpSession.getId());
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/ban")
    public ResponseEntity<Object> attachAgora(HttpSession httpSession, @RequestBody String badPlayerId) {
        gameSecurityService.banPlayer(httpSession.getId(), badPlayerId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/switchOwner")
    public ResponseEntity<Object> switchOwner(HttpSession httpSession, @RequestBody String newOwnerId) {
        gameSecurityService.switchOwner(httpSession.getId(), newOwnerId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/report")
    public ResponseEntity<Object> report(HttpSession httpSession, @RequestBody ReportDto reportDto) {
        gameSecurityService.report(httpSession.getId(), reportDto);
        return ResponseEntity.ok().build();
    }
    
}
