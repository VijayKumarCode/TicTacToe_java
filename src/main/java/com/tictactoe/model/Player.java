package com.tictactoe.model;

/**
 * Problem No. #104
 * Difficulty: Easy
 * Description: Model representing a player entity with type-based behavior
 * Link: N/A
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */



public class Player {
    // Enum to define types for easier logic handling in the Controller
    public enum PlayerType {
        REGISTERED, ANONYMOUS, AI
    }

    private String name;
    private String symbol; // "X" or "O"
    private PlayerType type;
    private String status;

    public Player(String name, String symbol, PlayerType type,String status) {
        this.name = name;
        this.symbol = symbol;
        this.type = type;
        this.status = status;
    }
    // Getters and Setters
    public String getStatus() {
        return status;
    }


     // It is also good practice to add a setter so you can update it later
    public void setStatus(String status) {
        this.status = status;
    }
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
        return name + " (" + status + ")";
    }
}
