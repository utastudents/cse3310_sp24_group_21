package uta.cse3310;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderBoard {

    private List<PlayerScore> scores;

    public LeaderBoard() {
        this.scores = new ArrayList<>();
    }

    public void addScore(String playerName, int score) {
        // checcck if the player already exists in the leaderboard
        boolean playerExists = false;
        for (PlayerScore playerScore : scores) {
            if (playerScore.getPlayerName().equals(playerName)) {
                // if the player exists, update their score
                playerScore.setScore(playerScore.getScore() + score);
                playerExists = true;
                break;
            }
        }
        // if the player doesn't exist, add them to the leaderboard
        if (!playerExists) {
            scores.add(new PlayerScore(playerName, score));
        }
        // sort the leaderboard based on score (descending order)
        scores.sort(Comparator.comparing(PlayerScore::getScore).reversed());
    }

    public List<PlayerScore> getHighScores() {
        return scores;
    }

    public String getTopPlayerName() {
        if (!scores.isEmpty()) {
            return scores.get(0).getPlayerName();
        }
        return null;
    }

    public int getTopScore() {
        if (!scores.isEmpty()) {
            return scores.get(0).getScore();
        }
        return 0;
    }

    private static class PlayerScore {
        private String playerName;
        private int score;

        public PlayerScore(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}
