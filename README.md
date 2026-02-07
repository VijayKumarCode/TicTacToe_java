🎮 Tic-Tac-Toe: Ubuntu GUI Edition

A modular, industry-standard Tic-Tac-Toe application built with Java Swing. This project showcases the transition from CLI logic to a Graphical User Interface (GUI), implementing advanced state management, dependency injection, and loose coupling.
🚀 Project Overview

This application provides a professional gameplay flow designed to demonstrate robust user state handling and UI navigation:

    Registered Mode: Captures user credentials via a UserLoginPanel, preserves the username throughout the session, and transitions to the game board.

    Guest Mode: Allows instant "one-click" play against the AI Agent using an anonymous session.

🪙 The "Toss Authority" Feature

Unlike traditional Tic-Tac-Toe games, this version introduces Toss Authority. The winner of the randomized pre-game toss is granted the power to decide the game's opening strategy via a custom TossDialog.
📂 Project Structure

This tree represents the clean separation of concerns within the src directory.
TicTacToe_Project/
├── .gitignore
├── README.md
└── src/
    └── com/
        └── tictactoe/
            ├── Main.java                 # Application Entry Point
            ├── controller/
            │   ├── GameController.java   # Core Game Logic & Event Handling
            │   └── NavigationController.java # CardLayout Navigation
            ├── model/
            │   ├── Board.java            # 3x3 Grid Logic
            │   └── Player.java           # Player Identity & Symbol Data
            └── view/
                ├── MainFrame.java        # Main Window (JFrame)
                ├── StartupPanel.java     # Landing Screen
                ├── GamePanel.java        # The Tic-Tac-Toe Board UI
                ├── UserLoginPanel.java   # Authentication UI
                └── components/
                    └── TossDialog.java   # Custom Modal for Toss Decision

                    📂 Modular Architecture (MVC)
                    Package,Component,Responsibility
Model,"Board, Player","Manages the 3x3 grid logic, win validation, and player identity persistence."
View,"MainFrame, GamePanel, StartupPanel, UserLoginPanel",Handles the visual layout and user input events.
View.components,TossDialog,"Specialized JDialog for handling the pre-game ""Play or Pass"" decision."
Controller,"GameController, NavigationController",Orchestrates screen transitions and acts as the bridge between UI and Logic.

🛠️ Installation & Setup (Ubuntu/Linux)
1. Prerequisites

Ensure you have OpenJDK 17 or higher installed:
sudo apt update
sudo apt install openjdk-17-jdk

You are absolutely right. In the software industry, especially for Ubuntu and open-source projects, a clear Project Structure section is essential. It helps other developers navigate your code without opening every folder.

I have updated the README to include the badges you requested and a detailed directory tree.
🎮 Tic-Tac-Toe: Ubuntu GUI Edition

A modular, industry-standard Tic-Tac-Toe application built with Java Swing. This project showcases the transition from CLI logic to a Graphical User Interface (GUI), implementing advanced state management, dependency injection, and loose coupling.
🚀 Project Overview

This application provides a professional gameplay flow designed to demonstrate robust user state handling and UI navigation:

    Registered Mode: Captures user credentials via a UserLoginPanel, preserves the username throughout the session, and transitions to the game board.

    Guest Mode: Allows instant "one-click" play against the AI Agent using an anonymous session.

🪙 The "Toss Authority" Feature

Unlike traditional Tic-Tac-Toe games, this version introduces Toss Authority. The winner of the randomized pre-game toss is granted the power to decide the game's opening strategy via a custom TossDialog.
📂 Project Structure

This tree represents the clean separation of concerns within the src directory.
Plaintext

TicTacToe_Project/
├── .gitignore
├── README.md
└── src/
    └── com/
        └── tictactoe/
            ├── Main.java                 # Application Entry Point
            ├── controller/
            │   ├── GameController.java   # Core Game Logic & Event Handling
            │   └── NavigationController.java # CardLayout Navigation
            ├── model/
            │   ├── Board.java            # 3x3 Grid Logic
            │   └── Player.java           # Player Identity & Symbol Data
            └── view/
                ├── MainFrame.java        # Main Window (JFrame)
                ├── StartupPanel.java     # Landing Screen
                ├── GamePanel.java        # The Tic-Tac-Toe Board UI
                ├── UserLoginPanel.java   # Authentication UI
                └── components/
                    └── TossDialog.java   # Custom Modal for Toss Decision

📂 Modular Architecture (MVC)
Package	Component	Responsibility
Model	Board, Player	Manages the 3x3 grid logic, win validation, and player identity persistence.
View	MainFrame, GamePanel, StartupPanel, UserLoginPanel	Handles the visual layout and user input events.
View.components	TossDialog	Specialized JDialog for handling the pre-game "Play or Pass" decision.
Controller	GameController, NavigationController	Orchestrates screen transitions and acts as the bridge between UI and Logic.
🛠️ Installation & Setup (Ubuntu/Linux)
1. Prerequisites

Ensure you have OpenJDK 17 or higher installed:
Bash

sudo apt update
sudo apt install openjdk-17-jdk

2. Clone & Navigate

git clone https://github.com/VijayKumarCode/TicTacToe_Project.git
cd TicTacToe_Project

3. Build & Run

# Compile
javac -d out src/com/tictactoe/Main.java

# Run
java -cp out com.tictactoe.Main

📈 Roadmap

[x] Initial MVC structure and package organization.

    [x] Implementation of "Toss Authority" decision logic.

    [x] Session persistence for Registered vs. Guest users.

    [ ] Next: Integration of Minimax AI Agent for an "Unbeatable" difficulty mode.

    [ ] Future: JSON-based local storage for player win/loss statistics.

👤 Career Focus

I am an aspiring Remote Software Engineer specializing in Java and Open Source development. This project serves as a practical application of my Data Structures and Algorithms (DSA) studies and my commitment to writing clean, maintainable code that aligns with the Ubuntu/Canonical ecosystem.
