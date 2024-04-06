package uta.cse3310;



//import org.java_websocket.WebSocket;
//import org.java_websocket.handshake.ClientHandshake;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;

import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.time.Instant;
import java.time.Duration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class WebSocket extends WebSocketServer{
        //all games stored in vector ActiveGames
        private Vector<Game> ActiveGames = new Vector<Game>();
        private int GameId = 1;
        private Statistics stats;
        private Instant startTime;
        private int connectionId = 0;

        public App(int port) {
            super(new InetSocketAddress(port));
        }
        
        public App(InetSocketAddress address) {
            super(address);
          }
        
          public App(int port, Draft_6455 draft) {
            super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
          }

        
        public void on_open(WebSocket conn, ClientHandshake handshake) {
        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
        ServerEvent E = new ServerEvent();

        //Search for a game
        Game G = null;
        for(Game i : ActiveGames){
            if(i.Players ==PlayerType.PLAYERONE){
                G = i;
                System.out.println("Found a match");
            }
        }
        //if no game create new game
        if(G == null){
            G = new Game(stats);
            G.GameId = GameId;
            GameId++;
            //add first player
            G.Players = PlayerType.PLAYERONE;
            ActiveGames.add(G);
            System.out.println("creating new game");
        }else if(G.Players == PlayerType.PLAYERONE){
            System.out.println("not a new game");
            G.Players = PlayerType.PLAYERTWO;
            G.StartGame();
        }else if(G.Players == PlayerType.PLAYERTWO){
            G.Players = PlayerType.PLAYERTHREE;
            G.StartGame();
        }else if(G.Players == PlayerType.PLAYERTHREE){
            G.Players = PlayerType.PLAYERFOUR;
            G.StartGame();
        }

        //create event to go to only the new player
        E.YouAre = G.Players;
        E.GameId = G.GameId;

        // allows the websocket to give us the Game when a message arrives..
        // it stores a pointer to G, and will give that pointer back to us
        // when we ask for it
        conn.setAttachment(G);

        Gson gson = new Gson();
        // Note only send to the single connection
        String jsonString = gson.toJson(E);
        conn.send(jsonString);
        System.out.println("> " + Duration.between(startTime, Instant.now()).toMillis() + " " + connectionId + " "
                + escape(jsonString));

        // Update the running time
        stats.setRunningTime(Duration.between(startTime, Instant.now()).toSeconds());

        // The state of the game has changed, so lets send it to everyone
        jsonString = gson.toJson(G);
        System.out
            .println("< " + Duration.between(startTime, Instant.now()).toMillis() + " " + "*" + " " + escape(jsonString));
        broadcast(jsonString);
    }
    

    
    public void start() {
        setConnectionLostTimeout(0);
        stats = new Statistics();
        startTime = Instant.now();
    }    

    public void on_close(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println(conn + " has closed");
        // Retrieve the game tied to the websocket connection
        Game G = conn.getAttachment();
        G = null;
    }

    public void on_error() {

    }
    public static void main(String[] args) {

        String HttpPort = System.getenv("HTTP_PORT");
        int port = 9080;
        if (HttpPort!=null) {
          port = Integer.valueOf(HttpPort);
        }
    
        // Set up the http server
    
        HttpServer H = new HttpServer(port, "./html");
        H.start();
        System.out.println("http Server started on port: " + port);
    
        // create and start the websocket server
    
        port = 9880;
        String WSPort = System.getenv("WEBSOCKET_PORT");
        if (WSPort!=null) {
          port = Integer.valueOf(WSPort);
        }
    
        WebSocket A = new WebSocket(port);
        A.setReuseAddr(true);
        A.start();
        System.out.println("websocket Server started on port: " + port);
    
      }
}

