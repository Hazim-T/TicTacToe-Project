package boardgame.model;

/**
 * Represents a move on the board game.
 * A move is defined by the row and column of the square to be moved to.
 */
public record Move(int row, int col) {

    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }
}