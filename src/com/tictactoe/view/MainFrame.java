
/**
 * Problem No. #101
 * Difficulty: Intermediate
 * Description: Main Application Frame using CardLayout for State Management
 * Link: N/A
 * Time Complexity: N/A
 * Space Complexity: N/A
 */

package com.tictactoe.view;

import com.tictactoe.controller.GameController;
import com.tictactoe.controller.NavigationController;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private  CardLayout cardLayout;
    private  JPanel mainContainer;
    private NavigationController nav;
    private GameController gameController;

    public MainFrame() {
        // 1. Basic Window Setup
        setTitle("Tic-Tac-Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null); // Center on screen

        // 2. Layout & Controller Initialization
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        nav = new NavigationController(mainContainer);

        // Initialize GameController with 'this' frame for dialog centering
        gameController = new GameController(this, nav);

        // 3. View Initialization & Connection
        // Creating panels and injecting dependencies
        StartupPanel startupPanel = new StartupPanel(nav, gameController);
        UserLoginPanel loginPanel = new UserLoginPanel(nav);
        GamePanel gamePanel = new GamePanel(gameController);

        // Connect the GameController to the View components
        gameController.setLoginPanel(loginPanel);
        gameController.setGamePanel(gamePanel);

        mainContainer.add(startupPanel, "STARTUP");
        mainContainer.add(loginPanel,"LOGIN");
        mainContainer.add(gamePanel,"GAME");

        add(mainContainer);

        // 4. Show Initial Screen
        cardLayout.show(mainContainer, "STARTUP");
        nav.showStartup();
    }
}