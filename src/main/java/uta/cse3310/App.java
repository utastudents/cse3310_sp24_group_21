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

import uta.cse3310.Grid.GridGen;


public class App extends WebSocketServer {
    // All games currently underway on this server are stored in
    // the vector ActiveGames
    Vector<Game> ActiveGames = new Vector<Game>();
    // Vector<Game> players = new Vector<Game>();
    List<String> words = new ArrayList<>();

    int GameId = 0;
    // static class gameGrid {
    //   int numAttempts;
    //   char[][] cells = new char[15][15];
    //   List<String> solutions = new ArrayList<>();
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
  
      System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
      ServerEvent E = new ServerEvent();
      Game G = null;
      for(Game i : ActiveGames) {
        if (i.player == uta.cse3310.PlayerType.PLAYERONE){
          G = i;
        }
      }
       

      if (G == null){
        G = new Game();
        // Lobby L = new Lobby();
        // G = L.createGame();
        words = Grid.readWords(); // Provide the filename as argument to readWords
        GridGen gen = Grid.createGrid(words, 40); // Provide words list and maxWords as arguments
        G.cells = gen.cells;
        Grid.printResult(gen);
        G.sol = gen.sol;
        GameId++;
        G.player = uta.cse3310.PlayerType.PLAYERONE;
        ActiveGames.add(G);
    }else {
      G.player = uta.cse3310.PlayerType.PLAYERTWO;
    }
    E.YouAre = G.player;
    E.GameId = G.GameId;
      // allows the websocket to give us the Game when a message arrives
      conn.setAttachment(G);
  
      Gson gson = new Gson();
      
  
      // The state of the game has changed, so lets send it to everyone
      String jsonString;
      jsonString = gson.toJson(E);
      conn.send(jsonString);
      jsonString = gson.toJson(G);
  
      // System.out.println(jsonString);
      broadcast(jsonString);
    }
  
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
      System.out.println(conn + " has closed");
      // Retrieve the game tied to the websocket connection
      Game G = conn.getAttachment();
      G = null;
    }
  
    @Override
    public void onMessage(WebSocket conn, String message) {
      System.out.println(conn + ": " + message);

      // Bring in the data from the webpage
      // A UserEvent is all that is allowed at this point
      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      
      Lobby lobby = gson.fromJson(message, Lobby.class);
      UserEvent U = gson.fromJson(message, UserEvent.class);
              Game game = conn.getAttachment();
              if (U.playing == true)
              game.Update(U);

              lobby.joinGame(game, lobby.name);
              game.PlayerName = lobby.name;
              
                String jsonString;
                jsonString = gson.toJson(game);
                System.out.println(jsonString);
                broadcast(jsonString);
      
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
    }
  
    
  
    public static void main(String[] args) {
  
      // Set up the http server
      int port = 9021;//9021
      HttpServer H = new HttpServer(port, "./html");
      H.start();
      System.out.println("http Server started on port:" + port);
  
      // create and start the websocket server
  
      port = 9121;//9121
      App A = new App(port);
      A.start();
      System.out.println("websocket Server started on port: " + port);
    }
  }
