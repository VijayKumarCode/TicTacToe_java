package com.tictactoe.view;

/*
 * Problem No. #131
 * Difficulty: Intermediate
 * Description: Refactored GamePanel acting as a container for BoardPanel and UI controls.
 * Link: https://docs.oracle.com/javase/tutorial/uiswing/layout/border.html
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */




import com.tictactoe.controller.GameController;
import com.tictactoe.view.components.BoardPanel;
import com.tictactoe.view.components.ScoreBoardPanel;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final GameController controller;
    private final JLabel statusLabel;
    private final BoardPanel boardPanel; // Reference to our new sub-panel
    private final ScoreBoardPanel scoreBoard;
    public GamePanel(GameController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(48, 10, 36)); // Ubuntu Dark Purple
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- TOP BAR (North) ---
        JPanel topBar = new JPanel(new BorderLayout(15, 0));
        topBar.setOpaque(false);

        JButton homeButton = new JButton("Home");
        styleNavButton(homeButton);
        homeButton.addActionListener(e -> handleHomeRequest());

        statusLabel = new JLabel("Your Turn!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Ubuntu", Font.BOLD, 18));
        statusLabel.setForeground(Color.WHITE);

        String[] levels = {"Easy", "Medium", "Hard"};
        JComboBox<String> difficultyBox = new JComboBox<>(levels);
        styleComboBox(difficultyBox);
        difficultyBox.addActionListener(e -> controller.setDifficulty((String) difficultyBox.getSelectedItem()));

        topBar.add(homeButton, BorderLayout.WEST);
        topBar.add(statusLabel, BorderLayout.CENTER);
        topBar.add(difficultyBox, BorderLayout.EAST);

        // --- BOARD GRID (Center) ---
        this.boardPanel = new BoardPanel(controller);

        // --- SCOREBOARD (South) ---
        this.scoreBoard = new ScoreBoardPanel(); // 2. Initialized

        // 3. Added to layout regions
        add(topBar, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(scoreBoard, BorderLayout.SOUTH);
    }

    // --- DELEGATION METHODS ---
    /**
     * Bridges the Controller to the ScoreBoard component.
     */
    public void updateScore(String winnerType) {
        if ("USER".equals(winnerType)) {
            scoreBoard.incrementUserWins();
        } else if ("AI".equals(winnerType)) {
            scoreBoard.incrementAIWins();
        } else if ("DRAW".equalsIgnoreCase(winnerType)){
            scoreBoard.incrementDraws();
        }
    }

    // Delegation Methods: Pass the request down to the boardPanel
    public void updateButton(int index, String symbol) {
        boardPanel.updateButton(index, symbol);
    }

    public void highlightWinningPath(int[] indices) {
        boardPanel.highlightWinningPath(indices);
    }

    public void clearBoard() {
        boardPanel.clearBoard();
        updateStatus("New Game Started");
    }

    public void updateStatus(String text) {
        statusLabel.setText(text);
    }

    private void styleNavButton(JButton btn) {
        btn.setBackground(new Color(119, 41, 83));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setBackground(new Color(119, 41, 83));
        box.setForeground(Color.WHITE);
        box.setOpaque(true);
    }

    private void handleHomeRequest() {
        int confirm = JOptionPane.showConfirmDialog(this, "Quit match?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) controller.handleHomeNavigation();
    }
}