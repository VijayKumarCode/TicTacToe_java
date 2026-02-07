
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
    private JButton[] buttons = new JButton[9];
    private GameController controller;

    public GamePanel(GameController controller) {
        this.controller = controller;

        // Industry Standard: Use GridLayout for a perfect square grid
        setLayout(new GridLayout(3, 3, 5, 5)); // 3x3 with 5px gaps
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(48, 10, 36));

        initializeButtons();
    }

    private void initializeButtons() {
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Ubuntu", Font.BOLD, 50));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(Color.WHITE);
            final int index = i;
            // CALL HERE: When a button is clicked, trigger the move logic
            buttons[i].addActionListener(e -> controller.handlePlayerMove(index));
            add(buttons[i]);
        }
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

    public void clearBoard() {
        for (JButton btn : buttons) {
            btn.setText("");
            btn.setEnabled(true);
        }
    }
}