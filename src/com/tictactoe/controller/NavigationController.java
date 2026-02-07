/**
 Problem No. #107
 * Difficulty: Easy
 * Description: Manages UI state transitions using CardLayout and string-based identifiers
 * Link: https://docs.oracle.com/javase/tutorial/uiswing/layout/card.html
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */

package com.tictactoe.controller;

import javax.swing.*;
import java.awt.*;

public class NavigationController {
    private final JPanel container;
    private final CardLayout cardLayout;

    public NavigationController(JPanel container) {
        this.container = container;
        this.cardLayout = (CardLayout) container.getLayout();
    }

    public void showStartup() {
        cardLayout.show(container, "STARTUP");
    }

    public void showLogin() {
        cardLayout.show(container, "LOGIN");
    }

    public void showGame() {
        cardLayout.show(container, "GAME");
    }

    public void showLobby() {
        cardLayout.show(container, "LOBBY");
    }
}
