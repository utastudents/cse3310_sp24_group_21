package uta.cse3310;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Vector;

import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Map;
import java.util.HashMap;
import java.time.Instant;
import java.time.Duration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WebSocket extends WebSocketServer {
    private Vector<Game> ActiveGames = new Vector<Game>();
    private int GameId = 1;
    private Statistics stats;
    private Instant startTime;
    private int connectionId = 0;

    public WebSocket(int port) {
        super(new InetSocketAddress(port));
    }

    public WebSocket(InetSocketAddress address) {
        super(address);
    }

    public WebSocket(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.singletonList(draft));
    }
    

    @Override
    public void onStart() {
        System.out.println("WebSocket server started successfully.");
    }

    @Override
    public void onOpen(org.java_websocket.WebSocket conn, ClientHandshake handshake) {
        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
        ServerEvent E = new ServerEvent();

        Game G = null;
        for (Game i : ActiveGames) {
            if (i.Players == PlayerType.PLAYERONE) {
                G = i;
                System.out.println("Found a match");
            }
        }

        if (G == null) {
            G = new Game(stats);
            G.GameId = GameId;
            GameId++;
            G.Players = PlayerType.PLAYERONE;
            ActiveGames.add(G);
            System.out.println("creating new game");
        } else if (G.Players == PlayerType.PLAYERONE) {
            System.out.println("not a new game");
            G.Players = PlayerType.PLAYERTWO;
            G.StartGame();
        } else if (G.Players == PlayerType.PLAYERTWO) {
            G.Players = PlayerType.PLAYERTHREE;
            G.StartGame();
        } else if (G.Players == PlayerType.PLAYERTHREE) {
            G.Players = PlayerType.PLAYERFOUR;
            G.StartGame();
        }

        E.YouAre = G.Players;
        E.GameId = G.GameId;

        conn.setAttachment(G);

        Gson gson = new Gson();
        String jsonString = gson.toJson(E);
        conn.send(jsonString);
        System.out.println("> " + Duration.between(startTime, Instant.now()).toMillis() + " " + connectionId + " "
                + escape(jsonString));

        stats.setRunningTime(Duration.between(startTime, Instant.now()).toSeconds());

        jsonString = gson.toJson(G);
        System.out.println("< " + Duration.between(startTime, Instant.now()).toMillis() + " " + "*" + " "
                + escape(jsonString));
        broadcast(jsonString);
    }

    @Override
    public void onClose(org.java_websocket.WebSocket conn, int code, String reason, boolean remote) {
        System.out.println(conn + " has closed");
        Game G = conn.getAttachment();
        G = null;
    }

    @Override
    public void onMessage(org.java_websocket.WebSocket conn, String message) {
        System.out.println("Received message from client: " + message);
    }

    @Override
    public void onError(org.java_websocket.WebSocket conn, Exception ex) {
        System.err.println("An error occurred: ");
        ex.printStackTrace();
    }

    private String escape(String message) {
        return message.replaceAll("[\r\n]", "\\\\n");
    }

    public void start() {
        setConnectionLostTimeout(0);
        stats = new Statistics();
        startTime = Instant.now();
    }

    public static void main(String[] args) {
        String HttpPort = System.getenv("HTTP_PORT");
        int port = 9080;
        if (HttpPort != null) {
            port = Integer.valueOf(HttpPort);
        }

        HttpServer H = new HttpServer(port, "./html");
        H.start();
        System.out.println("HTTP server started on port: " + port);

        port = 9880;
        String WSPort = System.getenv("WEBSOCKET_PORT");
        if (WSPort != null) {
            port = Integer.valueOf(WSPort);
        }

        WebSocket A = new WebSocket(port);
        A.setReuseAddr(true);
        A.start();
        System.out.println("WebSocket server started on port: " + port);
    }
}
