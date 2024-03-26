package uta.cse3310;

// there are a lot of remnants from the tictactoe program incase they're needed later, will be removed otherwise
public class Game {

    PlayerType Players;
    public PlayerType[] Button;

    public String[] Msg;
    public int GameId;
    public Statistics Stats;

    Game(Statistics s) {
        Stats = s;
        Button = new PlayerType[9];
        // initialize it
        ResetGrid();

        Msg = new String[2];
        Msg[0] = "";
    }

    public void ResetGrid() {
        // initializes the board to NOPLAYER in all spots
        for (int i = 0; i < Button.length; i++) {
            Button[i] = PlayerType.NOPLAYER;
        }
    }

    public void StartGame() {
        Msg[0] = "";
        Stats.setGamesInProgress(Stats.getGamesInProgress() + 1);
    }

    private boolean CheckLine(int i, int j, int k, PlayerType player) {
        return player == Button[i] && player == Button[j] && player == Button[k];
    }

    private boolean CheckHorizontal(PlayerType player) {
        return CheckLine(0, 1, 2, player) || CheckLine(3, 4, 5, player) || CheckLine(6, 7, 8, player);
    }

    private boolean CheckVertical(PlayerType player) {
        return CheckLine(0, 3, 6, player) || CheckLine(1, 4, 7, player) || CheckLine(2, 5, 8, player);
    }

    private boolean CheckDiagonal(PlayerType player) {
        return CheckLine(0, 4, 8, player) || CheckLine(2, 4, 6, player);
    }

    public boolean CheckBoard(PlayerType player) {
        return CheckHorizontal(player) || CheckVertical(player) || CheckDiagonal(player);
    }

    public boolean CheckDraw(PlayerType player) {
        // It is a draw if neither player has won.
        boolean retval = false;

        return retval;
    }

    // This function returns an index for each player
    // It does not depend on the representation of Enums
    public int PlayerToIdx(PlayerType P) {
        int retval = 0;
        return retval;
    }

    public void Update(UserEvent U) {
    }

    public void GameTime() {
        // this function can be called periodically if a
        // timer is needed.

    }
}
// In windows, shift-alt-F formats the source code
// In linux, it is ctrl-shift-I