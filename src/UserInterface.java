package uta.cse3310;

import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private Scanner scanner;

    public UserInterface(LeaderBoard leaderBoard) {
        this.scanner = new Scanner(System.in);
    }

    public void enterName() {
        System.out.println("Please enter your name:");
        String name = scanner.nextLine();
        System.out.println("Welcome, " + name + "!");
    }

    public void lobby() {
        System.out.println("Welcome to the lobby!");
        System.out.println("Options:");
        System.out.println("1. Start Game");
        System.out.println("2. View Rules");
        System.out.println("3. View Leaderboard");
        int choice = getIntInput("Enter your choice: ");
        switch (choice) {
            case 1:
                System.out.println("Starting game...");
                break;
            case 2:
                rules();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    public void wordList(List<String> words) {
        System.out.println("List of available words:");
        for (String word : words) {
            System.out.println(word);
        }
    }

    public void rules() {
        System.out.println("Game Rules:");
        System.out.println("Display game rules and instructions here.");
    }

    public void scoreBoard() {
        System.out.println("Scoreboard:");
        System.out.println("Display current game score here.");
    }

    public void chat() {
        System.out.println("Chatting...");
        // Implement chat functionality
    }

    public void grid() {
        System.out.println("Displaying game grid...");
        // Implement game grid display
    }

    public void playerlist() {
        System.out.println("List of players:");
        // Implement list of players in the game
    }

    public void gridinfo() {
        System.out.println("Game grid information:");
        // Implement game grid information
    }

    public void timer() {
        System.out.println("Starting timer...");
        // Implement timer functionality
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            System.out.print(prompt);
            scanner.next();
        }
        return scanner.nextInt();
    }
}
