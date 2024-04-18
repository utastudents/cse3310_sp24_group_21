package uta.cse3310;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Lobby {
    private int nextPlayerId = 1;
    private Map<Game, List<Player>> gamePlayers = new HashMap<>();

    public Game createGame() {
        Game game = new Game();
        gamePlayers.put(game, new ArrayList<>());
        return game;
    }

    public Player joinGame(Game game, String playerName) {
        int playerId = nextPlayerId++;
        Player player = new Player(playerName, playerId);

        List<Player> players = gamePlayers.get(game);
        players.add(player);

        return player;
    }

    public boolean startGame(Game game) {
        List<Player> players = gamePlayers.get(game);
        if (players.size() >= 2) {
            // Start the game logic
            return true;
        }
        return false;
    }

    public List<Player> getPlayers(Game game) {
        return gamePlayers.get(game);
    }
}