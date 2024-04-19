package uta.cse3310;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordList {

    private List<String> words;

    public WordList() {
        words = new ArrayList<>();
        loadWords("words.txt");
    }

    private void loadWords(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error loading words from file: " + e.getMessage());
        }
    }

    public String getWord() {
        if (words.isEmpty()) {
            return null;
        }
        // Get a random word from the list
        int randomIndex = (int) (Math.random() * words.size());
        return words.get(randomIndex);
    }
}
