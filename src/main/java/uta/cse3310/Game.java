package uta.cse3310;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Game {
    public PlayerType[][] Button = new PlayerType[20][20];
    public int GameId;
    public String PlayerName;
    char[][] cells = new char[20][20];

    public PlayerType players = new ArrayList<>();  
    public ArrayList<String> sol = new ArrayList<>();
    public ArrayList<String> PlayerUserNames = new ArrayList<String>();
    public GameState state = GameState.LOBBY;
    public PlayerType player;
    public int playerNum;

    public Game() {
        ResetBoard();  // Initialization of 'Button' is now moved into 'ResetBoard'
    }

    public void Update(UserEvent U) {
        int i, j, k, l;
        String delim = "[,]+";
        for (String s : sol) {
            String[] arr = s.split(delim);
            i = Integer.valueOf(arr[0]);
            j = Integer.valueOf(arr[1]);
            k = Integer.valueOf(arr[2]);
            l = Integer.valueOf(arr[3]);

            if (U.i == i && U.j == j && U.k == k && U.l == l && Button[U.i][U.j] == PlayerType.NOPLAYER
                    && Button[U.k][U.l] == PlayerType.NOPLAYER) {
                horizontal(i, j, k, l, U.PlayerIdx);
            }
        }
    }

    public void horizontal(int i, int j, int k, int l, PlayerType PlayerIdx) {
        // logic for placing 'PlayerIdx' horizontally from (j,k) to (j,l)
        if (i > k && j == l) {
            for (int e = k; e <= i; e++) {
                Button[j][e] = PlayerIdx;
            }
        } else if (k > i && j == l) {
            for (int f = i; f <= k; f++) {
                Button[j][f] = PlayerIdx;
            }
        }
    }

    public void ResetBoard() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                Button[i][j] = PlayerType.NOPLAYER;
            }
        }
    }

    public void StartGame() {
        throw new UnsupportedOperationException("Unimplemented method 'StartGame'");
    }
}
