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
    Vector<Game> players = new Vector<Game>();
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

      Game G = null;
       

      if (G == null){
        G = new Game();
        Lobby L = new Lobby();
        G = L.createGame();
        words = Grid.readWords("words.txt"); // Provide the filename as argument to readWords
        Grid.GridGen gen = Grid.createGrid(words, 250); // Provide words list and maxWords as arguments
        G.cells = gen.cells;
        Grid.printResult(gen);
    
        G.player = PlayerType.PLAYERONE;
    }
    

      // allows the websocket to give us the Game when a message arrives
      conn.setAttachment(G);
  
      Gson gson = new Gson();
      
  
      // The state of the game has changed, so lets send it to everyone
      String jsonString;

      jsonString = gson.toJson(G);
  
      System.out.println(jsonString);
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
      
          // if (U.Action.equals("JOIN_LOBBY")) {
              Game game = conn.getAttachment();
              // if (conn.getAttachment() != null) {
              //     game = (Game) conn.getAttachment();
              // } else {
              //     game = lobby.createGame();
              //     conn.setAttachment(game);
              // }
              lobby.joinGame(game, lobby.name);
              ServerEvent event = new ServerEvent();
              event.Game = game;
              event.PlayerName = lobby.name;
              event.Players = lobby.getPlayers(game);
              

                String jsonString;
                jsonString = gson.toJson(event);
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
      int port = 9021;
      HttpServer H = new HttpServer(port, "./html");
      H.start();
      System.out.println("http Server started on port:" + port);
  
      // create and start the websocket server
  
      port = 9121;
      App A = new App(port);
      A.start();
      System.out.println("websocket Server started on port: " + port);
    }
  }
  //  class Message {
  //     public String Action;
  //     public String PlayerName;
  //   }
    
  //   class ServerEvent {
  //     public Game Game;
  //     public String PlayerName;
  //     public String BottomMsg;
  //     public ArrayList<String> Players;
  //   }



