package boardgame.model;

import game.BasicState;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.tinylog.Logger;


public class GameModel implements BasicState<Move> {

    private Player currentPlayer;

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
        currentPlayer = Player.PLAYER_1;
        Logger.info("Current player initialized to " + getNextPlayer());
    }

    public Rock getRock(int i, int j) {
        return board[i][j].get();
    }

    /**
     * @param i the row index
     * @param j the column index
     * @return {@code ReadOnlyObjectProperty} of the {@code Rock} at the given index
     */
    public ReadOnlyObjectProperty<Rock> rockProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    /**
     * @return whether the {@code Move} provided can be applied to the state
     * @param move the move to be made
     */
    @Override
    public boolean isLegalMove(Move move) {
        // A green ROCK can't be replaced
        if (board[move.getRow()][move.getColumn()].get() == Rock.GREEN) {
            Logger.warn("Green rock clicked, Illegal move!");
            return false;
        }
        if (move.getRow() < 0 || move.getRow() >= BOARD_SIZE || move.getColumn() < 0 || move.getColumn() >= BOARD_SIZE) {
            Logger.error("Click is outside of the board index!");
            return false;
        }
        if (isGameOver()) {
            return false;
        }
        return true;
    }

    /**
     * Applies the given {@code Move} to the game board if it is legal.
     *
     * @param move the move to be made
     */
    @Override
    public void makeMove(Move move) {
        if (isLegalMove(move)) {
            board[move.getRow()][move.getColumn()].set(
                    switch (board[move.getRow()][move.getColumn()].get()) {
                        case NONE -> Rock.RED;
                        case RED -> Rock.YELLOW;
                        case YELLOW, GREEN -> Rock.GREEN;
                    }
            );
            if (!isGameOver()) {
                changePlayer();
            }
        }
    }

    /**
     * Returns the next player in the game based on the current player.
     *
     * @return the next player
     */
    @Override
    public Player getNextPlayer() {
        return currentPlayer;
    }

    private void changePlayer() {
        currentPlayer =  switch (getNextPlayer()) {
            case PLAYER_1 -> Player.PLAYER_2;
            case PLAYER_2 -> Player.PLAYER_1;
        };
    }

    /**
     * Checks if the game is over based off the game rules.
     *
     * @return {@code true} if the game is over, {@code false} otherwise
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
     * Checks the current status of the game
     *
     * @return the game status as a {@link Status} enum element
     */
    @Override
    public Status getStatus() {
        if (!isGameOver()) {
            if (currentPlayer == Player.PLAYER_1) {
                Logger.info("Player 1 wins!");
                return Status.PLAYER_1_WINS;
            } else if (currentPlayer == Player.PLAYER_2) {
                Logger.info("Player 2 wins!");
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