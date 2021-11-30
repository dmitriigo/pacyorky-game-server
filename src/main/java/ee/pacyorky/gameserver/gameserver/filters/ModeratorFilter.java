package ee.pacyorky.gameserver.gameserver.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import ee.pacyorky.gameserver.gameserver.agoraio.client.AgoraClient;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;
import ee.pacyorky.gameserver.gameserver.repositories.BanInfoRepository;
import ee.pacyorky.gameserver.gameserver.services.game.PlayerService;
import ee.pacyorky.gameserver.gameserver.services.security.GameSecurityService;
import lombok.AllArgsConstructor;

/**
 * Date: 30.11.2021
 * Time: 14:25
 */
@Component
@AllArgsConstructor
public class ModeratorFilter implements Filter {
    
    private final GameSecurityService gameSecurityService;
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var httpSession = ((HttpServletRequest) servletRequest).getSession(true);
        gameSecurityService.getBanInfoByPlayerId(httpSession.getId()).ifPresent(info -> {
            if (info.getIp() == null || !info.getIp().equals(servletRequest.getRemoteAddr())) {
                gameSecurityService.renewIp(info, servletRequest.getRemoteAddr(), httpSession.getId());
            }
            if (info.isPermanentlyBanned()) {
                throw new GlobalException("User is banned permanently", GlobalExceptionCode.USER_BANNED);
            }
        });
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
