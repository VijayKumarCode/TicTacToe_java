package com.tictactoe.controller;

/*
 * Problem No. #108
 * Difficulty: Intermediate
 * Description: Controller managing game flow, Toss Authority, and turn transitions
 * Link: https://leetcode.com/problems/design-tic-tac-toe/
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */
import com.tictactoe.network.NetworkManager;

import com.tictactoe.model.JSONHandler;
import com.tictactoe.view.LobbyPanel;
import com.tictactoe.view.components.TossDialog;
import com.tictactoe.model.Board;
import com.tictactoe.model.Player;
import com.tictactoe.view.GamePanel;
import com.tictactoe.view.UserLoginPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class GameController {
    private boolean isGameActive = true;

    // --- NETWORK FIELDS ---
    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isMyTurn; // Vital for multi-player sync
    private String mySymbol;

    // 1. Final Dependencies (Immutable - These never change)
    private final Board board;
    private final JFrame parentFrame;
    private final NavigationController nav;

    // 2. Game State & Logic (Mutable)
    private Player user;
    private Player ai;       // Generic computer opponent
    private Player opponent; // Specific opponent selected from Lobby
    private boolean isUserTurn;

    public enum Difficulty { EASY, MEDIUM, HARD }
    private Difficulty difficulty = Difficulty.EASY;

    // 3. View Components & Utilities (Lifecycle managed)
    private GamePanel gamePanel;
    private UserLoginPanel loginPanel;
    private LobbyPanel lobbyPanel;
    private NetworkManager networkManager;
    private Timer aiTimer;

    public GameController(JFrame parentFrame, NavigationController nav) {
        this.board = new Board();
        this.parentFrame = parentFrame;
        this.nav = nav;

        // To this:
        this.user = new Player("Guest", "X", Player.PlayerType.ANONYMOUS, "Online");
        this.ai = new Player("AI", "O",Player.PlayerType.AI,"Online");
    }

    public void initiateNetworkMatch(String ip) {
        // 1. Ensure the UI updates immediately
        if (gamePanel != null) {
            nav.showGame(); // Move to game screen first so user sees the status
        }

        // 2. Route to the correct internal networking logic
        if (ip == null || ip.trim().isEmpty()) {
            // You are the Host
            startAsServer();
        } else {
            // You are the Client
            startAsClient(ip);
        }
    }

    // --- CONNECTION LOGIC ---

    public void startAsServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(5555);
                updateStatus("Waiting for WiFi opponent...");
                socket = serverSocket.accept();
                mySymbol = "X";
                isMyTurn = true;
                setupStreams();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void startAsClient(String targetIp) {
        new Thread(() -> {
            try {
                socket = new Socket(targetIp, 5555);
                mySymbol = "O";
                isMyTurn = false;
                setupStreams();
            } catch (IOException e) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(null, "Host not found on WiFi!"));
            }
        }).start();
    }


    public void setDifficulty(String level) {
        try {
            this.difficulty = Difficulty.valueOf(level.toUpperCase());
            if (gamePanel != null) {
                gamePanel.updateStatus("Difficulty: " + level);
            }
        } catch (IllegalArgumentException e) {
            this.difficulty = Difficulty.EASY;
        }
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;

        // Logic: If we are starting a match from the Lobby,
        // we replace the generic AI with the specific opponent selected.
        this.ai = opponent;

        // Phase 2 Update: Mark them as 'In Game'
        this.opponent.setStatus("In Game");

        System.out.println("Match initialized against: " + opponent.getName());
    }


    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setLoginPanel(UserLoginPanel loginPanel) {
        if (loginPanel == null) return;

        this.loginPanel = loginPanel;

        // This is where the highlighting happens:
        // It links the UI button click to the 'handleLogin' method below.
        this.loginPanel.addLoginListener(this::handleLogin);
    }

    public void setLobbyPanel(LobbyPanel lobbyPanel) {
        this.lobbyPanel = lobbyPanel;
    }
    public void handleCellClick(int index) {
        // Check if game is active AND it is the player's turn (handles both AI and WiFi)
        if (!isGameActive || (!isUserTurn && !isMyTurn)) return;

        // WiFi logic: Send move if connected
        if (out != null) {
            out.println("MOVE:" + index);
            isMyTurn = false; // Turn switch for WiFi
        }

        // AI logic: Switch turn for AI
        if (ai != null && ai.getType() == Player.PlayerType.AI) {
            isUserTurn = false;
        }

        executeMove(index, mySymbol != null ? mySymbol : user.getSymbol());

        // Update status label based on the next action
        if (out != null) {
            updateStatus("Opponent's Turn...");
        } else if (ai != null && ai.getType() == Player.PlayerType.AI) {
            updateStatus("AI is thinking...");
            aiTimer = new Timer(500, e -> triggerAIMove());
            aiTimer.setRepeats(false);
            aiTimer.start();
        }
    }

    // 3. Update your handleLogin method
    private void handleLogin(ActionEvent e) {
        String username = loginPanel.getUsername();
        char[] password = loginPanel.getPassword();

        if (username != null && !username.trim().isEmpty() && password.length > 0) {
            this.user = new Player(username, "X", Player.PlayerType.REGISTERED, "Online");

            // Navigate to the Lobby screen
            nav.showLobby();

            // --- NEW: Trigger the data refresh ---
            if (this.lobbyPanel != null) {
                this.lobbyPanel.refreshLobby(); // This calls JSONHandler.loadPlayers()
            }
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Please enter both username and password.");
        }
    }

    public void handleGuestLogin() {
        this.user = new Player("Guest", "X", Player.PlayerType.ANONYMOUS, "Online");
        this.ai = new Player("AI", "O", Player.PlayerType.AI, "Online");

        // 1. Switch the screen first
        nav.showGame();

        // 2. Wait for the UI to "breathe" and draw the buttons
        SwingUtilities.invokeLater(() -> {
            startNewGameFlow();
        });
    }

    public void handleHomeNavigation() {
        if (aiTimer != null && aiTimer.isRunning()) {
            aiTimer.stop(); // The AI "thinking" is canceled immediately
        }
        nav.showStartup();
    }

    public void handleBackToLobby() {
        if (user != null) user.setStatus("Online");

        // Save the updated status to the file!
        List<Player> allPlayers = JSONHandler.loadPlayers();
        // (Logic to update the specific user in the list would go here)
        JSONHandler.savePlayers(allPlayers);

        nav.showLobby();
        if (lobbyPanel != null) lobbyPanel.refreshLobby();

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

        TossDialog dialog = new TossDialog(parentFrame, "You");
        dialog.setVisible(true);

        if (dialog.isPlayFirst()) {
            user.setSymbol("X");
            ai.setSymbol("O");
            isUserTurn = true;
            gamePanel.updateStatus("You play first (X)"); // Update the label you just made visible!
        } else {
            user.setSymbol("O");
            ai.setSymbol("X");
            isUserTurn = false;
            gamePanel.updateStatus("AI plays first (X)");
        }
    }

    private void handleAITossWin() {
        JOptionPane.showMessageDialog(parentFrame, "AI won the toss and chose to play first (X)!");

        // Update symbols on existing objects rather than recreating them
        // This preserves the Player's Name and Type
        ai.setSymbol("X");
        if (user != null) {
            user.setSymbol("O");
        }

        isUserTurn = false;
    }

    private void triggerAIMove() {
        if (ai.getType() != Player.PlayerType.AI) return;

        int moveIndex = switch (difficulty) {
            case HARD -> getBestMoveMinimax();
            case MEDIUM -> getSmartMove();
            case EASY -> getRandomMove();
        };

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

            // --- RESET STATUS ---
            gamePanel.updateStatus("Your Turn! (" + getUserSymbol() + ")");
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
        String message = String.format("%s (%s) has won the match!",
                winner.getName(), winner.getSymbol());

        // This call will make the method "highlighted" in GamePanel
        if (gamePanel != null) {
            String type = (winner.getType() == Player.PlayerType.AI) ? "AI" : "USER";
            gamePanel.updateScore(type);
        }

        JOptionPane.showMessageDialog(parentFrame, message, "Victory", JOptionPane.INFORMATION_MESSAGE);
        startNewGameFlow();
    }

    private void announceDraw() {
        if (gamePanel != null) {
            // MUST match what updateScore is looking for
            gamePanel.updateScore("DRAW");
        }
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

    private void setupStreams() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        SwingUtilities.invokeLater(() -> nav.showGame());
        listenForOpponent();
    }

    private void listenForOpponent() {
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.startsWith("MOVE:")) {
                        int index = Integer.parseInt(msg.split(":")[1]);
                        handleIncomingMove(index);
                    }
                }
            } catch (IOException e) {
                updateStatus("Connection Lost!");
            }
        }).start();
    }

    private void handleIncomingMove(int index) {
        // Crucial: Network threads cannot touch the UI directly; we must use the EDT [cite: 2026-01-20]
        SwingUtilities.invokeLater(() -> {
            // Determine the opponent's symbol based on yours [cite: 2026-01-20]
            String opponentSymbol = mySymbol.equals("X") ? "O" : "X";

            // Execute the move received from the WiFi [cite: 2026-01-20]
            executeMove(index, opponentSymbol);

            // Return control to the local player [cite: 2026-01-20]
            isMyTurn = true;
            updateStatus("Your Turn!");
        });
    }
    // --- LOGIC BRIDGE METHODS ---

    private void executeMove(int index, String symbol) {
        int row = index / 3;
        int col = index % 3;

        // Update the logical board model
        if (board.makeMove(row, col, symbol)) {
            // Update the visual board buttons
            if (gamePanel != null) {
                gamePanel.updateButton(index, symbol);
            }
            // Check if this move ended the game
            checkGameOver(symbol);
        }
    }

    public void updateStatus(String status) {
        // Thread-safe update to the UI label
        SwingUtilities.invokeLater(() -> {
            if (gamePanel != null) {
                gamePanel.updateStatus(status);
            }
            System.out.println("Status Update: " + status);
        });
    }

    public String getUserSymbol() {
        return user != null ? user.getSymbol() : "";
    }
    public String getAiSymbol() {
        return ai != null ? ai.getSymbol() : "";
    }
}