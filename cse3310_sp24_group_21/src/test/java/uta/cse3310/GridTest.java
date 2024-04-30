package uta.cse3310;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class GridTest {

    @Test
    public void testCreateGrid() {
        List<String> words = new ArrayList<>();
        words.add("TEST");
        words.add("GRID");

        Grid.GridGen grid = Grid.createGrid(words, 2); // Limiting to 2 words for testing
        assertNotNull(grid); // Ensure grid is not null
        assertTrue(grid.solutions.size() >= 1 && grid.solutions.size() <= 2); // Ensure correct number of words placed
    }

    @Test
    public void testCountValidWordCharacters() {
        Grid.GridGen grid = new Grid.GridGen();
        grid.solutions.add("HELLO (0, 0)(4,0)");
        grid.solutions.add("WORLD (0, 1)(4,1)");

        int count = Grid.countValidWordCharacters(grid);
        assertEquals(10, count); // Total characters in placed words
    }

    @Test
    public void testCalculateUniformness() {
        double[] letterCounts = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
        int totalFillerCount = 25;
    
        double uniformness = Grid.calculateUniformness(letterCounts, totalFillerCount);
        assertTrue(uniformness > 0.9); // Ensure uniformness is above 0.85
    }
    
    
    @Test
    public void testTryLocation() {
        Grid.GridGen grid = new Grid.GridGen();
        grid.cells = new char[5][5];
    
        int lettersPlaced = Grid.tryLocation(grid, "HELLO", 0, 0); // Attempt to place "HELLO" horizontally at (0, 0)
        assertEquals(5, lettersPlaced); // Ensure 5 letters are placed
        assertEquals("HELLO", grid.solutions.get(0).split("\\s+")[0]); // Verify correct word placement
    }
}