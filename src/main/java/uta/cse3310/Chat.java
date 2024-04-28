package uta.cse3310;
import java.util.ArrayList;

public class Chat {
    String word;
    PlayerType playeridx;
    boolean chatstatus;
    ArrayList<String> players = new ArrayList<String>();

    public void chatbox(Game game, String word) {
        ArrayList<String> arr = getPlayers(game);
        String delim = "[,]+";
        for (String s : arr) {
            System.out.println("player: " + s);
            String player[] = s.split(delim);
            String playertype = player[0];
            String name = player[1];
        if (playeridx == PlayerType.PLAYERONE &&  playertype.equals("PLAYERONE"))
            game.chat.add(String.format("%s : %s", name, word));
        else if (playeridx == PlayerType.PLAYERTWO && playertype.equals("PLAYERTWO"))
            game.chat.add(String.format("%s : %s", name, word));
        else if (playeridx == PlayerType.PLAYERTHREE && playertype.equals("PLAYERTHREE"))
            game.chat.add(String.format("%s : %s", name, word));
        else if (playeridx == PlayerType.PLAYERFOUR && playertype.equals("PLAYERFOUR"))
            game.chat.add(String.format("%s : %s", name, word));
        }
    }
    public ArrayList<String> getPlayers(Game game) {
        return game.chatplayers;
    }
    public void assignidx(Lobby lobby, Game game) {
        System.out.println("player: " + lobby.playeridx);
        game.chatplayers.add(lobby.playeridx+","+lobby.name);
    }
}