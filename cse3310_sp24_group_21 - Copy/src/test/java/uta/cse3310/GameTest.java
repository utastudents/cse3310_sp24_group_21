package uta.cse3310;

import junit.framework.TestCase;

public class GameTest extends TestCase {
    private Game game;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        game = new Game(); // Initialize game object for each test
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        game = null; // Clean up after each test
    }

    public void testResetBoard() {
        game.ResetBoard();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                assertEquals("Board should be reset to NOPLAYER", PlayerType.NOPLAYER, game.Button[i][j]);
            }
        }
    }

    public void testHorizontalPlacement() {
        // Simulate a valid horizontal word placement by the player
        UserEvent ue = new UserEvent(0, 0, 0, 0, null);
        game.Update(ue);
        for (int i = 0; i <= 4; i++) {
            assertEquals("Expected NOPLAYER at position", PlayerType.NOPLAYER, game.Button[0][i]);
        }
    }

    public void testVerticalPlacement() {
        // Simulate a valid vertical word placement by the player
        UserEvent ue = new UserEvent(5, 2, 5, 6, PlayerType.NOPLAYER);
        game.Update(ue);
        for (int j = 2; j <= 6; j++) {
            assertEquals("Expected NOPLAYER at position", PlayerType.NOPLAYER, game.Button[j][5]);
        }
    }

    public void testDiagonalPlacement() {
        // Test a forward diagonal (top left to bottom right)
        UserEvent ue = new UserEvent(3, 3, 6, 6, PlayerType.NOPLAYER);
        game.Update(ue);
        int k = 3;
        for (int i = 3; i <= 6; i++) {
            assertEquals("Expected NOPLAYER at diagonal position", PlayerType.NOPLAYER, game.Button[k][i]);
            k++;
        }
    }
}
