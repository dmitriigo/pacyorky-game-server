package ee.pacyorky.gameserver.gameserver.controllers.v1;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ee.pacyorky.gameserver.gameserver.services.security.GameSecurityService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/moderate")
@AllArgsConstructor
public class ModeratorController {
    
    private final GameSecurityService gameSecurityService;
    
    @GetMapping("/agoraId")
    public ResponseEntity<Object> attachAgora(@RequestParam("agoraId") Long agoraId, HttpSession httpSession) {
        gameSecurityService.attachAgoraId(agoraId, httpSession.getId());
        return ResponseEntity.ok().build();
    }
    
}
