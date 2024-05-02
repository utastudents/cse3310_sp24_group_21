package uta.cse3310;

import junit.framework.TestCase;
import java.util.ArrayList;

public class ChatTest extends TestCase {
    private Game game;
    private Lobby lobby;
    private Chat chat;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        game = new Game();
        lobby = new Lobby();
        chat = new Chat();
        game.chat = new ArrayList<>();
        game.chatplayers = new ArrayList<>();
    }

    public void testChatbox() {
        // Assign player with PlayerType directly
        lobby.playeridx = PlayerType.PLAYERONE;
        lobby.name = "John";
        chat.assignidx(lobby, game);

        lobby.playeridx = PlayerType.PLAYERTWO;
        lobby.name = "Jane";
        chat.assignidx(lobby, game);

        // Test chat functionality for Player One
        chat.playeridx = PlayerType.PLAYERONE;
        chat.chatbox(game, "Hello from Player One");
        assertTrue("Chat should contain message from Player One",
                   game.chat.contains("John : Hello from Player One"));

        // Test chat functionality for Player Two
        chat.playeridx = PlayerType.PLAYERTWO;
        chat.chatbox(game, "Hello from Player Two");
        assertTrue("Chat should contain message from Player Two",
                   game.chat.contains("Jane : Hello from Player Two"));
    }

    public void testAssignIdx() {
        lobby.playeridx = PlayerType.PLAYERONE;
        lobby.name = "Alice";
        chat.assignidx(lobby, game);
        assertTrue("Game chat players should include Alice with index",
                   game.chatplayers.contains("PLAYERONE,Alice"));
    }

    public void testGetPlayers() {
        lobby.playeridx = PlayerType.PLAYERONE;
        lobby.name = "Bob";
        chat.assignidx(lobby, game);
        ArrayList<String> players = chat.getPlayers(game);
        assertTrue("Should retrieve list containing Bob",
                   players.contains("PLAYERONE,Bob"));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        game = null;
        lobby = null;
        chat = null;
    }
}
