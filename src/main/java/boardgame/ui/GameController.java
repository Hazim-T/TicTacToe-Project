package boardgame.ui;

import boardgame.model.GameModel;
import boardgame.model.Move;
import boardgame.model.Rock;
import game.State;
import gameresult.TwoPlayerGameResult;
import gameresult.manager.TwoPlayerGameResultManager;
import gameresult.manager.json.JsonTwoPlayerGameResultManager;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.tinylog.Logger;
import util.javafx.EnumImageStorage;
import util.javafx.ImageStorage;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.ZonedDateTime;


public class GameController {
    @FXML
    private GridPane board;

    @FXML
    private TextField numberOfTurnsField;

    private Players players;

    private ZonedDateTime timeCreated;

    private GameModel model = new GameModel();

    private ImageStorage<Rock> imageStorage = new EnumImageStorage<>(Rock.class);

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            for (var i = 0; i < board.getRowCount(); i++) {
                for (var j = 0; j < board.getColumnCount(); j++) {
                    var square = createSquare(i, j);
                    board.add(square, j, i);
                }
            }
            numberOfTurnsField.textProperty().bind(model.numberOfTurnsProperty().asString());
            Stage stage = (Stage) board.getScene().getWindow();
            players = (Players) stage.getUserData();
            Logger.info("Player 1 name passed: {}", players.player1());
            Logger.info("Player 2 name passed: {}", players.player2());
            timeCreated = ZonedDateTime.now();
        });
    }

    private StackPane createSquare(int i, int j) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        var imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(60);
        imageView.imageProperty().bind(
                new ObjectBinding<Image>() {
                    {
                        super.bind(model.rockProperty(i, j));
                    }

                    @Override
                    protected Image computeValue() {
                        return imageStorage.get(model.rockProperty(i, j).get()).orElse(null);
                    }
                }
        );
        square.getChildren().add(imageView);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        Logger.debug(String.format("Click on square (%d,%d) by %s", row, col, model.getNextPlayer()));
        var nextMove = new Move(row, col);
        model.makeMove(nextMove);
        if (model.isGameOver()) {
            Logger.info("Game Over! " + getPlayerName(model.getNextPlayer()) + " wins!");
            handleGameOver();
        }
    }

    @FXML
    private void handleGameOver() {
        Logger.info("Game is Won!");
        storeResult();
        Platform.runLater(() -> {
            try {
                alertAndSwitchToLeaderboard();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void alertAndSwitchToLeaderboard() throws IOException {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over!");
        alert.setHeaderText(getPlayerName(model.getNextPlayer()) + " wins!");
        alert.setContentText("Check the Leaderboards!");
        alert.showAndWait();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/leaderboard.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) board.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private String getPlayerName(State.Player player) {
        if (players.player2().isEmpty() || players.player1().isEmpty()) {
            return player.toString();
        }
        return player == State.Player.PLAYER_1 ? players.player1() : players.player2();
    }

    private void storeResult() {
        var result = TwoPlayerGameResult.builder()
                .player1Name(players.player1())
                .player2Name(players.player2())
                .numberOfTurns(model.numberOfTurnsProperty().get())
                .status(model.getStatus())
                .duration(Duration.between(timeCreated, ZonedDateTime.now()))
                .created(timeCreated)
                .build();
        TwoPlayerGameResultManager manager = new JsonTwoPlayerGameResultManager(Path.of("database.json"));
        try {
            manager.add(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Logger.debug("Result stored successfully", result);
    }
}