
/* Problem No. #103
 * Difficulty: Easy
 * Description: Enum defining the Finite State Machine (FSM) for game flow control
 * Link: N/A
 * Time Complexity: O(1)
 * Space Complexity: O(1)
 */

package com.tictactoe.model;

public enum GameState {
    IDLE,
    // Phase 1: Pre-game
    WAITING_FOR_TOSS,      // Coin is flipping
    TOSS_WINNER_DECIDING,  // User won toss and is choosing Play or Pass

    // Phase 2: Active Play
    PLAYER_X_TURN,
    PLAYER_O_TURN,

    // Phase 3: Conclusion
    X_WON,
    O_WON,
    DRAW,

    // System State
    GAME_OVER                   // Reset or main menu state
}
