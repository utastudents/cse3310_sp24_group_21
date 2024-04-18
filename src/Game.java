package uta.cse3310;
import java.util.ArrayList;
import java.util.List;

public class Game {
    public Game() {
        // Initialize any fields or perform any necessary setup
    }
    private Statistics stats;

    public Game(Statistics stats) {
        this.stats = stats;
        // Initialize other fields if needed
    }
    public void StartGame() {
        // Implement game start logic here
    }
    public PlayerType Players;
    public int GameId;
    public List<String> players = new ArrayList<>();
    public GameState state = GameState.LOBBY; // current game state is in the lobby 
}