/**
 * Problem No. #104
 * Difficulty: Easy
 * Description: Model representing a player entity with type-based behavior
 * Link: N/A
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */

package com.tictactoe.model;

public class Player {
    // Enum to define types for easier logic handling in the Controller
    public enum PlayerType {
        REGISTERED, ANONYMOUS, AI
    }

    private String name;
    private String symbol; // "X" or "O"
    private PlayerType type;

    public Player(String name, String symbol, PlayerType type) {
        this.name = name;
        this.symbol = symbol;
        this.type = type;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public PlayerType getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + " (" + type + ") as " + symbol;
    }
}
