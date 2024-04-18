package uta.cse3310;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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

import uta.cse3310.Grid.GridGen;


public class WebSockets extends WebSocketServer {
    // All games currently underway on this server are stored in
    // the vector ActiveGames
    Vector<Game> ActiveGames = new Vector<Game>();
    Vector<Game> players = new Vector<Game>();

    int GameId = 0;
    // static class gameGrid {
    //   int numAttempts;
    //   char[][] cells = new char[15][15];
    //   List<String> solutions = new ArrayList<>();
    // }

    
    
    
  
    public WebSockets(int port) {
      super(new InetSocketAddress(port));
    }
  
    public WebSockets(InetSocketAddress address) {
      super(address);
    }
  
    public WebSockets(int port, Draft_6455 draft) {
      super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }
  
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
  
      System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
      // gameGrid grid = null;
      Game G;
      Lobby L = new Lobby();
      G = L.createGame();
      List<String> words = new ArrayList<>();
      words = Grid.readWords("words.txt"); 
      GridGen grid = Grid.createGrid(words);

     
  
      // No matches ? Create a new Game.
    //   if (G == null) {
    //     G = new Game();
    //     G.GameId = GameId;
    //     GameId++;
    //     // Add the first player
    //     G.Players = uta.cse3310.PlayerType.PLAYERONE;
    //     ActiveGames.add(G);
    //     System.out.println(" creating a new Game");
    //   }else if(G.Players == uta.cse3310.PlayerType.PLAYERONE){
    //     System.out.println("not a new game");
    //     G.Players = uta.cse3310.PlayerType.PLAYERTWO;
    //     //G.StartGame();
    // }else if(G.Players == uta.cse3310.PlayerType.PLAYERTWO){
    //     G.Players = uta.cse3310.PlayerType.PLAYERTHREE;
    // }else if(G.Players == uta.cse3310.PlayerType.PLAYERTHREE){
    //     G.Players = uta.cse3310.PlayerType.PLAYERFOUR;
    // }
    // G.StartGame();
      // } else {
      //   // join an existing game
      //   System.out.println(" not a new game");
      //   G.Players = uta.cse3310.PlayerType.PLAYERTWO;
      //   G.StartGame();
      // }
      // create an event to go to only the new player
      // E.YouAre = G.Players;
      // E.GameId = G.GameId;

      
      // allows the websocket to give us the Game when a message arrives
      conn.setAttachment(G);
  
      Gson gson = new Gson();
      // Note only send to the single connection
      // conn.send(gson.toJson(E));
      // System.out.println(gson.toJson(E));
  
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
      // UserEvent U = gson.fromJson(message, UserEvent.class);
      // Message msg = gson.fromJson(message, Message.class);
      Lobby lobby = gson.fromJson(message, Lobby.class);

      

      // String jsonString;
      // jsonString = gson.toJson(G);
      
      // System.out.println(jsonString);
      // broadcast(jsonString);
      
  
      // System.out.println(conn + ": " + message);
      //     GsonBuilder builder = new GsonBuilder();
      //     Gson gson = builder.create();
      //     UserEvent U = gson.fromJson(message, UserEvent.class);
      
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
              // conn.send(gson.toJson(event));
              // broadcast(gson.toJson(event));

                String jsonString;
                jsonString = gson.toJson(event);
                System.out.println(jsonString);
                broadcast(jsonString);

      //     } else if (msg.Action.equals("START_GAME")) {
      //         Game game = (Game) conn.getAttachment();
      //         if (lobby.startGame(game)) {
      //             ServerEvent event = new ServerEvent();
      //             event.Game = game;
      //             event.BottomMsg = "Game started!";
      //             event.Players = lobby.getPlayers(game);
      //             broadcast(gson.toJson(event));
      //         }
      //     }
      
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
      int port = 9080;
      HttpServer H = new HttpServer(port, "./html");
      H.start();
      System.out.println("http Server started on port:" + port);
  
      // create and start the websocket server
  
      port = 9880;
      WebSockets A = new WebSockets(port);
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

// package uta.cse3310;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.net.InetSocketAddress;
// import java.net.UnknownHostException;
// import java.nio.ByteBuffer;
// import java.util.Collections;
// import java.util.List;

// import org.java_websocket.WebSocket;
// import org.java_websocket.drafts.Draft;
// import org.java_websocket.drafts.Draft_6455;
// import org.java_websocket.handshake.ClientHandshake;
// import org.java_websocket.server.WebSocketServer;
// import java.util.Timer;
// import java.util.TimerTask;
// import java.util.Vector;
// import com.google.gson.Gson;
// import com.google.gson.GsonBuilder;

// public class App extends WebSocketServer {
//   private Lobby lobby = new Lobby();

//   public App(int port) {
//       super(new InetSocketAddress(port));
//   }

//   @Override
//   public void onOpen(WebSocket conn, ClientHandshake handshake) {
//       System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
//   }

//   @Override
//   public void onClose(WebSocket conn, int code, String reason, boolean remote) {
//       System.out.println(conn + " has closed");
//   }

//   @Override
// public void onMessage(WebSocket conn, String message) {
//     System.out.println(conn + ": " + message);
//     GsonBuilder builder = new GsonBuilder();
//     Gson gson = builder.create();
//     Message msg = gson.fromJson(message, Message.class);

//     if (msg.Action.equals("JOIN_LOBBY")) {
//         Game game;
//         if (conn.getAttachment() != null) {
//             game = (Game) conn.getAttachment();
//         } else {
//             game = lobby.createGame();
//             conn.setAttachment(game);
//         }
//         lobby.joinGame(game, msg.PlayerName);
//         ServerEvent event = new ServerEvent();
//         event.Game = game;
//         event.PlayerName = msg.PlayerName;
//         event.Players = lobby.getPlayers(game);
//         conn.send(gson.toJson(event));
//         broadcast(gson.toJson(event));
//     } else if (msg.Action.equals("START_GAME")) {
//         Game game = (Game) conn.getAttachment();
//         if (lobby.startGame(game)) {
//             ServerEvent event = new ServerEvent();
//             event.Game = game;
//             event.BottomMsg = "Game started!";
//             event.Players = lobby.getPlayers(game);
//             broadcast(gson.toJson(event));
//         }
//     }
// }

//   @Override
//   public void onMessage(WebSocket conn, ByteBuffer message) {
//       System.out.println(conn + ": " + message);
//   }

//   @Override
//   public void onError(WebSocket conn, Exception ex) {
//       ex.printStackTrace();
//   }

//   @Override
//   public void onStart() {
//       System.out.println("Server started!");
//       setConnectionLostTimeout(0);
//   }

//   public static void main(String[] args) {
//      // Set up the http server
//      int port = 9081;
//      HttpServer H = new HttpServer(port, "./html");
//      H.start();
//      System.out.println("http Server started on port:" + port);
 
//      // create and start the websocket server
 
//      port = 9881;
//      App A = new App(port);
//      A.start();
//      System.out.println("websocket Server started on port: " + port);
//   }
// }

// class Message {
//   public String Action;
//   public String PlayerName;
// }

// class ServerEvent {
//   public Game Game;
//   public String PlayerName;
//   public String BottomMsg;
//   public List<String> Players;
// }