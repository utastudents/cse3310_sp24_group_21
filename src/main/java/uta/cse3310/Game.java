package uta.cse3310;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


// there are a lot of remnants from the tictactoe program incase they're needed later, will be removed otherwise
public class Game {

    // public PlayerType Players;
    // // public PlayerType CurrentTurn;
    public PlayerType[][] Button;
    // // public String[] Msg;
    public int GameId;    
    public String PlayerName;
    // public ArrayList<String> vnames = new ArrayList<String>();
    char[][] cells = new char[50][50];

    public ArrayList<String> players = new ArrayList<>();
    public GameState state = GameState.LOBBY;
    PlayerType player;
    public int playerNum;
    
    


    Game() {
        Button = new PlayerType[50][50];
        ResetBoard();
    }

    public void Update(UserEvent U) {        
        if (Button[U.i][U.j] == PlayerType.NOPLAYER) { 
            Button[U.i][U.j] = U.PlayerIdx;
        }  
    }
   
    public void ResetBoard() {
        // initializes the board to NOPLAYER in all spots
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++){
            Button[i][j] = PlayerType.NOPLAYER;
            }
        }
    }

    

    

   

    

    

    // public void Update(UserEvent U) {
    //     if ((CurrentTurn == U.PlayerIdx) && (CurrentTurn == PlayerType.PLAYERONE || CurrentTurn == PlayerType.PLAYERTWO)) {
    //                     // Move is legitimate, lets do what was requested
            
    //                     // Is the button not taken by X or O?
    //                     if (Button[U.Button] == PlayerType.NOPLAYER) {
    //                         System.out.println("the button was 0, setting it to" + U.PlayerIdx);
    //                         Button[U.Button] = U.PlayerIdx;
    //                         if (U.PlayerIdx == PlayerType.PLAYERTWO) {
    //                             CurrentTurn = PlayerType.PLAYERONE;
    //                             Msg[1] = "Other Players Move.";
    //                             Msg[0] = "Your Move.";
    //                         } else {
    //                             CurrentTurn = PlayerType.PLAYERTWO;
    //                             Msg[0] = "Other Players Move.";
    //                             Msg[1] = "Your Move.";
    //                         }
    //                     } else {
    //                         Msg[PlayerToIdx(U.PlayerIdx)] = "Not a legal move.";
    //                     }
    //                  }
                    
            
    // }

    
}





    
    

    

   
    
   