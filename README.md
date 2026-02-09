🎮 Tic-Tac-Toe: Ubuntu GUI Edition

A modular, industry-standard Tic-Tac-Toe application built with Java Swing. This project showcases high-level software engineering principles including the MVC pattern, Minimax AI, and Unit Testing.

[![Java Version](https://img.shields.io/badge/Java-17%2B-orange)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Style](https://img.shields.io/badge/Architecture-MVC-green)](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller)
[![Platform](https://img.shields.io/badge/Platform-Ubuntu%20%2F%20Linux-E95420)](https://ubuntu.com/)

A modular, industry-standard Tic-Tac-Toe application built with **Java Swing**. This project showcases the transition from CLI logic to a Graphical User Interface (GUI), implementing advanced state management and loose coupling.

---

## 🚀 Project Overview

This application provides a professional gameplay flow designed to demonstrate robust user state handling and UI navigation:
* **Registered Mode:** Captures user credentials via a `UserLoginPanel`, preserves the username, and transitions to the game board.
* **Guest Mode:** Allows instant "one-click" play against the **AI Agent** using an anonymous session.

### 🪙 The "Toss Authority" Feature
The winner of the randomized pre-game toss is granted the power to decide the opening strategy via a custom `TossDialog`:
1.  **Play First:** Assigns the player "X" and the first move.
2.  **Pass Turn:** Assigns the player "O" and allows the AI to open the game.

---

## 📂 Project Structure

TicTacToe_Project/
├── src/
│   └── main/java/com/tictactoe/
│       ├── Main.java                 # Application Entry Point
│       ├── controller/
│       │   ├── GameController.java   # Game Logic & Minimax
│       │   └── NavigationController.java # CardLayout Navigation
│       ├── model/
│       │   ├── Board.java            # Grid State Logic
│       │   ├── GameState.java        # Match Status Tracking
│       │   └── Player.java           # Player Entities
│       └── view/
│           ├── MainFrame.java        # Primary Window
│           ├── GamePanel.java        # Main Game Screen
│           ├── StartupPanel.java     # Landing Screen
│           ├── UserLoginPanel.java   # Authentication UI
│           └── components/           # Reusable UI Widgets
│               ├── BoardPanel.java      # Isolated 3x3 Grid
│               ├── ScoreBoardPanel.java # Player Stats Display
│               └── TossDialog.java      # Modal Choice UI
├── test/java/com/tictactoe/model/
│   └── BoardTest.java                # Unit Tests for Board Logic
├── .gitignore
├── README.md
└── TicTacToe_Project.iml

🏗️ Technical Highlights
1. Component-Based UI

Instead of one giant file, the View is now split into specialized components:

    BoardPanel: Dedicated to rendering the grid and handling button clicks.

    ScoreBoardPanel: Tracks and displays wins/losses in real-time.

    TossDialog: A modal interface for the "Toss Authority" feature.

2. Robust State Management

The addition of GameState.java allows the controller to track whether the 
game is in progress, paused, or finished independently of the UI state.

3. Unit Testing (QA focused)

The inclusion of BoardTest.java demonstrates a "Test-Driven" mindset. 
This ensures that win conditions and draw logic are verified automatically
before every release.

                    🛠️ Build & Test (Ubuntu/Linux)
Run Production Code

# Compile
javac -d out src/main/java/com/tictactoe/**/*.java src/main/java/com/tictactoe/*.java

# Run
java -cp out com.tictactoe.Main

Run Unit Tests

# Ensure JUnit is in your classpath
java -jar junit-platform-console-standalone.jar --class-path out --select-class com.tictactoe.model.BoardTest

📈 Roadmap

    [x] Initial MVC structure and package organization.

    [x] Implementation of "Toss Authority" decision logic.

    [x] Integration of Minimax AI Agent (Hard Difficulty).

    [x] New: Refactored View into reusable Components (BoardPanel, ScoreBoardPanel).

    [x] New: Added JUnit Test Suite for core model logic.

    [ ] Next: Persistence Layer using JSON for local leaderboard statistics.

    👤 Career Focus

I am an aspiring Remote Software Engineer specializing in Java and Open Source development.
This project serves as a practical application of my DSA studies and my commitment to writing clean,
maintainable code that aligns with the Ubuntu/Canonical ecosystem.
