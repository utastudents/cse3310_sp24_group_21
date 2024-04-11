package uta.cse3310;

import org.java_websocket.WebSocket;
import java.util.Collection;

public class Chat {
    private final Collection<WebSocket> clients;

    public Chat(Collection<WebSocket> clients) {
        this.clients = clients;
    }

    public void broadcast(String message) {
        for (WebSocket client : clients) {
            if (client.isOpen()) { // Check if the client connection is open before sending the message
                client.send(message);
            }
        }
    }
}
