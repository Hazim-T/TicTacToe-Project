package boardgame;

import boardgame.model.GameModel;
import boardgame.model.Move;
import boardgame.model.Rock;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import org.tinylog.Logger;
import util.javafx.EnumImageStorage;
import util.javafx.ImageStorage;


public class GameController { // TODO: add champion name
    @FXML
    private GridPane board;

    private GameModel model = new GameModel();

    private ImageStorage<Rock> imageStorage = new EnumImageStorage<>(Rock.class);

    @FXML
    private void initialize() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }
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
        Logger.info(String.format("Click on square (%d,%d)", row, col));
        Logger.info("Click was made by " + model.getNextPlayer());
        var nextMove = new Move(row, col);
        model.makeMove(nextMove);
        if (model.isGameOver()){
            Logger.info("Game Over! " + model.getNextPlayer() + " wins!");
            handleGameOver();
        }
    }

    @FXML
    private void handleGameOver() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over!");
        alert.setHeaderText(model.getNextPlayer() + " wins!");
        alert.setContentText("Better luck next time!");
        alert.show();
    }
}