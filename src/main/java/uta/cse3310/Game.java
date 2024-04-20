package uta.cse3310;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


// there are a lot of remnants from the tictactoe program incase they're needed later, will be removed otherwise
public class Game {

    // public PlayerType Players;
    // // public PlayerType CurrentTurn;
    // // public PlayerType[] Button;
    // // public String[] Msg;
    // public int GameId;    
    // public String names;
    // public ArrayList<String> vnames = new ArrayList<String>();
    char[][] cells = new char[15][15];

    public ArrayList<String> players = new ArrayList<>();
    public GameState state = GameState.LOBBY;
    PlayerType player;
    public int playerNum;
    
    

    //public Statistics Stats;

    // Game(/*Statistics s*/) {
    //     // Stats = s;
    //     // Button = new PlayerType[9];
    //     // // initialize it
    //     // ResetGrid();
    //     // Players = PlayerType.PLAYERONE;
    //     // CurrentTurn = PlayerType.NOPLAYER;
    //     // Msg = new String[4];//2
    //     // Msg[0] = "Waiting for other player to join";
    //     // Msg[1] = "";
    //     // Msg[2] = "";
    //     // Msg[3] = "";
    //     // playerNum=0;

    // }
    // public void addPlayer(String name){
    //     vnames.add(name);
    //     playerNum++;
    // }
    // public void ResetGrid() {
    //     // initializes the board to NOPLAYER in all spots
    //     for (int i = 0; i < Button.length; i++) {
    //         Button[i] = PlayerType.NOPLAYER;
            
    //     }
    // }

    // public void StartGame() {
    //     // Msg[0] = "Player1";
    //     // Msg[1] = "Player2";
    //     // Msg[2] = "Player3";
    //     // Msg[3] = "Player4";
    //     // CurrentTurn = PlayerType.PLAYERONE;
    //     // Stats.setGamesInProgress(Stats.getGamesInProgress() + 1);
    // }

    // private boolean CheckLine(int i, int j, int k, PlayerType player) {
    //     return player == Button[i] && player == Button[j] && player == Button[k];
    // }

    // private boolean CheckHorizontal(PlayerType player) {
    //     return CheckLine(0, 1, 2, player) || CheckLine(3, 4, 5, player) || CheckLine(6, 7, 8, player);
    // }

    // private boolean CheckVertical(PlayerType player) {
    //     return CheckLine(0, 3, 6, player) || CheckLine(1, 4, 7, player) || CheckLine(2, 5, 8, player);
    // }

    // private boolean CheckDiagonal(PlayerType player) {
    //     return CheckLine(0, 4, 8, player) || CheckLine(2, 4, 6, player);
    // }

    // public boolean CheckBoard(PlayerType player) {
    //     return CheckHorizontal(player) || CheckVertical(player) || CheckDiagonal(player);
    // }

    // public boolean CheckDraw(PlayerType player) {
        // It is a draw if neither player has won.
    //     boolean retval = false;

    //     return retval;
    // }

    // This function returns an index for each player
    // It does not depend on the representation of Enums
    // public int PlayerToIdx(PlayerType P) {
    //     int retval = 0;
    //     //add idx for player 3,4
    //     if (P == PlayerType.PLAYERONE) {
    //         retval = 0;
    //     } else {
    //         retval = 1;
    //     }
    //     return retval;
    // }

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

    // public void GameTime() {
    //     // this function can be called periodically if a
    //     // timer is needed.

    // }
}





    
    

    

   
    
   