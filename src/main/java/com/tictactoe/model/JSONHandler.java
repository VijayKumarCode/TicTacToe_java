package com.tictactoe.model;


/**
 * Problem No. #103
 * Difficulty: Intermediate
 * Description: Utility for reading/writing player data to JSON for lobby persistence
 * Link: https://github.com/VijayKumarCode/TicTacToe_Project
 * Time Complexity: O(n) where n is the number of players
 * Space Complexity: O(n)
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class JSONHandler {
    private static final String FILE_PATH = "leaderboard.json";

    /**
     * Reads the JSON file and returns a list of Players.
     * For now, we use manual parsing to keep it simple without external libraries.
     */
    public static List<Player> loadPlayers() {
        List<Player> players = new ArrayList<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists() || file.length() == 0) {
                // If file is empty, create default players so the list isn't blank
                players.add(new Player("Ubuntu_Bot", "X", Player.PlayerType.AI, "Online"));
                players.add(new Player("Linus", "O", Player.PlayerType.REGISTERED, "Online"));
                savePlayers(players); // This triggers the save for the first time
                return players;
            }

            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            // For now, since we aren't using GSON, let's manually re-add them
            // to verify the UI works.
            players.add(new Player("Ubuntu_Bot", "X", Player.PlayerType.AI, "Online"));
            players.add(new Player("Linus", "O", Player.PlayerType.REGISTERED, "Online"));

        } catch (IOException e) {
            System.err.println("Error reading " + FILE_PATH + ": " + e.getMessage());
        }
        return players;
        }
    /**
     * Saves the list of players back to the JSON file.
     */

    public static void savePlayers(List<Player> players) {
        try {
            // Build a basic JSON array string manually for now
            StringBuilder sb = new StringBuilder("[\n");
            for (int i = 0; i < players.size(); i++) {
                Player p = players.get(i);
                sb.append(String.format("  {\"name\": \"%s\", \"status\": \"%s\"}",
                        p.getName(), p.getStatus()));
                if (i < players.size() - 1) sb.append(",");
                sb.append("\n");
            }
            sb.append("]");

            Files.write(Paths.get(FILE_PATH), sb.toString().getBytes());
            System.out.println("Saved " + players.size() + " players to " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Save failed: " + e.getMessage());
        }
    }
}
