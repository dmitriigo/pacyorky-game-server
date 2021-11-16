package ee.pacyorky.gameserver.gameserver.services.game.impl.gameexecutors;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.function.Consumer;

@AllArgsConstructor
@Builder
public class ExecutorCallback {

    private static final Consumer<Long> empty = l -> {
    };
    private final Consumer<Long> success;
    private final Consumer<Long> fail;
    private final Consumer<Long> forceFinish;

    public static Consumer<Long> empty() {
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
