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
    private final JLabel difficultyLabel; // Changed from JComboBox to JLabel
    private final BoardPanel boardPanel;
    private final ScoreBoardPanel scoreBoard;

    public GamePanel(GameController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(48, 10, 36)); // Ubuntu Purple
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- TOP BAR (North) ---
        JPanel topBar = new JPanel(new BorderLayout(15, 0));
        topBar.setOpaque(false);

        this.boardPanel = new BoardPanel(controller);
        this.scoreBoard = new ScoreBoardPanel();

        // 1. Navigation Group (Left side)
        JPanel navGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        navGroup.setOpaque(false);

        JButton homeButton = new JButton("Home");
        styleNavButton(homeButton);
        homeButton.addActionListener(e -> handleHomeRequest());

        JButton lobbyButton = new JButton("Lobby");
        styleNavButton(lobbyButton);
        lobbyButton.addActionListener(e -> handleLobbyRequest());

        navGroup.add(homeButton);
        navGroup.add(lobbyButton);

        // 2. Status Label (Center)
        statusLabel = new JLabel("Your Turn!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Ubuntu", Font.BOLD, 18));
        statusLabel.setForeground(Color.WHITE);

        // 3. Persistent Difficulty Display (Right side)
        difficultyLabel = new JLabel("Level: Easy");
        difficultyLabel.setFont(new Font("Ubuntu", Font.ITALIC, 16));
        difficultyLabel.setForeground(new Color(240, 119, 70)); // Canonical Orange
        difficultyLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        // --- ADD COMPONENTS TO THE TOPBAR ---
        topBar.add(navGroup, BorderLayout.WEST);
        topBar.add(statusLabel, BorderLayout.CENTER);
        topBar.add(difficultyLabel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(scoreBoard, BorderLayout.SOUTH);
    }

    public void setDifficultyDisplay(String level) {
        difficultyLabel.setText("Level: " + level);

        // Dynamic coloring for a professional touch
        switch (level.toUpperCase()) {
            case "HARD" -> difficultyLabel.setForeground(Color.RED);
            case "MEDIUM" -> difficultyLabel.setForeground(Color.YELLOW);
            default -> difficultyLabel.setForeground(new Color(240, 119, 70));
        }
    }

    // ... (rest of your existing delegation methods: updateButton, updateScore, etc.)

    public void updateStatus(String text) {
        statusLabel.setText(text);
    }

    private void styleNavButton(JButton btn) {
        btn.setBackground(new Color(119, 41, 83));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Ubuntu", Font.PLAIN, 14));
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

    private void handleHomeRequest() {
        int confirm = JOptionPane.showConfirmDialog(this, "Quit match?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) controller.handleHomeNavigation();
    }

    private void handleLobbyRequest() {
        int confirm = JOptionPane.showConfirmDialog(this, "Return to lobby?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) controller.handleBackToLobby();
    }
}