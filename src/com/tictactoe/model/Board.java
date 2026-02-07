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
        if (row >= 0 && row < 3 && col >= 0 && col < 3 && grid[row][col].isEmpty()) {
            grid[row][col] = symbol;
            return true;
        }
        return false;
    }

    public boolean checkWin(String symbol) {
        if (symbol == null || symbol.isEmpty()) return false;
        // Check rows, columns
        for (int i = 0; i < 3; i++) {
            if (grid[i][0].equals(symbol) && grid[i][1].equals(symbol) && grid[i][2].equals(symbol)) return true;
            if (grid[0][i].equals(symbol) && grid[1][i].equals(symbol) && grid[2][i].equals(symbol)) return true;
        }

        //diagonals
        if (grid[0][0].equals(symbol) && grid[1][1].equals(symbol) && grid[2][2].equals(symbol)) return true;
        return grid[0][2].equals(symbol) && grid[1][1].equals(symbol) && grid[2][0].equals(symbol);

    }
    public boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j].isEmpty()) return false;
            }
        }
        return true;
    }
}