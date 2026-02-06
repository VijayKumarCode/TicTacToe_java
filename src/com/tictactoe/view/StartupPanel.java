/**
 * Problem No. #102
 * Difficulty: Easy
 * Description: Startup Panel for User Selection
 * Link: N/A
 * Time Complexity: N/A
 * Space Complexity: N/A
 */
package com.tictactoe.view;

import javax.swing.*;
import java.awt.*;

public class StartupPanel extends JPanel {
    private MainFrame parentFrame;

    public StartupPanel(MainFrame frame) {
        this.parentFrame = frame;
        setLayout(new GridBagLayout()); // Centers everything

        JPanel content = new JPanel(new GridLayout(3, 1, 10, 10));

        JLabel title = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        title.setFont(new Font("Ubuntu", Font.BOLD, 24));

        JButton btnRegister = new JButton("Login / Register");
        JButton btnGuest = new JButton("Play as Guest");

        // Add dummy listeners for now to prove it works
        btnRegister.addActionListener(e -> JOptionPane.showMessageDialog(this, "Go to Login (To Be Implemented)"));
        btnGuest.addActionListener(e -> JOptionPane.showMessageDialog(this, "Start AI Game (To Be Implemented)"));

        content.add(title);
        content.add(btnRegister);
        content.add(btnGuest);

        add(content);
    }
}