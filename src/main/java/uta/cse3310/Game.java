package uta.cse3310;

import java.util.ArrayList;

public class Game {

    public PlayerType[][] Button;
    public int GameId;
    public String PlayerName;
    char[][] cells = new char[20][20];

    public ArrayList<String> players = new ArrayList<>();
    public ArrayList<String> sol = new ArrayList<>();
    public ArrayList<String> gameWords = new ArrayList<>();
    public ArrayList<String> findword = new ArrayList<>();
    public ArrayList<String> foundwords = new ArrayList<>();
    public ArrayList<String> chat = new ArrayList<>();
    public ArrayList<String> chatplayers = new ArrayList<>();
    public GameState state = GameState.LOBBY;
    PlayerType player;
    public int playerNum;
    boolean start;

    Game() {
        Button = new PlayerType[20][20];
        ResetBoard();
    }

    public void Update(UserEvent U) {
        int i, j, k, l;
        String delim = "[,]+";
        for (String s : sol) {
            String[] arr = s.split(delim);
            String a = arr[0];
            String b = arr[1];
            String c = arr[2];
            String d = arr[3];
            i = Integer.valueOf(a);
            j = Integer.valueOf(b);
            k = Integer.valueOf(c);
            l = Integer.valueOf(d);

            if (U.i == i && U.j == j && U.k == k && U.l == l && Button[U.i][U.j] == PlayerType.NOPLAYER
                    && Button[U.k][U.l] == PlayerType.NOPLAYER) {
                horizontal(i, j, k, l, U.PlayerIdx);
                vertical(i, j, k, l, U.PlayerIdx);
                diagonal(i, j, k, l, U.PlayerIdx);
                for (String words : findword) {
                    String[] word = words.split(delim);
                    String removedWord = word[0];
                    String e = word[1];
                    String f = word[2];
                    String g = word[3];
                    String h = word[4];
                    int m = Integer.valueOf(e);
                    int n = Integer.valueOf(f);
                    int o = Integer.valueOf(g);
                    int p = Integer.valueOf(h);
                    if (m == i && n == j && o == k && p == l) {
                        foundwords.add(removedWord);
                    }
                }
            }
        }
    }

    public void diagonal(int i, int j, int k, int l, PlayerType PlayerIdx) {
        if (i < k && j > l) {
            for (int a = i, b = j; a <= k;) {
                Button[b][a] = PlayerIdx;
                a++;
                b--;
            }
        } else if (i > k && j > l) {
            for (int a = i, b = j; a >= k;) {
                Button[b][a] = PlayerIdx;
                a--;
                b--;
            }
        } else if (i < k && j < l) {
            for (int a = i, b = j; a <= k;) {
                Button[b][a] = PlayerIdx;
                a++;
                b++;
            }
        } else if (i > k && j < l) {
            for (int a = i, b = j; a >= k;) {
                Button[b][a] = PlayerIdx;
                a--;
                b++;
            }
        }
    }

    public void vertical(int i, int j, int k, int l, PlayerType PlayerIdx) {
        if (j > l && i == k) {
            for (int e = l; e <= j; e++) {
                Button[e][i] = PlayerIdx;
            }
        } else if (l > j && i == k) {
            for (int f = j; f <= l; f++) {
                Button[f][i] = PlayerIdx;
            }
        }
    }

    public void horizontal(int i, int j, int k, int l, PlayerType PlayerIdx) {
        // horizontal word
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
        // initializes the board to NOPLAYER in all spots
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                Button[i][j] = PlayerType.NOPLAYER;
            }
        }
    }

    // public void Update(UserEvent U) {
    // if ((CurrentTurn == U.PlayerIdx) && (CurrentTurn == PlayerType.PLAYERONE ||
    // CurrentTurn == PlayerType.PLAYERTWO)) {
    // // Move is legitimate, lets do what was requested

    // // Is the button not taken by X or O?
    // if (Button[U.Button] == PlayerType.NOPLAYER) {
    // System.out.println("the button was 0, setting it to" + U.PlayerIdx);
    // Button[U.Button] = U.PlayerIdx;
    // if (U.PlayerIdx == PlayerType.PLAYERTWO) {
    // CurrentTurn = PlayerType.PLAYERONE;
    // Msg[1] = "Other Players Move.";
    // Msg[0] = "Your Move.";
    // } else {
    // CurrentTurn = PlayerType.PLAYERTWO;
    // Msg[0] = "Other Players Move.";
    // Msg[1] = "Your Move.";
    // }
    // } else {
    // Msg[PlayerToIdx(U.PlayerIdx)] = "Not a legal move.";
    // }
    // }

    // }

}
