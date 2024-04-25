package uta.cse3310;

import static org.mockito.Mockito.*;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.net.InetSocketAddress;
import java.util.Vector;

class AppTest {

    private App app;
    private WebSocket webSocket;
    private ClientHandshake clientHandshake;
    private InetSocketAddress address;

    @BeforeEach
    void setUp() {
        address = new InetSocketAddress(9121);
        app = new App(address);
        webSocket = mock(WebSocket.class);
        clientHandshake = mock(ClientHandshake.class);
        when(webSocket.getRemoteSocketAddress()).thenReturn(new InetSocketAddress("127.0.0.1", 9121));
    }

    @Test
    void testOnOpen() {
        app.onOpen(webSocket, clientHandshake);
        verify(webSocket, times(1)).setAttachment(any(Game.class));
    }

    @Test
    void testOnMessageString() {
        Game mockGame = new Game();
        when(webSocket.getAttachment()).thenReturn(mockGame);
        app.onMessage(webSocket, "Test Message");

        // Assert changes or interactions here
        // For example, verify that the game state is updated or messages are broadcasted
    }

    @Test
    void testOnClose() {
        Game mockGame = new Game();
        when(webSocket.getAttachment()).thenReturn(mockGame);
        app.onClose(webSocket, 1000, "Normal", false);
        assertNull(webSocket.getAttachment());
    }

    @Test
    void testOnError() {
        Exception exception = new RuntimeException("Error");
        app.onError(webSocket, exception);

        // You can check logs or error handling mechanisms here
    }
}
