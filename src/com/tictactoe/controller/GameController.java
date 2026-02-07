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

    private GamePanel gamePanel;
    private UserLoginPanel loginPanel;



    public GameController(JFrame parentFrame, NavigationController nav) {
        this.board = new Board();
        this.parentFrame = parentFrame;
        this.nav = nav;

        this.user = new Player("ANONYMOUS",  "X",Player.PlayerType.ANONYMOUS);
        this.ai = new Player("AI", "O",Player.PlayerType.AI);
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setLoginPanel(UserLoginPanel loginPanel) {
        this.loginPanel = loginPanel;
        this.loginPanel.addLoginListener(this::handleLogin);
    }
    public void handlePlayerMove(int index) {
        if (!isUserTurn()) return;

        int row = index / 3;
        int col = index % 3;

        // 1. Update Model (Highlights board.makeMove)
        if (board.makeMove(row, col, getUserSymbol())) {
            // 2. Update View
            gamePanel.updateButton(index, getUserSymbol());
            if (checkGameOver(getUserSymbol())) return;
            isUserTurn = false;
            // UX: Slight delay so AI move doesn't feel instant/robotic
            Timer timer = new Timer(500, e -> triggerAIMove());
            timer.setRepeats(false);
            timer.start();
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
        if (ai.getType() == Player.PlayerType.AI) {
            // Placeholder for future AI/Minimax implementation
            for (int i = 0; i < 9; i++) {
                int r = i / 3;
                int c = i % 3;

                // USE HERE: getAiSymbol() is now used to mark the board
                if (board.makeMove(r, c, getAiSymbol())) {
                    gamePanel.updateButton(i, getAiSymbol());

                    if (board.checkWin(getAiSymbol())) {
                        announceWinner(ai);
                        return;
                    }

                    isUserTurn = true;
                    break;
                }
            }
        }
    }

    private void announceWinner(Player winner) {
        // >>> THIS HIGHLIGHTS getName() <<<
        JOptionPane.showMessageDialog(parentFrame, winner.getName() + " has won the match!");
        startNewGameFlow();
    }

    private boolean checkGameOver(String symbol) {
        if (board.checkWin(symbol)) {
            Player winner = (user.getSymbol().equals(symbol)) ? user : ai;

            // USE HERE: winner.getName() will now reflect the registered username or "Guest"
            JOptionPane.showMessageDialog(parentFrame,
                    winner.getName() + " (" + symbol + ") Wins!",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);

            startNewGameFlow();
            return true;
        }
        if (board.isFull()) {
            JOptionPane.showMessageDialog(parentFrame, "It's a Draw!");
            startNewGameFlow();
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