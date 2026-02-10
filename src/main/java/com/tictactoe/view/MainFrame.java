package com.tictactoe.view;


/**
 * Problem No. #101
 * Difficulty: Intermediate
 * Description: Main Application Frame using CardLayout for State Management
 * Link: N/A
 * Time Complexity: N/A
 * Space Complexity: N/A
 */


import com.tictactoe.controller.GameController;
import com.tictactoe.controller.NavigationController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);

    public MainFrame() {
        // 1. Basic Window Setup
        setTitle("Tic-Tac-Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        // 2. Initialize Controllers
        NavigationController nav = new NavigationController(container);
        GameController gameController = new GameController(this, nav);

        // 3. View Initialization & Connection
        // Creating panels and injecting dependencies
        StartupPanel startupPanel = new StartupPanel(nav, gameController);
        GamePanel gamePanel = new GamePanel(gameController);
        LobbyPanel lobbyPanel = new LobbyPanel(nav,gameController);
        UserLoginPanel loginPanel = new UserLoginPanel(nav);

        //4. Connection (Dependency Injection)
        gameController.setGamePanel(gamePanel);
        gameController.setLoginPanel(loginPanel);
        gameController.setLobbyPanel(lobbyPanel);

        // Connect the GameController to the View components

        // 3. Register Panels to Container
        container.add(startupPanel, NavigationController.STARTUP);
        container.add(gamePanel, NavigationController.GAME);
        container.add(lobbyPanel, NavigationController.LOBBY);
        container.add(loginPanel, NavigationController.LOGIN);

        add(container);
        // 4. Show Initial Screen
        nav.showStartup();
    }
}