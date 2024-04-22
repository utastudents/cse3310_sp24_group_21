package uta.cse3310;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game {

    private PlayerType[] players;
    private int currentPlayerIndex;
    private String[] messages;
    private int gameId;
    private ArrayList<String> playerNames = new ArrayList<>();
    private char[][] grid = new char[50][50];  
    private GameState state;
    private Statistics stats;
    private HashMap<String, Boolean> wordList; 
    private int[] scores;
    private Grid gameGrid; 

    public Game(Statistics stats) {
        this.stats = stats;
        this.players = new PlayerType[4];
        this.messages = new String[4];
        this.scores = new int[4];
        this.gameGrid = new Grid();  
        this.state = GameState.LOBBY;
        currentPlayerIndex = 0;
        initializeGameGrid();
    }

    private void initializeGameGrid() {
        this.gameGrid.createGrid(Grid.readWords("words.txt"), 250);  

    public void addPlayer(String name) {
        if (playerNames.size() < 4) {
            playerNames.add(name);
            messages[playerNames.size() - 1] = "Welcome, " + name;
        }
    }

    public int getNumberOfPlayers() {
        return playerNames.size();
    }

    public void startGame() {
        fillGridWithRandomLetters();
        this.state = GameState.PLAYING;
        stats.setGamesInProgress(stats.getGamesInProgress() + 1);
    }

    private void fillGridWithRandomLetters() {
        Random rand = new Random();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = (char) ('A' + rand.nextInt(26));
            }
        }
    }

    public void displayBoard() {
        gameGrid.displayGrid();  
    }

    public boolean isWordValid(String word) {
        return wordList.containsKey(word) && wordList.get(word);
    }

    public void update(UserEvent event) {
        if (state == GameState.PLAYING) {
            String word = event.getWord().toUpperCase();
            if (isWordValid(word)) {
                messages[currentPlayerIndex] = "Valid word: " + word;
                scores[currentPlayerIndex] += word.length();
                wordList.put(word, false);
                if (checkRemainingWords() == 0) {
                    endGame();
                } else {
                    currentPlayerIndex = (currentPlayerIndex + 1) % playerNames.size();
                }
            } else {
                messages[currentPlayerIndex] = "Invalid word, try again!";
            }
        }
    }

    private int checkRemainingWords() {
        int remainingWords = 0;
        for (Boolean available : wordList.values()) {
            if (available) remainingWords++;
        }
        return remainingWords;
    }

    private void endGame() {
        int maxScore = 0;
        String winner = "";
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                winner = playerNames.get(i);
            }
        }
        state = GameState.FINISHED;
        for (int i = 0; i < messages.length; i++) {
            messages[i] = "Game over. Winner: " + winner + " with " + maxScore + " points!";
        }
    }

    public String getCurrentPlayerName() {
        return playerNames.get(currentPlayerIndex);
    }

    public boolean checkWin() {
        return state == GameState.FINISHED;
    }

    public boolean checkDraw() {
        return false;
    }

    public void setState(GameState newState) {
        this.state = newState;
    }

    public GameState getState() {
        return state;
    }
}


    
    

    

   
    
   
