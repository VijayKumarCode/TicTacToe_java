/**
 * Problem No. #100
 * Difficulty: Beginner
 * Description: Application Entry Point
 * Link: N/A
 * Time Complexity: N/A
 * Space Complexity: N/A
 */
package com.tictactoe;

import com.tictactoe.view.MainFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Run GUI in the Event Dispatch Thread (Best Practice)
        SwingUtilities.invokeLater(() -> {
            MainFrame app = new MainFrame();
            app.setVisible(true);
        });
    }
}