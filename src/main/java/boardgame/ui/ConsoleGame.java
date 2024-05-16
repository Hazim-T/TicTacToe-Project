package boardgame.ui;

import boardgame.model.GameModel;
import boardgame.model.Move;
import game.console.BasicGame;

import java.util.Scanner;

/**
 * Conducts the board game on the console.
 */
public class ConsoleGame {
    public static void main(String[] args) {
        var state = new GameModel();
        var game = new BasicGame<>(state, ConsoleGame::parseMove);
        game.start();
    }

    /**
     * Converts a string containing the position of a move to a {@code Move}
     * object.
     *
     * @param s a string that should contain two space-separated integers
     * @return the {@code Move} object that was constructed from the string
     * @throws IllegalArgumentException if the format of the string is invalid,
     * i.e., its content is not two integers separated with spaces
     */
    public static Move parseMove(String s) {
        s = s.trim();
        if (!s.matches("\\d+\\s+\\d+")) {
            throw new IllegalArgumentException();
        }
        var scanner = new Scanner(s);
        return new Move(scanner.nextInt(), scanner.nextInt());
    }
}
