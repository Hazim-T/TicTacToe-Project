package boardgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

public class GameApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ui.fxml"));
        Logger.info("UI loaded");
        stage.setTitle("Rocky Tic-Tac-Toe");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        Logger.info("Showing stage");
    }
}
