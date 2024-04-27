package uta.cse3310;

import java.util.Scanner;
import java.util.List;

public class UserInterface {

    private Scanner scanner;
    private LeaderBoard leaderBoard;  // Instance of LeaderBoard to manage scores

    public UserInterface(LeaderBoard leaderboard) {
        this.leaderBoard = leaderboard;
        scanner = new Scanner(System.in);
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
            case 3:
                leaderBoard();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    public void wordList() {
        System.out.println("List of available words:");
        // Assume a method exists to get words from a WordList object
    }

    public void rules() {
        System.out.println("Game Rules:");
        System.out.println("1. Describe the rules here.");
    }

    public void scoreBoard() {
        System.out.println("Scoreboard:");
        System.out.println("Current scores:");
        // Could also call leaderBoard.displayLeaderBoard() if it returns string
    }

    public void leaderBoard() {
        System.out.println("Leaderboard:");
        List<LeaderBoard.PlayerScore> scores = leaderBoard.getHighScores();
        for (LeaderBoard.PlayerScore score : scores) {
            System.out.println(score.getPlayerName() + ": " + score.getScore());
        }
    }

    public void chat() {
        System.out.println("Chatting...");
        // Chat implementation here
    }

    public void grid() {
        System.out.println("Displaying game grid...");
        // Grid display logic here
    }

    public void playerlist() {
        System.out.println("List of players:");
        // List players here
    }

    public void gridinfo() {
        System.out.println("Game grid information:");
        // Grid info here
    }

    public void timer() {
        System.out.println("Starting timer...");
        // Timer logic here
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
