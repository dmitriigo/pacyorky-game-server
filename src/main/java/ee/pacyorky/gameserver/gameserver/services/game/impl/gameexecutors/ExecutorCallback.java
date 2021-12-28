package ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors;

import java.util.function.LongConsumer;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class ExecutorCallback {
    
    private static final LongConsumer empty = l -> {
    };
    private final LongConsumer success;
    private final LongConsumer fail;
    private final LongConsumer forceFinish;
    
    public static LongConsumer empty() {
        return empty;
    }
    
    public void success(Long gameId) {
        success.accept(gameId);
    }
    
    public void forceFinish(Long gameId) {
        forceFinish.accept(gameId);
    }
    
    public void fail(Long gameId) {
        fail.accept(gameId);
    }
}
