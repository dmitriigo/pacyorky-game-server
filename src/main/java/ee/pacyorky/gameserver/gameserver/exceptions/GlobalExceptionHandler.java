package ee.pacyorky.gameserver.gameserver.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ee.pacyorky.gameserver.gameserver.dtos.ExceptionDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GlobalException.class)
    public @ResponseBody
    ExceptionDto handleGlobalException(GlobalException exception, HttpServletRequest request) {
        if (exception.getCode().equals(GlobalExceptionCode.INTERNAL_SERVER_ERROR)) {
            logger.error(exception.getMessage(), exception);
        }
        return new ExceptionDto(exception.getCode(), exception.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    ExceptionDto handleException(Exception exception, HttpServletRequest request) {
        logger.error(exception.getMessage(), exception);
        return new ExceptionDto(GlobalExceptionCode.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}
