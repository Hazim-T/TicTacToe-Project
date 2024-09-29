# TIC TAC TOE game project
Tic Tac Toe game with a UI using JavaFX and made to run on the terminal.

## Table of Contents

- [Description](#description)
- [Installation](#installation)
- [Terminal Usage](#terminal-usage)

## Description

This Tic-Tac-Toe game is played by two players and contains a twist in rules. Here are the rules and features of the game:

### Game Rules

- **Board**: The game is played on a 3x3 grid.
- **Stones**: Players use colored stones: Red, Yellow, and Green.
- **Turns**: Players take turns to make a move. They can:
    - Place a red stone into an empty cell.
    - Replace a red stone on the board with a yellow stone.
    - Replace a yellow stone on the board with a green stone.
- **Winning Condition**: The goal is to get three stones of the same color in a row, column, or diagonal. The player who achieves this first wins the game.


When a new game is started, the program will ask for the names of the players.

The program stores the results of the games as follows. For each game, the following information is stored: 
the date and time when the game started, the name of the players, the number of turns made by the players 
during the game, and the name of the winner. The game then stores the data in a JSON file.

The program displays a high-score table in which the top 5 players with the most wins are displayed.

## Installation

To install the project:

- Clone the repository: `git clone https://github.com/INBPA0420L/homework-project-2024-Hazim-T`

## Terminal Usage

To run the game on the terminal, run this command on a separate terminal:

- `mvn compile exec:java -Dexec.mainClass="boardgame.model.ConsoleGame" `

Example game:
<pre>
 - the initial board state:
<pre>
0 0 0
0 0 0
0 0 0
</pre>

- Moves made by players:

1. PLAYER_1: 0 0
2. PLAYER_2: 1 2
3. PLAYER_1: 1 2
4. PLAYER_2: 1 1
5. PLAYER_1: 2 2

PLAYER_1 wins!

- the board state after the moves:

<pre>
1 0 0
0 1 2
0 0 1
</pre>
</pre>
