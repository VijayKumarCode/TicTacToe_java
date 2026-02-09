package com.tictactoe.model;

/**
 * Problem No. #134
 * Difficulty: Intermediate
 * Description: Unit testing suite for Tic-Tac-Toe Board logic using JUnit 5.
 * Link: https://junit.org/junit5/docs/current/user-guide/
 * Time Complexity: O(1) per test
 * Space Complexity: O(1)
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(); // Fresh board before every test
    }

    // Helper method to simulate a "checkWin" since Board uses getWinningIndices
    private boolean checkWin(String symbol) {
        return board.getWinningIndices(symbol) != null;
    }

    @Test
    @DisplayName("Should detect a horizontal win")
    void testHorizontalWin() {
        board.makeMove(0, 0, "X");
        board.makeMove(0, 1, "X");
        board.makeMove(0, 2, "X");

        assertTrue(checkWin("X"), "X should have won on the top row");
        assertArrayEquals(new int[]{0, 1, 2}, board.getWinningIndices("X"));
    }

    @Test
    @DisplayName("Should detect a diagonal win")
    void testDiagonalWin() {
        board.makeMove(0, 0, "O");
        board.makeMove(1, 1, "O");
        board.makeMove(2, 2, "O");

        // FIX: getWinningIndices returns an array, so we check if it is not null
        assertNotNull(board.getWinningIndices("O"), "O should have won on the main diagonal");
        assertArrayEquals(new int[]{0, 4, 8}, board.getWinningIndices("O"));
    }

    @Test
    @DisplayName("Should return false for isFull on empty board")
    void testIsFullOnEmpty() {
        assertFalse(board.isFull());
    }

    @Test
    @DisplayName("Should correctly identify a draw state")
    void testIsFullOnDraw() {
        // Fill board in a draw pattern
        String[] moves = {"X", "O", "X", "X", "O", "O", "O", "X", "X"};
        for (int i = 0; i < 9; i++) {
            board.setSymbolAt(i, moves[i]);
        }
        assertTrue(board.isFull(), "Board should be reported as full");
        assertNull(board.getWinningIndices("X"), "X should not have won");
        assertNull(board.getWinningIndices("O"), "O should not have won");
    }

    @Test
    @DisplayName("Should not allow moving on an occupied cell")
    void testInvalidMove() {
        board.makeMove(1, 1, "X");
        boolean result = board.makeMove(1, 1, "O");
        assertFalse(result, "Should not be able to overwrite a move");
        assertEquals("X", board.getSymbolAt(4)); // Index 4 is (1,1)
    }
}
