package uta.cse3310;

import java.util.ArrayList;
import java.util.List;

public class Lobby {
     private ArrayList<Game> activeGames;
    private int gameId = 1;
    public String name;

    public Game createGame() {
        Game game = new Game();
        activeGames.add(game);
        activeGames = new ArrayList<>();
        return game;
    }

    public Game joinGame(Game game, String playerName) {
        game.players.add(playerName);
        return game;
    }
     
     public ArrayList<Game> getActiveGames() {
        return activeGames;
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

    
}
