package com.tictactoe.view;

/**
 * Problem No. #104
 * Difficulty: Intermediate
 * Description: UI Panel for the Mock Lobby displaying available players and their status
 * Link: https://github.com/VijayKumarCode/TicTacToe_Project
 * Time Complexity: O(n) to render the player list
 * Space Complexity: O(n) for the list model
 */


import com.tictactoe.controller.GameController;
import com.tictactoe.controller.NavigationController;
import com.tictactoe.model.Player;
import com.tictactoe.model.JSONHandler;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List; // Explicitly using util List to avoid AWT conflict

public class LobbyPanel extends JPanel {
    private DefaultListModel<Player> listModel;
    private JList<Player> playerList;
    private final JButton requestButton;
    private LobbyPanel lobbyPanel;

    public LobbyPanel(NavigationController nav, GameController gameController) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Header
        JLabel header = new JLabel("🌐 Available Opponents", SwingConstants.CENTER);
        header.setFont(new Font("Ubuntu", Font.BOLD, 18));
        add(header, BorderLayout.NORTH);

        // 2. The List setup
        this.listModel = new DefaultListModel<>(); // Marking as 'this' satisfies the 'final' highlight
        this.playerList = new JList<>(listModel);
        add(new JScrollPane(playerList), BorderLayout.CENTER);

        // 3. The Action Button
        this.requestButton = new JButton("Send Match Request");
        requestButton.setBackground(new Color(70, 130, 180));
        requestButton.setForeground(Color.WHITE);

        // Inside the LobbyPanel constructor
        requestButton.addActionListener(e -> {
            Player selected = playerList.getSelectedValue();
            if (selected != null) {
                String ip = JOptionPane.showInputDialog(this, "Enter IP (Leave blank to HOST):");

                if (ip != null) {
                    gameController.initiateNetworkMatch(ip);
                }
            }
        });

        add(requestButton, BorderLayout.SOUTH);
        refreshLobby();
    }

    private void showRequestSimulation(Player opponent, GameController gameController) {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "WiFi Connection", true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JLabel statusLabel = new JLabel("Synchronizing with " + opponent.getName() + "...", SwingConstants.CENTER);
        dialog.add(statusLabel);

        // Short timer to let the Socket connect in the background [cite: 2026-01-20]
        Timer timer = new Timer(1500, e -> {
            dialog.dispose();
            gameController.setOpponent(opponent);
        });

        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
    }

    public void refreshLobby() {
        listModel.clear();
        List<Player> players = JSONHandler.loadPlayers();
        if (players != null) {
            for (Player p : players) {
                listModel.addElement(p);
            }
        }
    }

    private void launchGame(Player opponent) {
        System.out.println("Handshaking with " + opponent.getName());
        JOptionPane.showMessageDialog(this, "Match Started! You are X, " + opponent.getName() + " is O.");
    }
}