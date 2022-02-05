package ee.pacyorky.gameserver.gameserver.exceptions;

public final class GlobalExceptionCode {
    
    private GlobalExceptionCode() {
    }
    
    public static final String PLAYER_ALREADY_IN_GAME = "PLAYER_ALREADY_IN_GAME";
    public static final String CAPACITY_LIMIT_REACHED = "CAPACITY_LIMIT_REACHED";
    public static final String GAMES_LIMIT_REACHED = "GAMES_LIMIT_REACHED";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String PLAYER_NOT_IN_GAME = "PLAYER_NOT_IN_GAME";
    public static final String PLAYERS_COUNT_LESS = "PLAYERS_COUNT_LESS";
    public static final String STEP_NOT_WAITING_VOTE = "STEP_NOT_WAITING_VOTE";
    public static final String GAME_NOT_WAITING = "GAME_NOT_WAITING";
    public static final String PASSWORD_NOT_SPECIFIED = "PASSWORD_NOT_SPECIFIED";
    public static final String PASSWORD_WRONG = "PASSWORD_WRONG";
    public static final String USER_BANNED = "USER_BANNED";
}
