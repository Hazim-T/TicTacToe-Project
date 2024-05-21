package boardgame.model;

import game.BasicState;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import lombok.Getter;
import org.tinylog.Logger;


public class GameModel implements BasicState<Move> {

    private Player currentPlayer;

    @Getter
    private final ReadOnlyIntegerWrapper numberOfTurns;

    /**
     * The size of the board.
     */
    public static final int BOARD_SIZE = 3;

    private ReadOnlyObjectWrapper<Rock>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];

    /**
     * Creates a {@code GameModel} object that corresponds to the original
     * initial state of the game.
     */
    public GameModel() {
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<Rock>(Rock.NONE);
            }
        }
        Logger.info("Game board initialized");
        numberOfTurns = new ReadOnlyIntegerWrapper(0);
        currentPlayer = Player.PLAYER_1;
        Logger.info("Current player initialized to " + getNextPlayer());
    }

    /**
     * @param i the row index
     * @param j the column index
     * @return {@code ReadOnlyObjectProperty} of the {@code Rock} at the given index
     */
    public ReadOnlyObjectProperty<Rock> rockProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    public ReadOnlyIntegerProperty numberOfTurnsProperty() {
        return numberOfTurns.getReadOnlyProperty();
    }

    /**
     * @return whether the {@code Move} provided can be applied to the state
     * @param move the move to be made
     */
    @Override
    public boolean isLegalMove(Move move) {
        return isOnBoard(move)
                && !isGreenRock(move)
                && !isGameOver();
    }

    private boolean isGreenRock(Move move) {
        return board[move.row()][move.col()].get() == Rock.GREEN;
    }

    private boolean isOnBoard(Move move) {
        return 0 <= move.row() && move.row() < BOARD_SIZE && 0 <= move.col() && move.col() < BOARD_SIZE;

    }

    /**
     * Applies the given {@code Move} to the game board if it is legal.
     *
     * @param move the move to be made
     */
    @Override
    public void makeMove(Move move) {
        if (isLegalMove(move)) {
            board[move.row()][move.col()].set(
                    switch (board[move.row()][move.col()].get()) {
                        case NONE -> Rock.RED;
                        case RED -> Rock.YELLOW;
                        case YELLOW, GREEN -> Rock.GREEN;
                    }
            );
            numberOfTurns.set(numberOfTurns.get() + 1);
            Logger.info("number of turns: " + getNumberOfTurns());
            if (!isGameOver()) {
                currentPlayer = currentPlayer.opponent();
            }
        }
    }

    /**
     * @return the player to make the next move
     */
    @Override
    public Player getNextPlayer() {
        return currentPlayer;
    }

    /**
     * {@return whether the game is over}
     */
    @Override
    public boolean isGameOver() {
        // Check rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][0].get() != Rock.NONE && board[i][0].get() == board[i][1].get() && board[i][1].get() == board[i][2].get()) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (board[0][j].get() != Rock.NONE && board[0][j].get() == board[1][j].get() && board[1][j].get() == board[2][j].get()) {
                return true;
            }
        }

        // Check diagonals
        if (board[0][0].get() != Rock.NONE && board[0][0].get() == board[1][1].get() && board[1][1].get() == board[2][2].get()) {
            return true;
        }
        if (board[0][2].get() != Rock.NONE && board[0][2].get() == board[1][1].get() && board[1][1].get() == board[2][0].get()) {
            return true;
        }
        return false;
    }

    /**
     * {@return the status of the game}
     */
    @Override
    public Status getStatus() {
        if (!isGameOver()) {
            return Status.IN_PROGRESS;
        }
        return currentPlayer == Player.PLAYER_2 ? Status.PLAYER_1_WINS : Status.PLAYER_2_WINS;
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