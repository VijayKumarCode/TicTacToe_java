package com.tictactoe.view.components;

/**
 * Problem No. #130
 * Difficulty: Intermediate
 * Description: Dedicated BoardPanel to manage the 3x3 grid logic and visual states.
 * Link: https://docs.oracle.com/javase/tutorial/uiswing/layout/grid.html
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */



import com.tictactoe.controller.GameController;
import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private final JButton[] buttons = new JButton[9];
    private final GameController controller;

    public BoardPanel(GameController controller) {
        this.controller = controller;

        // Setup the Grid
        setLayout(new GridLayout(3, 3, 8, 8));
        setOpaque(false);
        initializeButtons();
    }

    private void initializeButtons() {
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Ubuntu", Font.BOLD, 50));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(Color.WHITE);
            buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttons[i].setOpaque(true);

            final int index = i;
            buttons[i].addActionListener(e -> controller.handleCellClick(index));
            add(buttons[i]);
        }
    }

    public void updateButton(int index, String symbol) {
        if (index >= 0 && index < 9) {
            buttons[index].setText(symbol);
            buttons[index].setEnabled(false);

            if ("X".equals(symbol)) {
                buttons[index].setForeground(new Color(233, 84, 32)); // Ubuntu Orange
            } else {
                buttons[index].setForeground(new Color(119, 41, 83)); // Ubuntu Purple
            }
        }
    }

    public void highlightWinningPath(int[] indices) {
        if (indices == null) return;
        for (int index : indices) {
            if (index >= 0 && index < 9) {
                buttons[index].setBackground(new Color(56, 175, 87)); // Ubuntu Success Green
                buttons[index].setForeground(Color.WHITE);
            }
        }
    }

    public void clearBoard() {
        for (JButton btn : buttons) {
            btn.setText("");
            btn.setEnabled(true);
            btn.setBackground(Color.WHITE);
        }
    }
}