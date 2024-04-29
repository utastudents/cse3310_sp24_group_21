package uta.cse3310;

import java.util.*;
import java.util.stream.*;

public class Statistics {
    private Integer winsbyPlayer1;
    private Integer winsbyPlayer2;
    private Integer winsbyPlayer3;
    private Integer winsbyPlayer4;
    private Integer tieGames;
    private Integer activeGames;
    private Integer scoreTotal;
    private Integer rankofPlayer1;
    private Integer rankofPlayer2;
    private Integer rankofPlayer3;
    private Integer rankofPlayer4;

    public Statistics() {
        winsbyPlayer1 = 0;
        winsbyPlayer2 = 0;
        winsbyPlayer3 = 0;
        winsbyPlayer4 = 0;
        tieGames = 0;
        activeGames = 0;
        rankofPlayer1 = 0;
        rankofPlayer2 = 0;
        rankofPlayer3 = 0;
        rankofPlayer4 = 0;
        scoreTotal = 0;
    }

    public Integer getWinsForPlayer(PlayerType player) {
        switch (player) {
            case PLAYERONE: return winsbyPlayer1;
            case PLAYERTWO: return winsbyPlayer2;
            case PLAYERTHREE: return winsbyPlayer3;
            case PLAYERFOUR: return winsbyPlayer4;
            default: return 0;
        }
    }

    public void setWinsForPlayer(PlayerType player, Integer wins) {
        switch (player) {
            case PLAYERONE: winsbyPlayer1 = wins; break;
            case PLAYERTWO: winsbyPlayer2 = wins; break;
            case PLAYERTHREE: winsbyPlayer3 = wins; break;
            case PLAYERFOUR: winsbyPlayer4 = wins; break;
            default: break;
        }
    }

    public Integer getTieGames() {
        return tieGames;
    }

    public void setTieGames(Integer ties) {
        tieGames = ties;
    }

    public Integer getActiveGames() {
        return activeGames;
    }

    public void setActiveGames(Integer active) {
        activeGames = active;
    }

    public Integer getRankForPlayer(PlayerType player) {
        switch (player) {
            case PLAYERONE: return rankofPlayer1;
            case PLAYERTWO: return rankofPlayer2;
            case PLAYERTHREE: return rankofPlayer3;
            case PLAYERFOUR: return rankofPlayer4;
            default: return 0;
        }
    }

    public void setScoreTotal(Integer score) {
        scoreTotal = score;
        updatePlayerRanks();
    }

    private void updatePlayerRanks() {
        Map<PlayerType, Integer> ranks = new TreeMap<>();
        ranks.put(PlayerType.PLAYERONE, winsbyPlayer1);
        ranks.put(PlayerType.PLAYERTWO, winsbyPlayer2);
        ranks.put(PlayerType.PLAYERTHREE, winsbyPlayer3);
        ranks.put(PlayerType.PLAYERFOUR, winsbyPlayer4);

        // Sort players by wins descending
        LinkedHashMap<PlayerType, Integer> sortedRanks = ranks.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        // Update ranks based on sorted order
        int rank = 1;
        for (PlayerType player : sortedRanks.keySet()) {
            switch (player) {
                case PLAYERONE: rankofPlayer1 = rank++; break;
                case PLAYERTWO: rankofPlayer2 = rank++; break;
                case PLAYERTHREE: rankofPlayer3 = rank++; break;
                case PLAYERFOUR: rankofPlayer4 = rank++; break;
                default: break;
            }
        }
    }

    public String displayLeaderBoard() {
        String leaderboard = "Leaderboard:\nPlayer\tWins\tRank\n";
        leaderboard += "Player 1\t" + winsbyPlayer1 + "\t" + rankofPlayer1 + "\n";
        leaderboard += "Player 2\t" + winsbyPlayer2 + "\t" + rankofPlayer2 + "\n";
        leaderboard += "Player 3\t" + winsbyPlayer3 + "\t" + rankofPlayer3 + "\n";
        leaderboard += "Player 4\t" + winsbyPlayer4 + "\t" + rankofPlayer4 + "\n";
        return leaderboard;
    }
}
