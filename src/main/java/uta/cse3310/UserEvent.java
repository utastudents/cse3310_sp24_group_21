package uta.cse3310;

/**
 * Represents events initiated by the user from the client-side,
 * sent to the server to update the game state or player interactions.
 */
public class UserEvent {
    int GameId; // The unique identifier for the game session
    PlayerType PlayerIdx; // Identifies the player type or role
    String Name; // Optional: Player's name if needed for the event
    int i, j, k, l; // Coordinates or indices for game actions, like selecting grid positions
    public boolean playing; // Flag to indicate if the event is during active play
    boolean start; // Flag to indicate a request to start the game

    /**
     * Constructs a UserEvent for actions that involve specific positions in the game.
     *
     * @param m The start x-coordinate (row index).
     * @param n The start y-coordinate (column index).
     * @param o The end x-coordinate (row index).
     * @param p The end y-coordinate (column index).
     * @param playerIdx The type of player initiating the event.
     */
    UserEvent(int m, int n, int o, int p, PlayerType playerIdx) {
        this.i = m;
        this.j = n;
        this.k = o;
        this.l = p;
        this.PlayerIdx = playerIdx;
        this.playing = true;  // Assume this event type means the player is actively playing
    }

    /**
     * Constructs a UserEvent for general game actions like starting a game or identifying a player.
     *
     * @param gameId The game ID to which the event is related.
     * @param playerIdx The player type or identifier.
     * @param start Flag to indicate if the game should start.
     */
    UserEvent(int gameId, PlayerType playerIdx, boolean start) {
        this.GameId = gameId;
        this.PlayerIdx = playerIdx;
        this.start = start;
    }
}
