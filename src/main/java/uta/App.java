package uta.cse3310;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class App extends WebSocketServer {
  private Lobby lobby = new Lobby();

  public App(int port) {
      super(new InetSocketAddress(port));
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
      System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
      System.out.println(conn + " has closed");
  }

  @Override
public void onMessage(WebSocket conn, String message) {
    System.out.println(conn + ": " + message);
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    Message msg = gson.fromJson(message, Message.class);

    if (msg.Action.equals("JOIN_LOBBY")) {
        Game game;
        if (conn.getAttachment() != null) {
            game = (Game) conn.getAttachment();
        } else {
            game = lobby.createGame();
            conn.setAttachment(game);
        }
        lobby.joinGame(game, msg.PlayerName);
        ServerEvent event = new ServerEvent();
        event.Game = game;
        event.PlayerName = msg.PlayerName;
        event.Players = lobby.getPlayers(game);
        conn.send(gson.toJson(event));
        broadcast(gson.toJson(event));
    } else if (msg.Action.equals("START_GAME")) {
        Game game = (Game) conn.getAttachment();
        if (lobby.startGame(game)) {
            ServerEvent event = new ServerEvent();
            event.Game = game;
            event.BottomMsg = "Game started!";
            event.Players = lobby.getPlayers(game);
            broadcast(gson.toJson(event));
        }
    }
}

  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
      System.out.println(conn + ": " + message);
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
      ex.printStackTrace();
  }

  @Override
  public void onStart() {
      System.out.println("Server started!");
      setConnectionLostTimeout(0);
  }

  public static void main(String[] args) {
     // Set up the http server
     int port = 9081;
     HttpServer H = new HttpServer(port, "./html");
     H.start();
     System.out.println("http Server started on port:" + port);
 
     // create and start the websocket server
 
     port = 9881;
     App A = new App(port);
     A.start();
     System.out.println("websocket Server started on port: " + port);
  }
}

class Message {
  public String Action;
  public String PlayerName;
}

class ServerEvent {
  public Game Game;
  public String PlayerName;
  public String BottomMsg;
  public List<String> Players;
}
