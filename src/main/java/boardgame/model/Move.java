package boardgame.model;

/**
 * Represents a move on the board game.
 * A move is defined by the row and column of the square to be moved to.
 */
public class Move {

    private final int row;
    private final int column;

    public Move(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}