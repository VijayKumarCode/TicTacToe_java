
/**
 * Problem No. #105
 * Difficulty: Intermediate
 * Description: The 3x3 Grid UI for Tic Tac Toe
 * Link: N/A
 * Time Complexity: O(n) where n is number of cells (9)
 * Space Complexity: O(n) to store button references
 */

package com.tictactoe.view;

import com.tictactoe.controller.GameController;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final JButton[] buttons = new JButton[9];
    private final GameController controller;
    private final JLabel statusLabel;

    public GamePanel(GameController controller) {
        this.controller = controller;

        // Industry Standard: Use GridLayout for a perfect square grid
        setLayout(new BorderLayout(10,10)); // 3x3 with 5px gaps
        setBackground(new Color(48, 10, 36)); // Ubuntu Dark Purple
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        statusLabel = new JLabel("Your Turn!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Ubuntu", Font.BOLD, 18));
        statusLabel.setForeground(Color.WHITE);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JButton homeButton = new JButton("Home");
        styleNavButton(homeButton);
        // This triggers the NavigationController via your GameController
        homeButton.addActionListener(e -> handleHomeRequest());

        topBar.add(homeButton, BorderLayout.WEST);
        topBar.add(statusLabel, BorderLayout.CENTER);

        // Center: Game Grid
        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 8, 8));
        gridPanel.setOpaque(false);

        initializeButtons(gridPanel);

        add(topBar, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
    }

    private void initializeButtons(JPanel panel) {
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Ubuntu", Font.BOLD, 50));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(Color.WHITE);
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            final int index = i;
            // CALL HERE: When a button is clicked, trigger the move logic
            buttons[i].addActionListener(e -> controller.handlePlayerMove(index));
            panel.add(buttons[i]);
        }
    }

    private void styleNavButton(JButton btn) {
        btn.setBackground(new Color(119, 41, 83)); // Ubuntu Purple
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Ubuntu", Font.PLAIN, 14));
    }

    private void handleHomeRequest() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Quit match and return to Main Menu?",
                "Confirm Exit", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // This is where you'd call your NavigationController logic
            // Assuming you add a showHome() or similar in GameController
            controller.handleHomeNavigation();
        }
    }

    public void updateStatus(String text) {
        statusLabel.setText(text);
    }
    // This allows the Controller to assign logic to the buttons

    public void updateButton(int index, String symbol) {
        if (index >= 0 && index < 9) {
            buttons[index].setText(symbol);
            buttons[index].setEnabled(false);

            // Set Ubuntu-themed colors for X and O
            if ("X".equals(symbol)) {
                buttons[index].setForeground(new Color(233, 84, 32)); // Ubuntu Orange
            } else {
                buttons[index].setForeground(new Color(119, 41, 83)); // Ubuntu Purple
            }
        }// Disable after click
    }

    public void highlightWinningPath(int[] indices) {
        if (indices == null) return;

        for (int index : indices) {
            if (index >= 0 && index < 9) {
                buttons[index].setBackground(new Color(56, 175, 87)); // Ubuntu Success Green
                buttons[index].setForeground(Color.WHITE);
                // Ensure the button looks "active" even if disabled
                buttons[index].setOpaque(true);
                buttons[index].setBorderPainted(true);
            }
        }
    }

    public void clearBoard() {
        for (JButton btn : buttons) {
            btn.setText("");
            btn.setEnabled(true);
            btn.setBackground(Color.WHITE);
        }
        updateStatus("New Game Started");
    }
}