package uta.cse3310;

import junit.framework.TestCase;

public class LeaderBoardTest extends TestCase {
    private LeaderBoard leaderBoard;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        leaderBoard = new LeaderBoard();
    }

    public void testAddScore_NewPlayer() {
        leaderBoard.addScore("Alice", 100);
        assertEquals("Top player should be Alice", "Alice", leaderBoard.getTopPlayerName());
        assertEquals("Score of Alice should be 100", 100, leaderBoard.getTopScore());
    }

    public void testAddScore_ExistingPlayer() {
        leaderBoard.addScore("Alice", 100);
        leaderBoard.addScore("Alice", 50);
        assertEquals("Top player should still be Alice", "Alice", leaderBoard.getTopPlayerName());
        assertEquals("Score of Alice should be updated to 150", 150, leaderBoard.getTopScore());
    }

    public void testLeaderBoard_Sorting() {
        leaderBoard.addScore("Alice", 100);
        leaderBoard.addScore("Bob", 200);
        leaderBoard.addScore("Charlie", 150);
        assertEquals("Top score should be Bob's", "Bob", leaderBoard.getTopPlayerName());
        assertEquals("Top score should be 200", 200, leaderBoard.getTopScore());
    }

    public void testGetTopPlayerName() {
        leaderBoard.addScore("Alice", 100);
        leaderBoard.addScore("Bob", 200);
        assertEquals("Top player should be Bob", "Bob", leaderBoard.getTopPlayerName());
    }

    public void testGetTopScore() {
        leaderBoard.addScore("Alice", 100);
        leaderBoard.addScore("Bob", 200);
        assertEquals("Top score should be 200", 200, leaderBoard.getTopScore());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        leaderBoard = null;
    }
}
