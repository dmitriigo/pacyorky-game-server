package ee.pacyorky.gameserver.gameserver.exceptions;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
    
    private final String code;
    
    public GlobalException(String message, String code) {
        super(message);
        this.code = code;
    }
}
