package uta.cse3310;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.io.File;

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
  Vector<Game> ActiveGames = new Vector<Game>();
  List<String> words = new ArrayList<>();
  LeaderBoard leaderBoard = new LeaderBoard(); // Single leaderboard instance for the server

  int GameId = 0;

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
    System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
    Game G = null;
    for (Game i : ActiveGames) {
      if (i.player == PlayerType.PLAYERONE) {
        G = i;
      }
    }

    if (G == null) {
      words = Grid.readWords();
      GridGen gen = Grid.createGrid(words, 300);
      G = new Game(leaderBoard); // Pass the leaderboard to the new game
      G.cells = gen.cells;
      G.sol = gen.sol;
      G.gameWords = gen.words;
      G.findword = gen.findword;
      GameId++;
      G.player = PlayerType.PLAYERONE;
      ActiveGames.add(G);
    } else {
      G.player = PlayerType.PLAYERTWO;
    }

    ServerEvent E = new ServerEvent();
    E.YouAre = G.player;
    E.GameId = G.GameId;
    conn.setAttachment(G);

    Gson gson = new Gson();
    String jsonString = gson.toJson(E);
    conn.send(jsonString);
    jsonString = gson.toJson(G);
    broadcast(jsonString);
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println(conn + " has closed");
    Game G = conn.getAttachment();
    G = null;
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    System.out.println(conn + ": " + message);
    Gson gson = new GsonBuilder().create();
    UserEvent U = gson.fromJson(message, UserEvent.class);
    Game game = conn.getAttachment();
    if (U.playing) {
      game.Update(U);
      broadcastLeaderboard(); // Update all clients with the latest leaderboard
    }
  }

  // Broadcast the current state of the leaderboard to all clients
  private void broadcastLeaderboard() {
    String leaderboardJson = new Gson().toJson(leaderBoard.getHighScores());
    broadcast(leaderboardJson);
  }

  @Override
  public void onStart() {
    System.out.println("Server started!");
    setConnectionLostTimeout(0);
  }

  public static void main(String[] args) {
    int port = 9021;
    HttpServer H = new HttpServer(port, "./html");
    H.start();
    System.out.println("HTTP server started on port: " + port);

    port = 9121;
    App A = new App(port);
    A.start();
    System.out.println("WebSocket server started on port: " + port);
  }
}
