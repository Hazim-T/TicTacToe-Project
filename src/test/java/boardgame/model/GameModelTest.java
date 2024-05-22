package boardgame.model;

import game.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GameModelTest {

    private GameModel initialState;
    private GameModel progressState;
    private GameModel wonState;
    private GameModel rowWin;
    private GameModel colWin;
    private GameModel diagWin;

    private void generateWonStates(){
        rowWin = new GameModel();
        colWin = new GameModel();
        diagWin = new GameModel();

        for (int column = 0; column < 3; column++) {
            rowWin.makeMove(new Move(1, column));
        }
        for (int row = 0; row < 3; row++) {
            colWin.makeMove(new Move(row, 2));
        }
        for (int i = 0; i < 3; i++) {
            diagWin.makeMove(new Move(i, 2 - i));
        }
    }

    @BeforeEach
    void setUp() {
        initialState = new GameModel();
        progressState = new GameModel();
        wonState = new GameModel();
        progressState.makeMove(new Move(0, 0));
        progressState.makeMove(new Move(0, 0));
        progressState.makeMove(new Move(0, 0));
        wonState.makeMove(new Move(0, 0));
        wonState.makeMove(new Move(1, 1));
        wonState.makeMove(new Move(2, 1));
        wonState.makeMove(new Move(2, 2));

    }

    @Test
    void isLegalMove() {
        assertFalse(initialState.isLegalMove(new Move(1, 5))); //out of board
        assertFalse(initialState.isLegalMove(new Move(5, 1)));
        assertFalse(initialState.isLegalMove(new Move(-1, 1)));
        assertFalse(initialState.isLegalMove(new Move(1, -1)));
        assertTrue(progressState.isLegalMove(new Move(0, 1))); //in board and not green
        assertFalse(progressState.isLegalMove(new Move(0, 0))); //in board and green
        assertFalse(wonState.isLegalMove(new Move(1, 2))); //game over
    }


    @Test
    void makeMove() {
        assertEquals(initialState.getNumberOfTurns(), 0);

        initialState.makeMove(new Move(0, 0));
        assertEquals(Rock.RED, initialState.rockProperty(0, 0).get());
        assertEquals(initialState.getNumberOfTurns(), 1);

        initialState.makeMove(new Move(0, 0));
        assertEquals(Rock.YELLOW, initialState.rockProperty(0, 0).get());
        assertEquals(initialState.getNumberOfTurns(), 2);

        initialState.makeMove(new Move(0, 0));
        var previousplayer = initialState.getNextPlayer();
        assertEquals(Rock.GREEN, initialState.rockProperty(0, 0).get());
        assertEquals(initialState.getNumberOfTurns(), 3);

        initialState.makeMove(new Move(0, 0));
        assertEquals(Rock.GREEN, initialState.rockProperty(0, 0).get());
        assertEquals(initialState.getNumberOfTurns(), 3);
        assertEquals(initialState.getNextPlayer(), previousplayer);
    }

    @Test
    void getNextPlayer() {
        assertEquals(State.Player.PLAYER_1, initialState.getNextPlayer()); //initial player

        initialState.makeMove(new Move(0, 0));
        assertEquals(State.Player.PLAYER_2, initialState.getNextPlayer()); //initial player switch

        var previousPlayer = progressState.getNextPlayer();
        progressState.makeMove(new Move(1, 2));
        assertEquals(previousPlayer.opponent(), progressState.getNextPlayer()); //switch to opponent in middle of game

        previousPlayer = wonState.getNextPlayer();
        wonState.makeMove(new Move(1, 2));
        assertEquals(previousPlayer, wonState.getNextPlayer()); //won't switch player in a completed game
    }

    @Test
    void isGameOver() {
        generateWonStates();
        assertFalse(initialState.isGameOver());
        assertFalse(progressState.isGameOver());

        assertTrue(wonState.isGameOver()); // is opposite diagonal win
        assertTrue(diagWin.isGameOver());
        assertTrue(rowWin.isGameOver());
        assertTrue(colWin.isGameOver());
    }

    @Test
    void getStatus() {
        generateWonStates();
        assertEquals(State.Status.IN_PROGRESS, initialState.getStatus());
        assertEquals(State.Status.IN_PROGRESS, progressState.getStatus());
        assertEquals(State.Status.PLAYER_1_WINS, colWin.getStatus());
        assertEquals(State.Status.PLAYER_1_WINS, rowWin.getStatus());
        assertEquals(State.Status.PLAYER_2_WINS, wonState.getStatus());
    }

    @Test
    void testToString() {
        assertEquals(initialState.toString(), "0 0 0 \n0 0 0 \n0 0 0 \n");
        assertEquals(progressState.toString(), "3 0 0 \n0 0 0 \n0 0 0 \n");
        assertEquals(wonState.toString(), "1 0 0 \n0 1 0 \n0 1 1 \n");
    }
}