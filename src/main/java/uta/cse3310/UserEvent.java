package uta.cse3310;
// User events are sent from the webpage to the server

public class UserEvent {
    int GameId; // the game ID on the server
    PlayerType PlayerIdx;
    // int Button; // button number from 0 to 8
    String Name;
    int i, j, k, l;
    public boolean playing;
    String type;  
    String text;
    String username;

   
   public UserEvent(String _Type, String _Text, String _Username) {
    type = _Type;
    text = _Text;
    username = _Username;
}
    // UserEvent(int _GameId, PlayerType _PlayerIdx, int _Button) {
    // GameId = _GameId;
    // PlayerIdx = _PlayerIdx;
    // // Button = _Button;
    // }
}