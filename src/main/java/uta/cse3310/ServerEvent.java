package uta.cse3310;

import java.util.ArrayList;

public class ServerEvent {
    PlayerType YouAre;
    int GameId;
    public Game Game;
    public int PlayerId;
    public String PlayerName;
    public String BottomMsg;
    public ArrayList<String> Players = new ArrayList<>();
}