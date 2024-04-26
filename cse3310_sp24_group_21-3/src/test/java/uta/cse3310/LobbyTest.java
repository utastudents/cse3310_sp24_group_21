package uta.cse3310;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class LobbyTest {

    private Lobby lobby;

    @Before
    public void setUp() {
        lobby = new Lobby();
    }

    @Test
    public void testCreateGame() {
        Game game = lobby.createGame();

        // Check that a game was created
        assertNotNull(game);
        
        // Check that the activeGames list contains the created game
        ArrayList<Game> activeGames = lobby.getActiveGames();
        assertTrue(activeGames.contains(game));
    }

    @Test
    public void testJoinGame() {
        Game game = lobby.createGame();
        String playerName = "Player1";
        lobby.joinGame(game, playerName);

        assertTrue(game.players.contains(playerName));
    }

    @Test
    public void testStartGame() {
        Game game = lobby.createGame();
        lobby.joinGame(game, "Player1");
        lobby.joinGame(game, "Player2");

        assertTrue(lobby.startGame(game));
        assertEquals(GameState.PLAYING, game.state);
    }


    @Test
    public void testGetPlayers() {
        Game game = lobby.createGame();
        lobby.joinGame(game, "Player1");
        lobby.joinGame(game, "Player2");

        ArrayList<String> players = lobby.getPlayers(game);
        assertEquals(2, players.size());
        assertTrue(players.contains("Player1"));
        assertTrue(players.contains("Player2"));
    }
}
