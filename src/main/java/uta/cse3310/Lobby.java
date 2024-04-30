package uta.cse3310;

import java.util.ArrayList;

public class Lobby {
    private ArrayList<Game> activeGames = new ArrayList<>();
    public String name;
    PlayerType playeridx;
    boolean status;

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

    public ArrayList<Game> getActiveGames() {
        return activeGames;
    }

    public boolean startGame(Game game) {
        if (game.players.size() >= 2) {
            game.state = GameState.PLAYING;
            return true;
        }
        return false;
    }

    public void removeGame(Game game) {
        activeGames.remove(game);
    }

    public ArrayList<String> getPlayers(Game game) {
        return new ArrayList<>(game.players);
    }
}
