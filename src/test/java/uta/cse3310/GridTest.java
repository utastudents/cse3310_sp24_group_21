package uta.cse3310;

import junit.framework.TestCase;
import java.util.Arrays;
import java.util.List;

public class GridTest extends TestCase {
    private Grid.GridGen grid;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        grid = new Grid.GridGen();
        // Initialize the grid to empty state
        for (int i = 0; i < Grid.nRows; i++) {
            Arrays.fill(grid.cells[i], (char) 0);
        }
    }

    public void testReadWords() {
        // Testing reading words from the file
        List<String> words = Grid.readWords();
        assertFalse("Word list should not be empty", words.isEmpty());
    }

    public void testCreateGrid() {
        // Using shorter, more manageable words that should easily fit into the grid
        List<String> words = Arrays.asList("CAT", "DOG", "HAT", "LOG", "MOG", "RAT", "BAT", "SAG");
    
        // Invoke createGrid with a controlled list of words
        Grid.GridGen createdGrid = Grid.createGrid(words, 10); // Allow more than the minimum words
    
        assertNotNull("Grid should not be null after creation", createdGrid);
        assertTrue("Grid should have at least the minimum number of words placed", createdGrid.solutions.size() >= Grid.minWords);
        // Check if any words are placed at all
        assertTrue("At least one word should be placed to pass the test", createdGrid.solutions.size() > 0);
    
        //print out details for debugging
        System.out.println("Words placed: " + createdGrid.solutions.size());
        System.out.println("Attempts: " + createdGrid.numAttempts);
        System.out.println("Density: " + createdGrid.density);
    }
    

    public void testPlaceMessage() {
        // Testing message placement
        String message = "HELLO";
        int placedLength = Grid.placeMessage(grid, message);
        assertEquals("Placed message length should match the message length", message.length(), placedLength);
    }

    public void testTryPlaceWord() {
        // Testing word placement
        String word = "HELLO";
        int[] orientations = new int[8];
        int cellsFilled = Grid.tryPlaceWord(grid, word, orientations);
        assertTrue("Cells filled should be greater than zero after placing a word", cellsFilled > 0);
    }

    public void testTryLocation() {
        // Testing specific location for word placement
        String word = "TEST";
        int dir = 0; // Direction index for horizontal right
        int pos = 0; // Start position at the top-left corner of the grid
        int lettersPlaced = Grid.tryLocation(grid, word, dir, pos);
        assertTrue("Number of letters placed should match word length if no overlaps", lettersPlaced > 0);
    }

    public void testCountValidWordCharacters() {
        // Testing the count of characters in placed words
        grid.solutions.add("HELLO      (0, 0)(4,0)");
        grid.solutions.add("WORLD      (0, 1)(4,1)");
        int count = Grid.countValidWordCharacters(grid);
        assertEquals("Count of valid word characters should match the total letters in solutions", 10, count);
    }

    public void testUniformFillerDistribution() {
        // Test uniform distribution of filler characters
        int[] letterCount = new int[26]; // Initialize letter counts
        Arrays.fill(letterCount, 5); // Assume an initial distribution
        char filler = Grid.getUniformFiller(letterCount, 4, 10);
        assertTrue("Filler should be a valid uppercase letter", filler >= 'A' && filler <= 'Z');
        assertTrue("Filler should not exceed maximum occurrences", letterCount[filler - 'A'] <= 10);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        grid = null;
    }
}
