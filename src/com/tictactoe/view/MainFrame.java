
/**
 * Problem No. #101
 * Difficulty: Intermediate
 * Description: Main Application Frame using CardLayout for State Management
 * Link: N/A
 * Time Complexity: N/A
 * Space Complexity: N/A
 */

package com.tictactoe.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private  CardLayout cardLayout;
    private  JPanel mainContainer;

    public MainFrame() {
        // 1. Basic Window Setup
        setTitle("Tic-Tac-Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null); // Center on screen

        // 2. Layout Manager Setup
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 3. Add Screens (Views)
        // We will add the StartupPanel here in the next step
        mainContainer.add(new StartupPanel(this), "STARTUP");

        add(mainContainer);

        // 4. Show Initial Screen
        cardLayout.show(mainContainer, "STARTUP");
    }

    // Method to switch screens dynamically
    public void switchTo(String viewName) {
        cardLayout.show(mainContainer, viewName);
    }
}