package uta.cse3310;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LeaderBoardTest {
    private LeaderBoard leaderBoard;
    private Lobby lobby;
    private Game game;

    @Before
    public void setUp() {
        leaderBoard = new LeaderBoard();
        lobby = new Lobby();
        game = new Game();
    }
    @Test
    public void testSetPlayer() {
        lobby.name = "Test Lobby";
        lobby.playeridx = PlayerType.PLAYERONE; 
        lobby.createGame();
        leaderBoard.setPlayer(lobby, 100, game);
        assertEquals(1, game.scores.size());
        assertEquals("Test Lobby:100:PLAYERONE", game.scores.get(0));
    }
    @Test
    public void testAddScore() {
        game.scores.add("Test Lobby:100:PLAYERONE");
        leaderBoard.addScore(PlayerType.PLAYERONE, game, 50);
        assertEquals(1, game.scores.size());
        assertEquals("Test Lobby:150:PLAYERONE", game.scores.get(0));
    }
}
