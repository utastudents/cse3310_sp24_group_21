package uta.cse3310;

import java.util.HashMap;
import java.util.Map;

public class LeaderBoard {
    // Map to store player names and their corresponding high scores
    private Map<String, Integer> playerHighScores;

    // Constructor to initialize the leaderboard
    public LeaderBoard() {
        playerHighScores = new HashMap<>();
    }

    // Method to set the high score for a player
    // Parameters:
    //   - playerName: The name of the player
    //   - score: The high score achieved by the player
    public void setPlayerHighScore(String playerName, int score) {
        playerHighScores.put(playerName, score);
    }

    // Method to retrieve the high score for a player
    // Parameters:
    //   - playerName: The name of the player
    // Returns:
    //   - The high score of the player, or 0 if the player is not found
    public int getPlayerHighScore(String playerName) {
        return playerHighScores.getOrDefault(playerName, 0);
    }
    public void displayLeaderBoard() {
        System.out.println("Leaderboard:");
        if (playerHighScores.isEmpty()) {
            System.out.println("No players in the leaderboard.");
        } else {
            for (Map.Entry<String, Integer> entry : playerHighScores.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }
}
