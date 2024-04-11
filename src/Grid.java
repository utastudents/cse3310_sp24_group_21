package uta.cse3310;

public class Grid {
    private int sizeGrid;
    private char[] letters;

    public Grid(int sizeGrid) {
        this.sizeGrid = sizeGrid;
        this.letters = new char[sizeGrid * sizeGrid];
        initializeGrid();
    }

    // Method to generate the grid
    public char[] generateGrid() {
        placeWordsInGrid();
        placeLettersInGrid();
        return letters;
    }

    // Method to calculate grid density
    public float calGridDensity() {
        int validChar = 0;
        for (char letter : letters) {
            if (letter != '-') {
                validChar++;
            }
        }
        float density = (float) validChar / (sizeGrid * sizeGrid);
        if (density <= 0.67) {
            // If density is not achieved, throw an exception
            throw new IllegalStateException("should achieve 0.67");
        }
        return density;
    }

    public float calGridOrientation(float wordsNum, float direction) {
        // The number of words in the starting grid shall be at least 15% of each of the
        // 5 orientations
        return 0;
    }

    // Method to place words in the grid
    private void placeWordsInGrid() {
        // ADD CODE
    }

    // Method to place letters in the grid
    private void placeLettersInGrid() {
        // ADD CODE
    }

    // Method to initialize the grid with empty cells
    private void initializeGrid() {
        for (int i = 0; i < sizeGrid * sizeGrid; i++) {
            letters[i] = '-';
        }
    }
}