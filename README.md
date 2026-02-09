🎮 Tic-Tac-Toe: Ubuntu GUI Edition

A modular, industry-standard Tic-Tac-Toe application built with Java Swing. This project showcases high-level software engineering principles including the MVC pattern, Minimax AI, and Unit Testing.
🚀 Project Overview

This application provides a professional gameplay flow designed to demonstrate robust user state handling and UI navigation:

    Registered Mode: Captures user credentials via UserLoginPanel, preserves the username, and transitions to the game board.

    Guest Mode: Allows instant "one-click" play against the AI Agent using an anonymous session.

🪙 The "Toss Authority" Feature

The winner of the randomized pre-game toss is granted the power to decide the opening strategy via a custom TossDialog:

    Play First: Assigns the player "X" and the first move.

    Pass Turn: Assigns the player "O" and allows the AI to open the game.
    
## 📂 Project Structure

```text
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

## 🏗️ Technical Highlights

### 1. Component-Based UI
The **View** is split into specialized, reusable components to ensure high maintainability and loose coupling:
* **BoardPanel**: Dedicated to rendering the 3x3 grid and managing button events.
* **ScoreBoardPanel**: Tracks and displays real-time match statistics and player scores.
* **TossDialog**: A custom modal interface that implements the "Toss Authority" logic.

### 2. Robust State Management
The implementation of `GameState.java` allows the `GameController` to track whether the match is **Active**, **Paused**, or **Terminal** (Win/Draw) completely independent of the UI state. This follows the **Single Responsibility Principle**.

### 3. Unit Testing (QA Focused)
The inclusion of `BoardTest.java` demonstrates a **Test-Driven Development (TDD)** mindset. This ensures that:
* All 8 win conditions (rows, columns, diagonals) are verified.
* Draw logic and board-full states are handled without regression.
* The core game logic remains stable as the UI evolves.
                   🛠️ Build & Test (Ubuntu/Linux)
Run Production Code
# Compile
javac -d out src/main/java/com/tictactoe/**/*.java src/main/java/com/tictactoe/*.java

# Run
java -cp out com.tictactoe.Main

Run Unit Tests
# Run with JUnit Console Standalone
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
