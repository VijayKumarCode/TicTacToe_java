/**
 * Problem No. #108
 * Difficulty: Intermediate
 * Description: Controller managing game flow, Toss Authority, and turn transitions
 * Link: https://leetcode.com/problems/design-tic-tac-toe/
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */

package com.tictactoe.controller;

import com.tictactoe.model.Board;
import com.tictactoe.model.Player;
import com.tictactoe.view.GamePanel;
import com.tictactoe.view.UserLoginPanel;
import com.tictactoe.view.components.TossDialog;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class GameController {
    private final Board board;
    private final JFrame parentFrame;
    private final NavigationController nav;

    private Player user;
    private Player ai;
    private boolean isUserTurn;

    private String currentDifficulty = "Easy";

    private GamePanel gamePanel;
    private UserLoginPanel loginPanel;
    private Timer aiTimer;



    public GameController(JFrame parentFrame, NavigationController nav) {
        this.board = new Board();
        this.parentFrame = parentFrame;
        this.nav = nav;

        this.user = new Player("ANONYMOUS",  "X",Player.PlayerType.ANONYMOUS);
        this.ai = new Player("AI", "O",Player.PlayerType.AI);
    }

    public void setDifficulty(String level) {
        this.currentDifficulty = level;
        // Industry Tip: Use a status update instead of just a Print statement
        if (gamePanel != null) {
            gamePanel.updateStatus("Difficulty: " + level);
        }
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setLoginPanel(UserLoginPanel loginPanel) {
        this.loginPanel = loginPanel;
        this.loginPanel.addLoginListener(this::handleLogin);
    }
    public void handlePlayerMove(int index) {
        // 1. Guard Clauses: Ensure it's the user's turn and the game isn't over
        if (!isUserTurn()) return;

        int row = index / 3;
        int col = index % 3;

        // 2. Update Model (Logical board)
        if (board.makeMove(row, col, getUserSymbol())) {

            // 3. Update View (Visual board)
            gamePanel.updateButton(index, getUserSymbol());

            // 4. Check Termination: Win or Draw
            if (checkGameOver(getUserSymbol())) return;

            // 5. Switch Turn
            isUserTurn = false;

            // 6. Trigger AI with Lifecycle Management
            // We assign the timer to our class field so handleHomeNavigation() can stop it
            aiTimer = new Timer(500, e -> triggerAIMove());
            aiTimer.setRepeats(false);
            aiTimer.start();
        }
    }
    private void handleLogin(ActionEvent e) {
        // Now these methods in UserLoginPanel will be USED (highlighted)
        String username = loginPanel.getUsername();
        char[] password = loginPanel.getPassword();

        // Basic validation logic
        if (username != null && !username.trim().isEmpty() && password.length > 0) {
            JOptionPane.showMessageDialog(parentFrame, "Login Successful! Welcome, " + username + ".");
            this.user = new Player(username, "X", Player.PlayerType.REGISTERED);
            this.ai = new Player("Computer", "O", Player.PlayerType.AI);

            nav.showGame();

            startNewGameFlow();
            // Here you would typically navigate to the game or lobby
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Invalid Credentials. Please try again.",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleGuestLogin() {
        // 1. Set up the guest identities
        this.user = new Player("Guest", "X", Player.PlayerType.ANONYMOUS);
        this.ai = new Player("AI", "O", Player.PlayerType.AI);

        // 2. Switch the screen (This makes the GamePanel visible)
        nav.showGame();

        // 3. Prepare the board and start the Toss
        startNewGameFlow();
    }

    public void handleHomeNavigation() {
        if (aiTimer != null && aiTimer.isRunning()) {
            aiTimer.stop(); // The AI "thinking" is canceled immediately
        }
        nav.showStartup();
    }

    public void startNewGameFlow() {
        // 1. Perform the Toss
        // CALL HERE: Clear the UI buttons before starting a new round
        if (gamePanel != null) {
            gamePanel.clearBoard();
        }

        // Reset the internal logical board
        board.resetBoard();
        boolean userWonToss = Math.random() < 0.5;

        if (userWonToss) {
            handleUserTossWin();
        } else {
            handleAITossWin();
        }
        if (!isUserTurn) {
            triggerAIMove();
        }
    }

    private void handleUserTossWin() {

        TossDialog dialog = new TossDialog(parentFrame,"You");
        dialog.setVisible(true);
        // Your requirement: Authority to play or pass
        if (dialog.isPlayFirst()) {
            user.setSymbol("X");
            ai.setSymbol("O");
            isUserTurn = true;
        } else {
            user.setSymbol("O");
            ai.setSymbol("X");
            isUserTurn = false;
        }
    }

    private void handleAITossWin() {
        JOptionPane.showMessageDialog(parentFrame, "AI won the toss and chose to play first (X)!");

        // 1. We can recreate the AI player, or just update it
        this.ai = new Player("Computer", "X", Player.PlayerType.AI);

        // 2. STOP! Do NOT do: this.user = new Player(...)
        // Instead, just update the existing user's symbol
        if (this.user != null) {
            this.user.setSymbol("O");
        } else {
            // Fallback only if somehow user wasn't initialized
            this.user = new Player("Guest", "O", Player.PlayerType.ANONYMOUS);
        }

        isUserTurn = false;
    }

    private void triggerAIMove() {
        // 1. Safety check
        if (ai.getType() != Player.PlayerType.AI) return;

        int moveIndex = switch (currentDifficulty) {
            case "Hard" -> getBestMoveMinimax();
            case "Medium" -> getSmartMove();
            default -> getRandomMove();
        };

        // 2. Strategy Switching based on currentDifficulty

        // 3. Execute the chosen move
        if (moveIndex != -1) {
            executeAIMove(moveIndex);
        }
    }

    private void executeAIMove(int index) {
        int r = index / 3;
        int c = index % 3;

        if (board.makeMove(r, c, getAiSymbol())) {
            gamePanel.updateButton(index, getAiSymbol());

            if (checkGameOver(getAiSymbol())) return;

            isUserTurn = true;
            gamePanel.updateStatus("Your Turn (" + getUserSymbol() + ")");
        }
    }

    /**
     * EASY LOGIC: Picks a random empty spot.
     */
    private int getRandomMove() {
        java.util.List<Integer> available = new java.util.ArrayList<>();
        for (int i = 0; i < 9; i++) {
            // Checking if the square is empty
            if (board.getSymbolAt(i).isEmpty()) {
                available.add(i);
            }
        }
        if (available.isEmpty()) return -1;
        return available.get(new java.util.Random().nextInt(available.size()));
    }

    /**
     * MEDIUM LOGIC: Wins if possible, blocks if necessary, otherwise moves randomly.
     */
    private int getSmartMove() {
        // 1. Can AI win in one move?
        for (int i = 0; i < 9; i++) {
            if (canMoveLeadToWin(i, getAiSymbol())) return i;
        }

        // 2. Is User about to win? Block!
        for (int i = 0; i < 9; i++) {
            if (canMoveLeadToWin(i, getUserSymbol())) return i;
        }

        // 3. Otherwise, fall back to random
        return getRandomMove();
    }

    private int getBestMoveMinimax() {
        int bestScore = Integer.MIN_VALUE;
        int move = -1;

        for (int i = 0; i < 9; i++) {
            if (board.getSymbolAt(i).isEmpty()) {
                board.setSymbolAt(i, getAiSymbol()); // Try move
                int score = minimax(board, 0, false);
                board.setSymbolAt(i, ""); // Undo move

                if (score > bestScore) {
                    bestScore = score;
                    move = i;
                }
            }
        }
        return move;
    }

    private int minimax(Board b, int depth, boolean isMaximizing) {
        // Base Cases: Check for terminal states
        if (b.getWinningIndices(getAiSymbol()) != null) return 10 - depth;
        if (b.getWinningIndices(getUserSymbol()) != null) return depth - 10;
        if (b.isFull()) return 0;

        int bestScore;
        if (isMaximizing) {
            bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (b.getSymbolAt(i).isEmpty()) {
                    b.setSymbolAt(i, getAiSymbol());
                    int score = minimax(b, depth + 1, false);
                    b.setSymbolAt(i, "");
                    bestScore = Math.max(score, bestScore);
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (b.getSymbolAt(i).isEmpty()) {
                    b.setSymbolAt(i, getUserSymbol());
                    int score = minimax(b, depth + 1, true);
                    b.setSymbolAt(i, "");
                    bestScore = Math.min(score, bestScore);
                }
            }
        }
        return bestScore;
    }

    /**
     * SIMULATION LOGIC: Temporarily places a symbol to see if it results in a win.
     */
    private boolean canMoveLeadToWin(int index, String symbol) {
        if (!board.getSymbolAt(index).isEmpty()) return false;

        // Simulation: Try the move
        board.setSymbolAt(index, symbol);
        boolean wins = (board.getWinningIndices(symbol) != null);

        // Cleanup: Undo the move immediately!
        board.setSymbolAt(index, "");
        return wins;
    }

    private void announceWinner(Player winner) {
        // Professional touch: Include the symbol in the announcement
        String message = String.format("%s (%s) has won the match!",
                winner.getName(), winner.getSymbol());

        JOptionPane.showMessageDialog(parentFrame, message, "Victory", JOptionPane.INFORMATION_MESSAGE);
        startNewGameFlow();
    }

    private void announceDraw() {
        JOptionPane.showMessageDialog(parentFrame, "The match is a Draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        startNewGameFlow();
    }

    private boolean checkGameOver(String symbol) {
        int[] winningIndices = board.getWinningIndices(symbol);

        if (winningIndices != null) {
            if (gamePanel != null) {
                gamePanel.highlightWinningPath(winningIndices);
                gamePanel.updateStatus("Game Over!");
            }

            Player winner = (user.getSymbol().equals(symbol)) ? user : ai;
            announceWinner(winner);
            return true;
        }

        // 2. Check for Draw (The Board must have an isFull() method)
        if (board.isFull()) {
            announceDraw(); // Delegate to the specialized method
            return true;
        }

        return false;
    }

    public boolean isUserTurn() {
        return isUserTurn;
    }
    public String getUserSymbol() {
        return user != null ? user.getSymbol() : "";
    }
    public String getAiSymbol() {
        return ai != null ? ai.getSymbol() : "";
    }
}