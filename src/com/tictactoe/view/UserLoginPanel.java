/*
 * Problem No. #110
 * Difficulty: Intermediate
 * Description: Login UI with GridBagLayout and Ubuntu branding for registered users
 * Link: N/A
 * Time Complexity: O(1)
 * Space Complexity: O(1)
*/

package com.tictactoe.view;

import com.tictactoe.controller.NavigationController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * LoginPanel provides the UI for player registration and login.
 * Designed with a focus on clean layout and event delegation to the Controller.
 */

public class UserLoginPanel extends JPanel {

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final NavigationController nav;

    public UserLoginPanel(NavigationController nav) {
        this.nav = nav;

        this.usernameField = new JTextField(15);
        this.passwordField = new JPasswordField(15);
        this.loginButton = new JButton("Login");

        initComponents();
    }

    private void initComponents() {
        // Using GridBagLayout for a centered, professional look
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        // Title
        JLabel titleLabel = new JLabel("Player Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Ubuntu", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);


        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Login Button
        loginButton.setBackground(new Color(119, 41, 83)); // Ubuntu Purple
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        // Guest Mode Button
        JButton backButton = new JButton("Back");
        gbc.gridy = 4;
        add(backButton, gbc);

        backButton.addActionListener(e -> nav.showStartup());

    }

    // Methods to attach listeners (to be called by GameController)
    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }
}