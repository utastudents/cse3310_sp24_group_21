package uta.cse3310;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public int winsbyPlayer1;
    public int winsbyPlayer2;
    public int winsbyPlayer3;
    public int winsbyPlayer4;
    public int PlayerOnePoints;
    public int PlayerTwoPoints;
    public int PlayerThreePoints;
    public int PlayerFourPoints;


    private Map<String, Integer> leaderboard = new HashMap<>();
    public void setPlayerOnePoints(int points) {
        this.PlayerOnePoints = points;
    }
    
    public void setPlayerTwoPoints(int points) {
        this.PlayerTwoPoints = points;
    }
    
    public void setPlayerThreePoints(int points) {
        this.PlayerThreePoints = points;
    }
    
    public void setPlayerFourPoints(int points) {
        this.PlayerFourPoints = points;
    }
    public int getPlayerOnePoints() {
        return PlayerOnePoints;
    }
    
    public int getPlayerTwoPoints() {
        return PlayerTwoPoints;
    }
    
    public int getPlayerThreePoints() {
        return PlayerThreePoints;
    }
    
    public int getPlayerFourPoints() {
        return PlayerFourPoints;
    }
    
    PlayerType player;
    public int playerNum;
    boolean start;

    Game() {
        Button = new PlayerType[20][20];
        ResetBoard();
        leaderboard.put(PlayerType.PLAYERONE.toString(), 0);
        leaderboard.put(PlayerType.PLAYERTWO.toString(), 0);
        leaderboard.put(PlayerType.PLAYERTHREE.toString(), 0);
        leaderboard.put(PlayerType.PLAYERFOUR.toString(), 0);
        
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


    public void selectWord(String selectedWord, PlayerType playerType) {
        System.out.println("Selected word: " + selectedWord);
        // Check if the selected word is valid
        if (isWordValid(selectedWord)) {
            // Calculate points based on the length of the word
            int wordLength = selectedWord.length();
            int points = calculatePoints(wordLength);
            System.out.println("Points earned: " + points);
            // Add points to the player's score in the leaderboard
            addToLeaderboard(playerType.toString(), points);
        }
    }

    private boolean isWordValid(String selectedWord) {
        String selectedWordUpperCase = selectedWord.toUpperCase();
        for (String word : gameWords) {
            if (word.equals(selectedWordUpperCase)) {
                return true; // The word is valid
            }
        }
        return false; // The word is not valid
    }
    
    

    private int calculatePoints(int wordLength) {
        // Points can be calculated based on the length of the word
        return wordLength;
    }

    private void addToLeaderboard(String playerName, int points) {
        // Add the points to the player's score in the leaderboard
        leaderboard.put(playerName, leaderboard.getOrDefault(playerName, 0) + points);
        // Optionally, you can print or display the updated leaderboard
        displayLeaderboard();
    }

    private void displayLeaderboard() {
        System.out.println("Leaderboard:");
        for (String playerName : leaderboard.keySet()) {
            int score = leaderboard.get(playerName);
            System.out.println(playerName + ": " + score);
        }
    }

    

}
