package uta.cse3310;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StatisticsTest {
    private Statistics statistics;

    @Before
    public void setUp() {
        statistics = new Statistics();
    }

    @Test
    public void testGetWinsForPlayer() {
        statistics.setWinsForPlayer(PlayerType.PLAYERONE, 5);
        statistics.setWinsForPlayer(PlayerType.PLAYERTWO, 3);
        statistics.setWinsForPlayer(PlayerType.PLAYERTHREE, 2);
        statistics.setWinsForPlayer(PlayerType.PLAYERFOUR, 1);

        assertEquals(5, statistics.getWinsForPlayer(PlayerType.PLAYERONE).intValue());
        assertEquals(3, statistics.getWinsForPlayer(PlayerType.PLAYERTWO).intValue());
        assertEquals(2, statistics.getWinsForPlayer(PlayerType.PLAYERTHREE).intValue());
        assertEquals(1, statistics.getWinsForPlayer(PlayerType.PLAYERFOUR).intValue());
    }

    @Test
    public void testSetAndGetTieGames() {
        statistics.setTieGames(3);
        assertEquals(3, statistics.getTieGames().intValue());
    }

    @Test
    public void testSetAndGetActiveGames() {
        statistics.setActiveGames(2);
        assertEquals(2, statistics.getActiveGames().intValue());
    }

    @Test
    public void testSetScoreTotal() {
        statistics.setScoreTotal(100);
        updatePlayerRanks(statistics);

        assertEquals(1, statistics.getRankForPlayer(PlayerType.PLAYERONE).intValue());
        assertEquals(2, statistics.getRankForPlayer(PlayerType.PLAYERTWO).intValue());
        assertEquals(3, statistics.getRankForPlayer(PlayerType.PLAYERTHREE).intValue());
        assertEquals(4, statistics.getRankForPlayer(PlayerType.PLAYERFOUR).intValue());
    }

    private void updatePlayerRanks(Statistics statistics) {
        statistics.setWinsForPlayer(PlayerType.PLAYERONE, 10);
        statistics.setWinsForPlayer(PlayerType.PLAYERTWO, 8);
        statistics.setWinsForPlayer(PlayerType.PLAYERTHREE, 6);
        statistics.setWinsForPlayer(PlayerType.PLAYERFOUR, 4);
    }

    @Test
    public void testDisplayLeaderBoard() {
        statistics.setWinsForPlayer(PlayerType.PLAYERONE, 10);
        statistics.setWinsForPlayer(PlayerType.PLAYERTWO, 8);
        statistics.setWinsForPlayer(PlayerType.PLAYERTHREE, 6);
        statistics.setWinsForPlayer(PlayerType.PLAYERFOUR, 4);
        statistics.setScoreTotal(100);

        String expectedLeaderboard = "Leaderboard:\nPlayer\tWins\tRank\n" +
                "Player 1\t10\t1\n" +
                "Player 2\t8\t2\n" +
                "Player 3\t6\t3\n" +
                "Player 4\t4\t4\n";

        assertEquals(expectedLeaderboard, statistics.displayLeaderBoard());
    }
}
