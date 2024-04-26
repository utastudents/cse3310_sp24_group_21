package uta.cse3310;

import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.util.Vector;

import java.time.Duration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uta.cse3310.Grid.GridGen;

public class App extends WebSocketServer {
  // All games currently underway on this server are stored in
  // the vector ActiveGames
  private Vector<Game> ActiveGames = new Vector<Game>();
  // Vector<Game> players = new Vector<Game>();
  List<String> words = new ArrayList<>();

  private int GameId = 1;
  private Statistics stats;
  private int connectionId = 0;
  private Instant TimeStart;
  // static class gameGrid {
  // int numAttempts;
  // char[][] cells = new char[15][15];
  // List<String> solutions = new ArrayList<>();
  // }

  public App(int port) {
    super(new InetSocketAddress(port));
  }

  public App(InetSocketAddress address) {
    super(address);
  }

  public App(int port, Draft_6455 draft) {
    super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {

    connectionId++;

    System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
    ServerEvent E = new ServerEvent();
    Game G = null;
    for (Game i : ActiveGames) {
      if (i.player == uta.cse3310.PlayerType.PLAYERONE ||
          i.player == uta.cse3310.PlayerType.PLAYERTWO ||
          i.player == uta.cse3310.PlayerType.PLAYERTHREE ||
          i.player == uta.cse3310.PlayerType.PLAYERFOUR

      ) {
        G = i;
        System.out.println("Found A Match");
      }
    }
    // If no Matches
    if (G == null) {
      G = new Game();
      G.GameId = GameId;
      GameId++;
      words = Grid.readWords(); // Provide the filename as argument to readWords
      GridGen gen = Grid.createGrid(words, 300); // Provide words list and maxWords as arguments
      G.cells = gen.cells;
      Grid.printResult(gen);
      G.players = PlayerType.PLAYERONE;
      ActiveGames.add(G);
      System.out.println(" Creating a new Game");
    } else {
      System.out.println(" Not a new game");
      G.players = PlayerType.values()[G.players.ordinal() + 1];
      G.StartGame();
    }
    E.YouAre = G.player;
    E.GameId = G.GameId;
    // allows the websocket to give us the Game when a message arrives
    conn.setAttachment(G);

    Gson gson = new Gson();

    // The state of the game has changed, so lets send it to everyone
    // Note only send to the single connection
    String jsonString = gson.toJson(E);
    conn.send(jsonString);
    System.out
        .println("> " + Duration.between(TimeStart, Instant.now()).toMillis() + " " + connectionId + " "
            + escape(jsonString));

    // The state of the game has changed, so lets send it to everyone
    jsonString = gson.toJson(G);
    System.out
        .println("< " + Duration.between(TimeStart, Instant.now()).toMillis() + " " + "*" + " " + escape(jsonString));
    broadcast(jsonString);

  }

  public class G {

  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println(conn + " has closed");
    // Retrieve the game tied to the websocket connection
    Game G = conn.getAttachment();
    G = null;
  }

  String type; // The chat message
  String text;
  String username;

  @Override
  public void onMessage(WebSocket conn, String message) {
    System.out
        .println("< " + Duration.between(TimeStart, Instant.now()).toMillis() + " " + "-" + " " + escape(message));

    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();

    try {
      UserEvent U = gson.fromJson(message, UserEvent.class);
      Game G = conn.getAttachment();
      G.Update(U);

      System.err.println("message: " + message + message.contains("username") + U.GameId + G.GameId);
      if (message.contains("username")) { // && U.GameId == G.GameId
        G.PlayerUserNames.add(U.username);
      }
      System.err.println(gson.toJson(U));
      if ("chat-messages".equals(U.type)) {
        // Chat message
        // Send the message to everyone
        System.err.println("a;sldkjf;alskdjf;laksdjf;lasdjkl;f");
        String chatMessageJson = gson.toJson(new UserEvent("chat", U.text, U.username));
        System.err.println("chat message: " + chatMessageJson);
        broadcast(chatMessageJson);
        System.err.println("chat message broadcasted");
        return;
      }

      String jsonString;
      jsonString = gson.toJson(G);
      System.out
          .println("> " + Duration.between(TimeStart, Instant.now()).toMillis() + " " + "*" + " " + escape(jsonString));
      broadcast(jsonString);
    } catch (Exception e) {
      System.err.println("The Message isn't a valid JSON: " + e);
    }

  }

  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    System.out.println(conn + ": " + message);

  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    ex.printStackTrace();
    if (conn != null) {
      // some errors like port binding failed may not be assignable to a specific
      // websocket
    }
  }

  @Override
  public void onStart() {
    System.out.println("Server started!");
    setConnectionLostTimeout(0);
    TimeStart = Instant.now();
  }

  private String escape(String S) {
    // turns " into \"
    String retval = new String();
    // this routine is very slow.
    // but it is not called very often
    for (int i = 0; i < S.length(); i++) {
      Character ch = S.charAt(i);
      if (ch == '\"') {
        retval = retval + '\\';
      }
      retval = retval + ch;
    }
    return retval;
  }

  public static void main(String[] args) {

    String HttpPort = System.getenv("HTTP_PORT");
    int port = 9080;
    if (HttpPort != null) {
      port = Integer.valueOf(HttpPort);
    }

    // Set up the http server

    HttpServer H = new HttpServer(port, "./html");
    H.start();
    System.out.println("http Server started on port: " + port);

    // create and start the websocket server

    port = 9180;
    String WSPort = System.getenv("WEBSOCKET_PORT");
    if (WSPort != null) {
      port = Integer.valueOf(WSPort);
    }

    App A = new App(port);
    A.setReuseAddr(true);
    A.start();
    System.out.println("websocket Server started on port: " + port);

  }
}