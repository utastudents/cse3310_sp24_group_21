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
        scores.add(new PlayerScore(playerName, score));
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
    }
}
