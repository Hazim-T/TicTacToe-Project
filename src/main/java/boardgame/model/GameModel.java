package boardgame.model;

import game.BasicState;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class GameModel implements BasicState<Move> {

    private Player currentPlayer;

    public static final int BOARD_SIZE = 3;

    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];

    public GameModel() {
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<Square>(Square.NONE);
            }
        }
        currentPlayer = Player.PLAYER_1;
    }

    public ReadOnlyObjectProperty<Square> squareProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    public Square getSquare(int i, int j) {
        return board[i][j].get();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public boolean isLegalMove(Move move) {
        if (board[move.getRow()][move.getColumn()].get() == Square.GREEN) {
            System.out.println("Can't click there!");
            return false;
        }
        if (move.getRow() < 0 || move.getRow() >= BOARD_SIZE || move.getColumn() < 0 || move.getColumn() >= BOARD_SIZE) {
            System.out.println("Out of the board!");
            return false;
        }
        return true;
    }

    @Override
    public void makeMove(Move move) {
        board[move.getRow()][move.getColumn()].set(
                switch (board[move.getRow()][move.getColumn()].get()) {
                    case NONE -> Square.RED;
                    case RED -> Square.YELLOW;
                    case YELLOW, GREEN -> Square.GREEN;
                }
        );
        if (!isGameOver()) {
            currentPlayer = getNextPlayer();
        }
    }

    @Override
    public Player getNextPlayer() {
        return switch (currentPlayer) {
            case PLAYER_1 -> Player.PLAYER_2;
            case PLAYER_2 -> Player.PLAYER_1;
        };
    }

    @Override
    public boolean isGameOver() {
        // Check rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][0].get() != Square.NONE && board[i][0].get() == board[i][1].get() && board[i][1].get() == board[i][2].get()) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (board[0][j].get() != Square.NONE && board[0][j].get() == board[1][j].get() && board[1][j].get() == board[2][j].get()) {
                return true;
            }
        }

        // Check diagonals
        if (board[0][0].get() != Square.NONE && board[0][0].get() == board[1][1].get() && board[1][1].get() == board[2][2].get()) {
            return true;
        }
        if (board[0][2].get() != Square.NONE && board[0][2].get() == board[1][1].get() && board[1][1].get() == board[2][0].get()) {
            return true;
        }
        return false;
    }

    @Override
    public Status getStatus() {
        if (!isGameOver()) {
            if (currentPlayer == Player.PLAYER_1) {
                return Status.PLAYER_1_WINS;
            } else if (currentPlayer == Player.PLAYER_2) {
                return Status.PLAYER_2_WINS;
            }
        }
        return Status.IN_PROGRESS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                sb.append(board[i][j].get().ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}