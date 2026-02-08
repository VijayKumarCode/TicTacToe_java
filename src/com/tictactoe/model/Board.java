/**
 * Problem No. #101
 * Difficulty: Easy
 * Description: Model representing the 3x3 Tic-Tac-Toe grid and win-condition logic
 * Link: https://leetcode.com/problems/design-tic-tac-toe/
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */

package com.tictactoe.model;

public class Board {
    private final String[][] grid;

    public Board() {
        grid = new String[3][3];
        resetBoard();
    }

    public void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j] = "";
            }
        }
    }

    public boolean makeMove(int row, int col, String symbol) {
        if (row >= 0 && row < 3 && col >= 0 && col < 3 && grid[row][col].isEmpty()) {
            grid[row][col] = symbol;
            return true;
        }
        return false;
    }

    /**
     * NEW: Utility for AI to "peek" at a cell using a single index (0-8).
     */
    public String getSymbolAt(int index) {
        return grid[index / 3][index % 3];
    }

    /**
     * NEW: Utility for AI to simulate or undo moves during difficulty calculation.
     */
    public void setSymbolAt(int index, String symbol) {
        grid[index / 3][index % 3] = symbol;
    }

    public int[] getWinningIndices(String symbol) {
            if (symbol == null || symbol.isEmpty()) return null;

            // Check rows
            for (int i = 0; i < 3; i++) {
                if (grid[i][0].equals(symbol) && grid[i][1].equals(symbol) && grid[i][2].equals(symbol)) {
                    return new int[]{i * 3, i * 3 + 1, i * 3 + 2};
                }
            }

            // Check columns
            for (int i = 0; i < 3; i++) {
                if (grid[0][i].equals(symbol) && grid[1][i].equals(symbol) && grid[2][i].equals(symbol)) {
                    return new int[]{i, i + 3, i + 6};
                }
            }

            // Check diagonals
            if (grid[0][0].equals(symbol) && grid[1][1].equals(symbol) && grid[2][2].equals(symbol)) {
                return new int[]{0, 4, 8};
            }
            if (grid[0][2].equals(symbol) && grid[1][1].equals(symbol) && grid[2][0].equals(symbol)) {
                return new int[]{2, 4, 6};
            }

            return null;
        }

    public boolean isFull() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                // If ANY cell is empty/null, the board is NOT full
                if (grid[r][c] == null || grid[r][c].trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}