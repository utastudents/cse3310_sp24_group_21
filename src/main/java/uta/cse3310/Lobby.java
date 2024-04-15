package uta.cse3310;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
    private List<Game> activeGames = new ArrayList<>();
    private int gameId = 1;

    public Game createGame() {
        Game game = new Game();
        activeGames.add(game);
        game.players = new ArrayList<>();
        return game;
    }

    public Game joinGame(Game game, String playerName) {
        game.players.add(playerName);
        return game;
    }

    public boolean startGame(Game game) {
        if(game.players.size() >= 2) {
            game.state = GameState.PLAYING;
            return true;
        }
        return false;
    }

    public void removeGame(Game game) {
        activeGames.remove(game);
    }

    public List<String> getPlayers(Game game) {
        return new ArrayList<>(game.players);
    }
    
}
