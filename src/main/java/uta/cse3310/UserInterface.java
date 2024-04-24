package uta.cse3310;

import java.util.Scanner;

public class UserInterface {

    private Scanner scanner;
     // Declare a variable for the LeaderBoard object

    public UserInterface() {
        scanner = new Scanner(System.in);
    }


public void enterName() {
    System.out.println("Please enter your name:");
    String name = scanner.nextLine();
    System.out.println("Welcome, " + name + "!");
}

public void lobby() {
    //System.out.println("Welcome to the lobby!");
    //System.out.println("Options:");
    //System.out.println("1. Start Game");
    //System.out.println("2. View Rules");
    //System.out.println("3. View Leaderboard");
    //int choice = getIntInput("Enter your choice: ");
    //switch (choice) {
       // case 1:
            //System.out.println("Starting game...");
           // break;
        //case 2:
          //  rules();
           // break;
       // case 3:
           // leaderBoard();
           // break;
        //default:
          //  System.out.println("Invalid choice.");
    }


public void wordList() {
    System.out.println("List of available words:");
    // Display list of words
}

public void rules() {
    System.out.println("Game Rules:");
    // Display game rules and instructions
}

public void scoreBoard() {
    System.out.println("Scoreboard:");
    // Display current game score
}

public void leaderBoard() {
    System.out.println("Leaderboard:");
// Call the static method directly
}


public void chat() {
    System.out.println("Chatting...");
    // Implement chat functionality
}

public void grid() {
    System.out.println("Displaying game grid...");
    // Display game grid
}

public void playerlist() {
    System.out.println("List of players:");
    // Display list of players in the game
}

public void gridinfo() {
    System.out.println("Game grid information:");
    // Display information about the game grid
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
