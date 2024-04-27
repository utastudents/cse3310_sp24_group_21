package uta.cse3310;

import java.util.ArrayList;

/**
 * Represents events and states sent from the server to clients,
 * such as game updates, player assignments, or messages.
 */
public class ServerEvent {
    PlayerType YouAre;  // The role or type assigned to the connected client
    int GameId;         // Identifier of the game session related to this event
    public Game Game;   // The game object with current state, if needed for the event
    public int PlayerId;  // Unique identifier for a player, could be useful for tracking
    public String PlayerName; // Name of the player involved in this event
    public String BottomMsg;  // Optional message for display at the client's UI
    public ArrayList<String> Players = new ArrayList<>(); // List of players, for lobby or game updates

    /**
     * Default constructor for creating a basic ServerEvent with initialization.
     */
    public ServerEvent() {
        this.YouAre = PlayerType.NOPLAYER;  // Default to NOPLAYER unless specified
        this.GameId = -1;  // Default ID that should be overridden when setting up a real game event
        this.Game = null;  // No game associated by default
        this.PlayerId = -1;  // Default identifier
        this.PlayerName = "";
        this.BottomMsg = "";
        this.Players = new ArrayList<>();
    }

    /**
     * Constructs a ServerEvent with specified player type and game ID, typically used when a new player joins.
     *
     * @param youAre The role or type of the player.
     * @param gameId The game identifier associated with this event.
     */
    public ServerEvent(PlayerType youAre, int gameId) {
        this();
        this.YouAre = youAre;
        this.GameId = gameId;
    }

    /**
     * Adds a player name to the event's player list, commonly used in lobby updates.
     *
     * @param playerName The name of the player to add.
     */
    public void addPlayer(String playerName) {
        this.Players.add(playerName);
    }

    /**
     * Sets a message to be shown in the UI, used for feedback or instructions.
     *
     * @param message The message to be displayed.
     */
    public void setMessage(String message) {
        this.BottomMsg = message;
    }
}
