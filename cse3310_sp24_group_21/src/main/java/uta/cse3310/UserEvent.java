package uta.cse3310;
// User events are sent from the webpage to the server

public class UserEvent {
    int GameId; // the game ID on the server
    PlayerType PlayerIdx;
    // int Button; // button number from 0 to 8
    String Name;
    int i, j, k, l;
    public boolean playing;
    boolean start;
    private boolean wordIsSelectedAndHighlighted;
    

    UserEvent(int m, int n, int o, int p, PlayerType noplayer) {

    }
 // Getter and setter methods for wordIsSelectedAndHighlighted
 public boolean isWordSelectedAndHighlighted() {
    return wordIsSelectedAndHighlighted;
}

public void setWordSelectedAndHighlighted(boolean wordIsSelectedAndHighlighted) {
    this.wordIsSelectedAndHighlighted = wordIsSelectedAndHighlighted;
}
public PlayerType getPlayerIdx() {
    return PlayerIdx;
}

}
