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
    public ArrayList<String> scores = new ArrayList<>();
    public GameState state = GameState.LOBBY;
    PlayerType player;
    public int playerNum;
    boolean start;
    Game() {
        Button = new PlayerType[20][20];
        ResetBoard();
    }
    public void Update(UserEvent U,Game game) {
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
                        LeaderBoard board = new LeaderBoard();
                        if (horizontal(i, j, k, l, U.PlayerIdx) != 0) 
                            board.addScore(U.PlayerIdx,game,horizontal(i, j, k, l, U.PlayerIdx));
                        if (vertical(i, j, k, l, U.PlayerIdx) != 0)
                        board.addScore(U.PlayerIdx,game,vertical(i, j, k, l, U.PlayerIdx));
                        if (diagonal(i, j, k, l, U.PlayerIdx) != 0)
                        board.addScore(U.PlayerIdx,game,diagonal(i, j, k, l, U.PlayerIdx));
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
    public int diagonal(int i, int j, int k, int l, PlayerType PlayerIdx) {
        if (i < k && j > l) {
            for (int a = i, b = j; a <= k;) {
                Button[b][a] = PlayerIdx;
                a++;
                b--;
            }return k-i+1;
        } else if (i > k && j > l) {
            for (int a = i, b = j; a >= k;) {
                Button[b][a] = PlayerIdx;
                a--;
                b--;
            }return i-k+1;
        } else if (i < k && j < l) {
            for (int a = i, b = j; a <= k;) {
                Button[b][a] = PlayerIdx;
                a++;
                b++;
            }return k-i+1;
        } else if (i > k && j < l) {
            for (int a = i, b = j; a >= k;) {
                Button[b][a] = PlayerIdx;
                a--;
                b++;
            } return i-k+1;
        }return 0;
    }
    public int vertical(int i, int j, int k, int l, PlayerType PlayerIdx) {
        if (j > l && i == k) {
            for (int e = l; e <= j; e++) {
                Button[e][i] = PlayerIdx;
            }return j-l+1;
        } else if (l > j && i == k) {
            for (int f = j; f <= l; f++) {
                Button[f][i] = PlayerIdx;
            }return l-j+1;
        }return 0;
    }
    public int horizontal(int i, int j, int k, int l, PlayerType PlayerIdx) {
        // horizontal word
        if (i > k && j == l) {
            for (int e = k; e <= i; e++) {
                Button[j][e] = PlayerIdx;
            } return i-k+1;
        } else if (k > i && j == l) {
            for (int f = i; f <= k; f++) {
                Button[j][f] = PlayerIdx;
            } return k-i+1;
        }return 0;
    }
    public void ResetBoard() {
        // initializes the board to NOPLAYER in all spots
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                Button[i][j] = PlayerType.NOPLAYER;
            }
        }
    }
      //public void selectWord(String selectedWord, PlayerType playerType) {
        //System.out.println("Selected word: " + selectedWord);
        // Check if the selected word is valid
       // if (isWordValid(selectedWord)) {
            // Calculate points based on the length of the word
           // int wordLength = selectedWord.length();
            //int points = calculatePoints(wordLength);
            //System.out.println("Points earned: " + points);
            // Add points to the player's score in the leaderboard
           // addToLeaderboard(playerType.toString(), points);
        //}
    //}



    //LEADERBOARD
    //private boolean isWordValid(String selectedWord) {
        // Check if the selected word is present in the word bank
       // boolean isWordInWordBank = gameWords.contains(selectedWord);
        
        // Check if the selected word has not already been found (crossed out)
       // boolean isWordNotAlreadyFound = !foundwords.contains(selectedWord);
        
        // Return true only if the word is in the word bank, has not already been found,
        // and meets the Wordle game rules (e.g., matches the length of the target word)
        //return isWordInWordBank && isWordNotAlreadyFound && matchesTargetWord(selectedWord);
    //}
    
    //private boolean matchesTargetWord(String selectedWord) {
        // Check if the selected word matches the target word (for simplicity, let's assume a fixed target word)
        //String targetWord = gameWords.get(0); // Assuming the first word in the word bank is the target word
        //return selectedWord.equalsIgnoreCase(targetWord);
    //}
    
    //private int calculatePoints(int wordLength) {
        // Points can be calculated based on the length of the word
        //return wordLength;
//}
    
    //private void addToLeaderboard(String playerName, int points) {
        // Add the points to the player's score in the leaderboard
       // leaderBoard.addScore(playerName, points);
    //}
    
    
    
}
