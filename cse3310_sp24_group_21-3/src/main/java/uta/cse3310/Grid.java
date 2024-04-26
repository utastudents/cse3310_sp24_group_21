package uta.cse3310;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    }

    // gen words 8 directions (technically 8 because diagonal is four different
    // directions)
    static final int[][] DIRS = {
            { 1, 0 }, { 0, 1 }, { 1, 1 }, { 1, -1 }, { -1, 0 }, { 0, -1 }, { -1, -1 }, { -1, 1 }
    };

    // grid size
    static final int nRows = 20, nCols = 20;
    static final int gridSize = nRows * nCols;

    // min words in grid
    static final int minWords = 6;
    static final Random RANDOM = new Random();

    public static void main(String[] args) {
        /* 
        long startTime = System.currentTimeMillis();
        printResult(createGrid(readWords(), 750)); // number at end is for max word limit
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("\nTime taken to generate grid: " + elapsedTime +
                " milliseconds");
        */
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

        // attempt grid 100 times
        while (++numAttempts < 100) {
            Collections.shuffle(words); // shuffle words
            grid = new GridGen();
            int target = (int) (gridSize * 0.67); // Target approximately 67% of the grid area
            int cellsFilled = 0;
            int minWordsPerOrientation = (int) Math.ceil(gridSize * 0.15 / 5); // 15% of each orientation

            int[] orientations = new int[8]; // Track the number of words placed in each orientation

            for (String word : words) {
                // Ensure that each orientation has at least 15% of the required words
                if (orientations[0] >= minWordsPerOrientation && orientations[1] >= minWordsPerOrientation &&
                        orientations[2] >= minWordsPerOrientation && orientations[3] >= minWordsPerOrientation &&
                        orientations[4] >= minWordsPerOrientation) {
                    break;
                }

                cellsFilled += tryPlaceWord(grid, word, orientations);

                if (cellsFilled > target || grid.solutions.size() >= maxWords) {
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

    static int tryPlaceWord(GridGen grid, String word, int[] orientations) {
        int randDir = RANDOM.nextInt(DIRS.length);
        int randPos = RANDOM.nextInt(gridSize);
        int cellsFilled = 0;

        for (int dir = 0; dir < DIRS.length; dir++) {
            dir = (dir + randDir) % DIRS.length;

            for (int pos = 0; pos < gridSize; pos++) {
                pos = (pos + randPos) % gridSize;

                int lettersPlaced = tryLocation(grid, word, dir, pos);

                if (lettersPlaced > 0) {
                    cellsFilled += lettersPlaced;
                    orientations[dir]++;
                    return cellsFilled;
                }
            }
        }

        return cellsFilled;
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

        // Count occurrences of each letter in words
        int[] letterCount = new int[26];
        for (String word : grid.words) {
            for (char letter : word.toCharArray()) {
                letterCount[letter - 'A']++;
            }
        }

        // Determine the maximum occurrences of any letter
        int maxOccurrences = Arrays.stream(letterCount).max().orElse(0);

        // Calculate the minimum occurrences of each letter for uniform distribution
        int minOccurrences = (gridSize - grid.solutions.stream().mapToInt(s -> s.length()).sum()) / 26;

        for (int r = 0; r < nRows; r++) {
            System.out.printf("%n%d  ", r);

            for (int c = 0; c < nCols; c++) {
                if (grid.cells[r][c] == 0) {
                    // Fill empty cells with random letters ensuring uniform distribution
                    char filler = getUniformFiller(letterCount, minOccurrences, maxOccurrences);
                    grid.cells[r][c] = filler;
                    letterCount[filler - 'A']--;
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

    // Method to get a uniform filler character
    static char getUniformFiller(int[] letterCount, int minOccurrences, int maxOccurrences) {
        char filler = (char) ('A' + RANDOM.nextInt(26));
        int index = filler - 'A';

        // Ensure the chosen filler has not exceeded its maximum occurrences
        while (letterCount[index] <= minOccurrences && letterCount[index] < maxOccurrences) {
            filler = (char) ('A' + RANDOM.nextInt(26));
            index = filler - 'A';
        }

        return filler;
    }

}