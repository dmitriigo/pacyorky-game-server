package ee.pacyorky.gameserver.gameserver.exceptions;

import ee.pacyorky.gameserver.gameserver.dtos.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GlobalException.class)
    public @ResponseBody
    ExceptionDto handleGlobalException(GlobalException exception, HttpServletRequest request) {
        ExceptionDto response = new ExceptionDto(exception.getCode(), exception.getMessage());
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    ExceptionDto handleException(Exception exception, HttpServletRequest request) {
        ExceptionDto response = new ExceptionDto(GlobalExceptionCode.INTERNAL_SERVER_ERROR, exception.getMessage());
        return response;
    }
}
