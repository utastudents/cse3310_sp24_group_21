package uta.cse3310;

public class Player {
    private String name;
    private int id;

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
    }

    // Getter and setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}