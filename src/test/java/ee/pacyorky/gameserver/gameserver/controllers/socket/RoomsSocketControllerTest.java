package ee.pacyorky.gameserver.gameserver.controllers.socket;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;

@SpringBootTest
class RoomsSocketControllerTest {

    static final String WEBSOCKET_URI = "ws://localhost:3000/api/connect";
    static final String WEBSOCKET_TOPIC = "/topic";
    BlockingQueue<String> blockingQueue;
    WebSocketStompClient stompClient;

    @Test
    void addGame() {
    }

    public void setup() {

    }

    @Test
    public void shouldReceiveAMessageFromTheServer() throws Exception {
        /*blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new SockJsClient(
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
        StompSession session = stompClient
                .connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);
        session.subscribe("/topic/games", new DefaultStompFrameHandler());

        String message = "MESSAGE TEST";
        session.send("/add-game", message.getBytes());
        var s = blockingQueue.poll(1, SECONDS);
        System.out.println(s);*/

    }

    class DefaultStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            blockingQueue.offer(new String((byte[]) o));
        }
    }
}
