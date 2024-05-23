package boardgame.ui;

import java.io.IOException;
import java.nio.file.Path;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import gameresult.manager.TwoPlayerGameResultManager;
import gameresult.manager.json.JsonTwoPlayerGameResultManager;
import javafx.stage.Stage;
import org.tinylog.Logger;

public class LeaderboardController {
    @FXML
    private static final int NUMBER_OF_ROWS_TO_SHOW = 5;

    @FXML
    private TableView<TwoPlayerGameResultManager.Wins> tableView;

    @FXML
    private TableColumn<TwoPlayerGameResultManager.Wins, String> playerName;

    @FXML
    private TableColumn<TwoPlayerGameResultManager.Wins, Integer> numberOfWins;

    @FXML
    private void initialize() throws IOException {
        playerName.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        numberOfWins.setCellValueFactory(new PropertyValueFactory<>("numberOfWins"));
        ObservableList<TwoPlayerGameResultManager.Wins> observableList = FXCollections.observableArrayList();
        observableList.addAll(new JsonTwoPlayerGameResultManager(Path.of("database.json")).getPlayersWithMostWins(NUMBER_OF_ROWS_TO_SHOW));
        Logger.debug("Results fetched from database");
        tableView.setItems(observableList);
        Logger.info("Leaderboard displayed with results");
    }

    @FXML
    private void onNewGame(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();

        Platform.runLater(() -> {
            try {
                new GameApplication().start(new Stage());
            } catch (Exception e) {
                Logger.error("Error loading opening screen", e);
            }
        });
    }

    @FXML
    private void onExitButton() {
        Logger.info("Closing application");
        Platform.exit();
    }
}