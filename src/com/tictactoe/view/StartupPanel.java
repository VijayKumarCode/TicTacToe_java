/**
 * Problem No. #102
 * Difficulty: Easy
 * Description: Startup Panel for User Selection
 * Link: N/A
 * Time Complexity: N/A
 * Space Complexity: N/A
 */
package com.tictactoe.view;

import com.tictactoe.controller.GameController;
import com.tictactoe.controller.NavigationController;
import javax.swing.*;
import java.awt.*;

public class StartupPanel extends JPanel {
    private NavigationController nav;
    private GameController gameController;

    public StartupPanel(NavigationController nav, GameController gameController) {
        this.nav = nav;
        this.gameController = gameController;
        setLayout(new GridBagLayout()); // Centers everything
        JPanel content = new JPanel(new GridLayout(3, 1, 10, 10));
        JLabel title = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        title.setFont(new Font("Ubuntu", Font.BOLD, 24));

        JButton btnRegister = new JButton("Login / Register");
        JButton btnGuest = new JButton("Play as Guest");

        // Add dummy listeners for now to prove it works
        // >>> THIS CALL WILL HIGHLIGHT startNewGameFlow IN YOUR CONTROLLER <<<
        btnRegister.addActionListener(e -> nav.showLogin());

        btnGuest.addActionListener(e -> gameController.handleGuestLogin());

        content.add(title);
        content.add(btnRegister);
        content.add(btnGuest);

        add(content);
    }
}