package ee.pacyorky.gameserver.gameserver.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ee.pacyorky.gameserver.gameserver.config.AppProperties;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalException;
import ee.pacyorky.gameserver.gameserver.exceptions.GlobalExceptionCode;

@Component
public class ActuatorFilter implements Filter {
    
    @Autowired
    private AppProperties appProperties;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if ((req.getRequestURI().startsWith("/api/actuator") || req.getRequestURI().startsWith("/api/shutdown")) && !appProperties.getSecretKey().equals(req.getParameter("skey"))) {
            throw new GlobalException("404", GlobalExceptionCode.INTERNAL_SERVER_ERROR);
        }
        chain.doFilter(request, response);
    }
}
