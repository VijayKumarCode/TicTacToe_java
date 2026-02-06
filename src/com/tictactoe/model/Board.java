/**
 * Problem No. #103
 * Difficulty: Intermediate
 * Description: Logic for 3x3 Grid and Win Validation
 * Link: N/A
 * Time Complexity: O(1) - checking 8 possible win lines
 * Space Complexity: O(1) - fixed 3x3 array
 */

package com.tictactoe.model;

public class Board {
    private String[][] grid;

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
        if (grid[row][col].isEmpty()) { //grid[row][col].equals("") can also be use.
            grid[row][col] = symbol;
            return true;
        }
        return false;
    }

    public boolean checkWin(String symbol) {
        // Check rows, columns, and diagonals
        for (int i = 0; i < 3; i++) {
            if (grid[i][0].equals(symbol) && grid[i][1].equals(symbol) && grid[i][2].equals(symbol)) return true;
            if (grid[0][i].equals(symbol) && grid[1][i].equals(symbol) && grid[2][i].equals(symbol)) return true;
        }
        if (grid[0][0].equals(symbol) && grid[1][1].equals(symbol) && grid[2][2].equals(symbol)) return true;
        return grid[0][2].equals(symbol) && grid[1][1].equals(symbol) && grid[2][0].equals(symbol);
    }
}