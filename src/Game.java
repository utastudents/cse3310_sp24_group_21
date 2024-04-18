package uta.cse3310;

import java.util.ArrayList;
import java.util.List;

enum PlayerType {
    PLAYER1,
    PLAYER2
}

class Statistics {
    // Placeholder for statistics
}

public class Game {
    private PlayerType[] players;
    private List<String> msg;
    private int gameId;
    private Statistics stats;
    private char[][] grid; // Grid of letters
    private boolean[][] selected; // To track selected letters
    private boolean[][] wordCompleted; // To track completed words

    public Game(Statistics stats) {
        this.stats = stats;
        this.players = new PlayerType[2]; // Assuming only 2 players for simplicity
        this.msg = new ArrayList<>();
        this.gameId = 0; // Set game ID
        this.grid = generateGrid(); // Generate initial grid
        this.selected = new boolean[grid.length][grid[0].length];
        this.wordCompleted = new boolean[grid.length][grid[0].length];
    }

    public void ResetGrid() {
        this.grid = generateGrid(); // Regenerate grid
        this.selected = new boolean[grid.length][grid[0].length];
        this.wordCompleted = new boolean[grid.length][grid[0].length];
    }

    public void StartGame() {
        // Initialize players, display grid, etc.
        // This method is responsible for starting the game.
    }

    private boolean CheckLine(int startX, int startY, int endX, int endY, PlayerType player) {
        // This method checks if a line formed by the given coordinates is valid for the
        // player.
        // It checks if the selected line forms a valid word.
        StringBuilder word = new StringBuilder();
        int dx = (endX - startX) / Math.max(Math.abs(endX - startX), 1);
        int dy = (endY - startY) / Math.max(Math.abs(endY - startY), 1);
        for (int x = startX, y = startY; x != endX + dx || y != endY + dy; x += dx, y += dy) {
            word.append(grid[x][y]);
        }

        return isValidWord(word.toString());
    }

    private boolean CheckHorizontal(PlayerType player) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (j + 1 < grid[i].length && !selected[i][j] && !selected[i][j + 1]) {
                    if (CheckLine(i, j, i, j + 1, player)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean CheckVertical(PlayerType player) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i + 1 < grid.length && !selected[i][j] && !selected[i + 1][j]) {
                    if (CheckLine(i, j, i + 1, j, player)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean CheckDiagonal(PlayerType player) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i + 1 < grid.length && j + 1 < grid[i].length && !selected[i][j] && !selected[i + 1][j + 1]) {
                    if (CheckLine(i, j, i + 1, j + 1, player)) {
                        return true;
                    }
                }
                if (i - 1 >= 0 && j + 1 < grid[i].length && !selected[i][j] && !selected[i - 1][j + 1]) {
                    if (CheckLine(i, j, i - 1, j + 1, player)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean CheckBoard(PlayerType player) {
        return CheckHorizontal(player) || CheckVertical(player) || CheckDiagonal(player);
    }

    public boolean CheckDraw(PlayerType player) {
        // Check if the grid is filled and no player has completed any word
        for (int i = 0; i < wordCompleted.length; i++) {
            for (int j = 0; j < wordCompleted[i].length; j++) {
                if (!wordCompleted[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private int PlayerToIdx(PlayerType player) {
        return player == PlayerType.PLAYER1 ? 0 : 1;
    }

    public void Update(UserEvent event) {
        // Update game state based on user events
    }

    public void GameTime() {
        // Manage game timing
    }

    private char[][] generateGrid() {
        // Generate the grid of letters

        return new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
    }

    private boolean isValidWord(String word) {
        // Placeholder for word validation logic

        return word.length() > 1;
    }
}

class UserEvent {
    // Define user event properties and methods as needed
}