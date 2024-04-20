package uta.cse3310;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Grid {

    static class GridGen {
        int numAttempts;
        char[][] cells = new char[nRows][nCols];
        List<String> solutions = new ArrayList<>();
        double density;
    }

    // gen words 8 directions
    static final int[][] DIRS = {
            { 1, 0 }, { 0, 1 }, { 1, 1 }, { 1, -1 }, { -1, 0 }, { 0, -1 }, { -1, -1 }, { -1, 1 }
    };

    // grid size
    static final int nRows = 50, nCols = 50;
    static final int gridSize = nRows * nCols;

    // min words in grid
    static final int minWords = 6;
    static final Random RANDOM = new Random();

    public static void main(String[] args) {
        printResult(createGrid(readWords("words.txt"), 250)); // number at end is for max word limit
    }

    static List<String> readWords(String filename) {
        int maxLength = Math.max(nRows, nCols);

        List<String> words = new ArrayList<>();

        try (Scanner sc = new Scanner(new FileReader(filename))) {
            while (sc.hasNext()) {
                String s = sc.next().trim().toLowerCase();
                if (s.matches("^[a-z]{3," + maxLength + "}$")) { // 3 is min word size limit
                    words.add(s.toUpperCase());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // Handle file not found error
        }

        return words;
    }

    static GridGen createGrid(List<String> words, int maxWords) {
        GridGen grid = null;
        int numAttempts = 0;

        // attempt grid 100 times
        while (++numAttempts < 100) {
            Collections.shuffle(words); // shuffle words
            grid = new GridGen();
            int messageLength = placeMessage(grid, "Grid:");
            int target = gridSize - messageLength;
            int cellsFilled = 0;

            for (String word : words) {
                cellsFilled += tryPlaceWord(grid, word);

                if (cellsFilled == target || grid.solutions.size() >= maxWords) {
                    break; // Stop placing words if the target is reached or maximum words reached
                }
            }

            if (grid.solutions.size() >= minWords && grid.solutions.size() <= maxWords) {
                grid.numAttempts = numAttempts;
                grid.density = (double) countValidWordCharacters(grid) / gridSize; // Calculate density
                return grid;
            }
        }

        return grid;
    }

    static int placeMessage(GridGen grid, String msg) {
        msg = msg.toUpperCase().replaceAll("[^A-Z]", "");
        int messageLength = msg.length();

        if (messageLength > 0 && messageLength < gridSize) {
            int gapSize = gridSize / messageLength;

            for (int i = 0; i < messageLength; i++) {
                int pos = i * gapSize + RANDOM.nextInt(gapSize);
                grid.cells[pos / nCols][pos % nCols] = msg.charAt(i);
            }

            return messageLength;
        }

        return 0;
    }

    static int tryPlaceWord(GridGen grid, String word) {
        int randDir = RANDOM.nextInt(DIRS.length);
        int randPos = RANDOM.nextInt(gridSize);

        for (int dir = 0; dir < DIRS.length; dir++) {
            dir = (dir + randDir) % DIRS.length;

            for (int pos = 0; pos < gridSize; pos++) {
                pos = (pos + randPos) % gridSize;

                int lettersPlaced = tryLocation(grid, word, dir, pos);

                if (lettersPlaced > 0)
                    return lettersPlaced;
            }
        }

        return 0;
    }

    static int tryLocation(GridGen grid, String word, int dir, int pos) {
        int r = pos / nCols;
        int c = pos % nCols;
        int length = word.length();

        // check bounds
        if ((DIRS[dir][0] == 1 && (length + c) > nCols)
                || (DIRS[dir][0] == -1 && (length - 1) > c)
                || (DIRS[dir][1] == 1 && (length + r) > nRows)
                || (DIRS[dir][1] == -1 && (length - 1) > r))
            return 0;

        int i, rr, cc, overlaps = 0;

        // check cells
        for (i = 0, rr = r, cc = c; i < length; i++) {
            if (grid.cells[rr][cc] != 0 && grid.cells[rr][cc] != word.charAt(i))
                return 0;

            cc += DIRS[dir][0];
            rr += DIRS[dir][1];
        }

        // place word
        for (i = 0, rr = r, cc = c; i < length; i++) {
            if (grid.cells[rr][cc] == word.charAt(i))
                overlaps++;
            else
                grid.cells[rr][cc] = word.charAt(i);

            if (i < length - 1) {
                cc += DIRS[dir][0];
                rr += DIRS[dir][1];
            }
        }

        int lettersPlaced = length - overlaps;

        if (lettersPlaced > 0)
            grid.solutions.add(String.format("%-10s (%d, %d)(%d,%d)", word, c, r, cc, rr));

        return lettersPlaced;
    }

    // Count the characters in valid words placed on the grid
    static int countValidWordCharacters(GridGen grid) {
        int count = 0;
        for (String solution : grid.solutions) {
            String word = solution.split("\\s+")[0]; // Extract the word
            count += word.length();
        }
        return count;
    }

    // print
    static void printResult(GridGen grid) {
        if (grid == null || grid.numAttempts == 0) {
            System.out.println("No grid to display");
            return;
        }

        int size = grid.solutions.size();

        System.out.println("Number of Attempts: " + grid.numAttempts);
        System.out.println("Number of words: " + size);
        System.out.println("Density of the grid: " + grid.density); // Print the density

        System.out.print("\n    ");

        for (int c = 0; c < nCols; c++) {
            System.out.print(c + "  ");
        }

        System.out.println();

        for (int r = 0; r < nRows; r++) {
            System.out.printf("%n%d  ", r);

            for (int c = 0; c < nCols; c++) {
                if (grid.cells[r][c] == 0) {
                    grid.cells[r][c] = (char) ('A' + RANDOM.nextInt(26)); // Fill empty cells with random letters
                }
                System.out.printf(" %c ", grid.cells[r][c]);
            }
        }

        System.out.println("\n");

        for (int i = 0; i < size - 1; i += 2) {
            System.out.printf("%s %s%n", grid.solutions.get(i), grid.solutions.get(i + 1));
        }

        if (size % 2 == 1) {
            System.out.println(grid.solutions.get(size - 1));
        }
    }
}