package uta.cse3310;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Grid {

    static class GridGen {
        int numAttempts;
        char[][] cells = new char[nRows][nCols];
        List<String> solutions = new ArrayList<>();
        ArrayList<String> sol = new ArrayList<>();
        ArrayList<String> words = new ArrayList<>();
        ArrayList<String> findword = new ArrayList<>();
        double density;
        int[] orientationCounts = new int[DIRS.length]; // Track orientation counts
    }

    // gen words 6 directions
    static final int[][] DIRS = {
            { 1, 0 }, // Horizontal right to left
            { -1, 0 }, // Horizontal left to right
            { 0, 1 }, // Vertical top to bottom
            { 0, -1 }, // Vertical bottom to top
            { 1, 1 }, // Diagonal down
            { -1, -1 } // Diagonal up
    };

    // grid size
    static final int nRows = 20, nCols = 20;
    static final int gridSize = nRows * nCols;

    // min words in grid
    static final int minWords = 6;
    static final Random RANDOM = new Random();
    static final int ALPHABET_SIZE = 26;

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        printResult(createGrid(readWords(), 500)); // number at end is for max word limit
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("\nTime taken to generate grid: " + elapsedTime +
                " milliseconds");

    }

    public static List<String> readWords() {
        int maxLength = Math.max(nRows, nCols);

        List<String> words = new ArrayList<>();
        File file = new File("src/words.txt");
        try (Scanner sc = new Scanner(new FileReader(file))) {
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

    public static GridGen createGrid(List<String> words, int maxWords) {
        GridGen grid = null;
        int numAttempts = 0;

        // Variables to keep track of word counts in each orientation
        int[] orientationWordCounts = new int[DIRS.length];

        // attempt grid 100 times
        while (++numAttempts < 100) {
            Collections.shuffle(words); // shuffle words
            grid = new GridGen();
            int target = (int) (gridSize * 0.67); // Target approximately 67% of the grid area
            int cellsFilled = 0;

            for (String word : words) {
                cellsFilled += tryPlaceWord(grid, word, orientationWordCounts);

                if (cellsFilled > target || grid.solutions.size() >= maxWords) {
                    break; // Stop placing words if the target is reached or maximum words reached
                }
            }

            if (grid.solutions.size() >= minWords && grid.solutions.size() <= maxWords) {
                grid.numAttempts = numAttempts;
                grid.density = (double) countValidWordCharacters(grid) / gridSize; // Calculate density

                // Copy orientationWordCounts to grid's orientationCounts
                System.arraycopy(orientationWordCounts, 0, grid.orientationCounts, 0, DIRS.length);

                return grid;
            }
        }

        return grid;
    }

    static int tryPlaceWord(GridGen grid, String word, int[] orientationWordCounts) {
        int lettersPlaced = 0;
        int[] orientationPercentages = calculateOrientationPercentages(grid, orientationWordCounts);

        // Sort orientations by percentage (ascending order)
        List<Integer> sortedOrientations = new ArrayList<>();
        for (int i = 0; i < orientationPercentages.length; i++) {
            sortedOrientations.add(i);
        }
        sortedOrientations.sort(Comparator.comparingInt(i -> orientationPercentages[i]));

        // Shuffle positions to add randomness
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < gridSize; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);

        for (int orientation : sortedOrientations) {
            int dir = orientation;

            for (int pos : positions) {
                lettersPlaced = tryLocation(grid, word, dir, pos);

                if (lettersPlaced > 0) {
                    orientationWordCounts[dir]++; // Increment word count for this orientation
                    return lettersPlaced;
                }
            }
        }

        return 0;
    }

    static int[] calculateOrientationPercentages(GridGen grid, int[] orientationWordCounts) {
        int size = grid.solutions.size();
        int[] orientationPercentages = new int[DIRS.length];

        for (int i = 0; i < DIRS.length; i++) {
            orientationPercentages[i] = (int) ((double) orientationWordCounts[i] / size * 100);
        }

        return orientationPercentages;
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
        grid.sol.add(String.format("%d,%d,%d,%d", c, r, cc, rr));
        grid.words.add(word);
        grid.findword.add(String.format("%s,%d,%d,%d,%d", word, c, r, cc, rr));
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
    // print
    static void printResult(GridGen grid) {
        if (grid == null || grid.numAttempts == 0) {
            System.out.println("No grid to display");
            return;
        }

        int size = grid.solutions.size();

        System.out.println("Number of Attempts: " + grid.numAttempts);
        System.out.println("Number of words: " + size);
        System.out.println("Density of the grid: " + grid.density);

        // Display percentage of words in each orientation
        System.out.println("Percentage of words in each orientation:");
        for (int i = 0; i < DIRS.length; i++) {
            String orientationLabel;
            switch (i) {
                case 0:
                    orientationLabel = "Horizontal (right to left)";
                    break;
                case 1:
                    orientationLabel = "Horizontal (left to right)";
                    break;
                case 2:
                    orientationLabel = "Vertical (top to bottom)";
                    break;
                case 3:
                    orientationLabel = "Vertical (bottom to top)";
                    break;
                case 4:
                    orientationLabel = "Diagonal (down)";
                    break;
                case 5:
                    orientationLabel = "Diagonal (up)";
                    break;
                default:
                    orientationLabel = "Unknown";
                    break;
            }
            double percentage = (double) grid.orientationCounts[i] / size * 100;
            System.out.printf("%s: %.2f%%%n", orientationLabel, percentage);
        }

        // Display uniformness of filler characters
        double[] letterCounts = new double[ALPHABET_SIZE]; // Array to store counts of each letter
        int totalFillerCount = 0; // Total count of filler characters
        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {
                if (grid.cells[r][c] == 0) {
                    char filler = (char) ('A' + RANDOM.nextInt(ALPHABET_SIZE)); // Random filler character
                    letterCounts[filler - 'A']++; // Increment count for the corresponding letter
                    totalFillerCount++;
                }
            }
        }
        // Calculate uniformness. value of 1 would mean perfectly uniform
        double uniformness = calculateUniformness(letterCounts, totalFillerCount);
        String formattedUniformness = String.format("%.4f", uniformness);
        System.out.println("Uniformness of filler characters: " + formattedUniformness);

        System.out.print("\n    ");

        for (int c = 0; c < nCols; c++) {
            System.out.print(c + "  ");
        }

        System.out.println();

        fillEmptyCells(grid);

        for (int r = 0; r < nRows; r++) {
            System.out.printf("%n%d  ", r);

            for (int c = 0; c < nCols; c++) {
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

    // Helper method to calculate uniformness of filler characters
    static double calculateUniformness(double[] letterCounts, int totalFillerCount) {
        double uniformness = 0;
        for (double count : letterCounts) {
            uniformness += Math.pow(count / totalFillerCount - 1.0 / ALPHABET_SIZE, 2); // squared deviation
        }
        return 1 - uniformness / ALPHABET_SIZE; // Normalize to range [0, 1]
    }

    static void fillEmptyCells(GridGen grid) {
        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {
                if (grid.cells[r][c] == 0) {
                    grid.cells[r][c] = (char) ('A' + RANDOM.nextInt(ALPHABET_SIZE)); // Fill empty cells with random
                                                                                     // letters
                }
            }
        }
    }

}
