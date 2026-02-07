/**
 * Problem No. #111
 * Difficulty: Intermediate
 * Description: Custom modal dialog for the Toss Authority mechanism
 * Link: N/A
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */
package com.tictactoe.view.components;

import javax.swing.*;
import java.awt.*;

public class TossDialog extends JDialog {
    private boolean playFirst = true; // Default choice
    private String winnerName;

    public TossDialog(JFrame parent, String winnerName) {
        super(parent, "Toss Authority", true);
        this.winnerName = winnerName;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setSize(350, 200);
        setLocationRelativeTo(getParent());
        setResizable(false);

        // Header Section
        JPanel header = new JPanel();
        header.setBackground(new Color(119, 41, 83)); // Ubuntu Purple
        JLabel title = new JLabel(winnerName + " won the Toss!");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Ubuntu", Font.BOLD, 16));
        header.add(title);

        // Choice Section
        JPanel body = new JPanel(new GridBagLayout());
        JLabel instruction = new JLabel("Choose your opening strategy:");
        instruction.setFont(new Font("Ubuntu", Font.PLAIN, 14));

        JButton btnPlay = new JButton("Play First (X)");
        JButton btnPass = new JButton("Pass Turn (O)");

        // Action Listeners
        btnPlay.addActionListener(e -> {
            playFirst = true;
            dispose();
        });

        btnPass.addActionListener(e -> {
            playFirst = false;
            dispose();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        body.add(instruction, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        body.add(btnPlay, gbc);

        gbc.gridx = 1;
        body.add(btnPass, gbc);

        add(header, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
    }

    public boolean isPlayFirst() {
        return playFirst;
    }
}