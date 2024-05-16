package boardgame.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class GameStartController {
    @FXML
    private TextField player1NameField;

    @FXML
    private TextField player2NameField;

    @FXML
    private void handleNextButton(ActionEvent event) throws IOException {
        Logger.info("Player 1 name entered: {}", player1NameField.getText());
        Logger.info("Player 2 name entered: {}", player2NameField.getText());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ui.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Players players = new Players(player1NameField.getText(), player2NameField.getText());
        stage.setUserData(players);
        stage.setScene(new Scene(root));
        stage.show();
    }
}