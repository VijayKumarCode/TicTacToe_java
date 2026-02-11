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
    private Player ai;
    private Player opponent;
    private boolean isUserTurn;

/** * Guard to prevent AI from triggering multiple moves
 * simultaneously (e.g., during Toss transitions).
 */
    private boolean isAiProcessing = false;

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
                // Added a 5-second timeout so the app doesn't hang forever
                socket = new Socket();
                socket.connect(new java.net.InetSocketAddress(targetIp, 5555), 5000);

                mySymbol = "O";
                isMyTurn = false;
                setupStreams();
                updateStatus("Connected to Host!");
            } catch (java.net.SocketTimeoutException e) {
                showError("Connection timed out. Is the Host online?");
            } catch (IOException e) {
                showError("Could not find Host at " + targetIp + " on port 5555.");
            }
        }).start();
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(parentFrame, message, "Connection Error", JOptionPane.ERROR_MESSAGE)
        );
    }


    public void setDifficulty(String level) {
        try {
            this.difficulty = Difficulty.valueOf(level.toUpperCase());
            // Push the update to the UI
            if (gamePanel != null) {
                gamePanel.setDifficultyDisplay(level);
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
        // 1. Basic activity and turn check
        if (!isGameActive || (!isUserTurn && !isMyTurn)) return;
        if (!board.getSymbolAt(index).isEmpty()) return;

        // 2. Determine if this is a WiFi match
        boolean isWiFiMatch = (socket != null && !socket.isClosed());

        // 3. EXECUTE MOVE
        executeMove(index, mySymbol != null ? mySymbol : user.getSymbol());

        if (isWiFiMatch) {
            // --- WIFI LOGIC ---
            out.println("MOVE:" + index);
            isMyTurn = false;
            isUserTurn = false; // Ensure both turn flags are locked
            updateStatus("Waiting for Opponent...");
        } else if (ai != null && ai.getType() == Player.PlayerType.AI) {
            // --- AI LOGIC ---
            isUserTurn = false;
            updateStatus("AI is thinking...");

            // Only trigger AI if it's NOT a WiFi match
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

        // 1. SELECT DIFFICULTY
        String[] options = {"Easy", "Medium", "Hard"};
        int selection = JOptionPane.showOptionDialog(parentFrame,
                "Select AI Difficulty:",
                "Guest Match",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (selection == JOptionPane.CLOSED_OPTION) return;

        // Store the selected difficulty
        String chosenLevel = options[selection];
        this.difficulty = Difficulty.valueOf(chosenLevel.toUpperCase());

        // 2. NAVIGATE & INITIALIZE UI
        nav.showGame();

        // 3. UPDATE UI LABEL & START TOSS
        SwingUtilities.invokeLater(() -> {
            if (gamePanel != null) {
                gamePanel.setDifficultyDisplay(chosenLevel); // Make it visible immediately
            }
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
        this.isAiProcessing = false;
        this.isGameActive = true; // Reactive the board for the new round [cite: 2026-01-20]

        // 1. Clear Local UI
        if (gamePanel != null) {
            gamePanel.clearBoard();
        }
        board.resetBoard();

        // 2. WIFI MATCH LOGIC
        if (socket != null && !socket.isClosed()) {
            if (serverSocket != null) { // I am the Host
                // Tell the client to clear their screen before the toss [cite: 2026-01-20]
                out.println("RESET_GAME");

                boolean hostWonToss = Math.random() < 0.5;
                out.println("TOSS_RESULT:" + hostWonToss);
                handleNetworkToss(hostWonToss);
            }
            // Client waits for messages in listenForOpponent()
        } else {
            // 3. LOCAL/AI MATCH LOGIC
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
    }

    private void handleNetworkToss(boolean iWon) {
        if (iWon) {
            handleUserTossWin(); // Show the choice dialog to the winner
        } else {
            updateStatus("Opponent won the toss. Waiting for their choice...");
            isMyTurn = false;
        }
    }

    private void handleUserTossWin() {
        TossDialog dialog = new TossDialog(parentFrame, "You");
        dialog.setVisible(true);

        boolean isWiFi = (socket != null && !socket.isClosed());

        if (dialog.isPlayFirst()) {
            user.setSymbol("X");
            ai.setSymbol("O");
            isUserTurn = true;
            isMyTurn = true; // For WiFi matches
            updateStatus("You play first (X)");

            if (isWiFi) out.println("OPPONENT_DECISION:SECOND");
        } else {
            // PLAYER CHOSE TO PASS (Play Second)
            user.setSymbol("O");
            ai.setSymbol("X");
            isUserTurn = false;
            isMyTurn = false; // You cannot play now!
            updateStatus("Opponent plays first (X)");

            if (isWiFi) {
                out.println("OPPONENT_DECISION:FIRST");
            } else {
                // If local AI, trigger its move
                triggerAIMove();
            }
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
        // Strict guard check
        if (isAiProcessing || isUserTurn || ai.getType() != Player.PlayerType.AI || !isGameActive) return;

        isAiProcessing = true;
        updateStatus("AI is thinking...");

        // Background the move calculation so the UI doesn't stutter
        SwingWorker<Integer, Void> worker = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() {
                return switch (difficulty) {
                    case HARD -> getBestMoveMinimax();
                    case MEDIUM -> getSmartMove();
                    default -> getRandomMove();
                };
            }

            @Override
            protected void done() {
                try {
                    int moveIndex = get();
                    if (moveIndex != -1) {
                        executeAIMove(moveIndex);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isAiProcessing = false;
                }
            }
        };
        worker.execute();
    }

    private void executeAIMove(int index) {
        int r = index / 3;
        int c = index % 3;

        try {
            if (board.makeMove(r, c, getAiSymbol())) {
                gamePanel.updateButton(index, getAiSymbol());

                if (checkGameOver(getAiSymbol())) return;

                // Switch turn back to user
                isUserTurn = true;
                gamePanel.updateStatus("Your Turn! (" + getUserSymbol() + ")");
            }
        } finally {
            // ALWAYS release the guard last, so the next move can happen
            isAiProcessing = false;
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
        isGameActive = false; // Immediately stop all clicks [cite: 2026-01-20]
        String message = String.format("%s (%s) has won!", winner.getName(), winner.getSymbol());

        if (gamePanel != null) {
            String type = (winner.getType() == Player.PlayerType.AI) ? "AI" : "USER";
            gamePanel.updateScore(type);
        }

        // Blocking Dialog: Code stops here until "OK" is clicked [cite: 2026-01-08]
        JOptionPane.showMessageDialog(parentFrame, message, "Victory", JOptionPane.INFORMATION_MESSAGE);

        // WIFI SYNC: Only the Server (Host) decides when the next match starts [cite: 2026-01-20]
        if (socket != null && !socket.isClosed()) {
            if (serverSocket != null) { // I am the Host
                startNewGameFlow();
            } else {
                updateStatus("Waiting for Host to start next match...");
            }
        } else {
            // Local/AI Flow
            if (user.getType() == Player.PlayerType.ANONYMOUS) {
                handleGuestLogin();
            } else {
                startNewGameFlow();
            }
        }
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
                    // 1. Handle Game Reset Signal (Prevents the overlapping windows)
                    if (msg.equals("RESET_GAME")) {
                        SwingUtilities.invokeLater(() -> {
                            board.resetBoard();
                            if (gamePanel != null) gamePanel.clearBoard();
                            isGameActive = true;
                            updateStatus("Host is starting a new match...");
                        });
                    }
                    // 2. Handle the Toss Decision from the other player
                    else if (msg.startsWith("DECISION:") || msg.startsWith("OPPONENT_DECISION:")) {
                        String choice = msg.split(":")[1];
                        SwingUtilities.invokeLater(() -> {
                            if (choice.equals("OPPONENT_PLAYS_FIRST") || choice.equals("FIRST")) {
                                mySymbol = "X";
                                isMyTurn = true;
                                isUserTurn = true;
                                updateStatus("Opponent passed! Your turn (X)");
                            } else {
                                mySymbol = "O";
                                isMyTurn = false;
                                isUserTurn = false;
                                updateStatus("Opponent is playing first (X)");
                            }
                        });
                    }
                    // 3. Handle actual moves
                    else if (msg.startsWith("MOVE:")) {
                        int index = Integer.parseInt(msg.split(":")[1]);
                        handleIncomingMove(index);
                    }
                    // 4. Handle the Toss Result
                    else if (msg.startsWith("TOSS_RESULT:")) {
                        boolean hostWon = Boolean.parseBoolean(msg.split(":")[1]);
                        boolean iAmClient = (serverSocket == null);
                        boolean iWon = iAmClient ? !hostWon : hostWon;
                        SwingUtilities.invokeLater(() -> handleNetworkToss(iWon));
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