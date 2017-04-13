package Game.Controllers;

import Framework.AI.BotInterface;
import Framework.GUI.Board;
import Framework.Game.GameLogicInterface;
import Game.Models.Othello;
import Game.Views.CustomLabel;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by peterzen on 2017-04-12.
 * Part of the othello project.
 */
public class BoardController extends Board {
    public Othello othello;
    private static final int BOARDSIZE = 8;
    private BotInterface AI;
    private GameLogicInterface gameLogic;

    private Label[] listOfLabels;
    private boolean isOurTurn = false;

    private static final String gridCellStyle = "-fx-border-color: black; -fx-border-width:1;";
    private static final String cellTakenStyle = "-fx-border-color: red; -fx-border-width:1;";
    private static final String preGameGridStyle = "-fx-border-color: yellow;-fx-border-width:3;-fx-padding: 10 10 10 10;-fx-border-insets: 10 10 10 10;";
    private static final String ourTurnGridStyle = "-fx-border-color: green;-fx-border-width:3;-fx-padding: 10 10 10 10;-fx-border-insets: 10 10 10 10;";
    private static final String theirTurnGridStyle = "-fx-border-color: red;-fx-border-width:3;-fx-padding: 10 10 10 10;-fx-border-insets: 10 10 10 10;";
    private static double cellWidth;
    private static double cellHeight;

    public BotInterface getAI() {
        return AI;
    }

    public GameLogicInterface getGameLogic() {
        return gameLogic;
    }

    public void initialize() {
//        System.out.println("Init");
        cellWidth = (gridPane.getPrefWidth() / BOARDSIZE) - 2;
        cellHeight = (gridPane.getPrefWidth() / BOARDSIZE) - 2;
        drawGrid(BOARDSIZE);
        loadGrid();

        this.othello = new Othello();
        othello.showBoard();
    }

    // List of coordinates
    public Map<Integer, int[]> getListOfCoordinates() {
        Map<Integer, int[]> listOfCoordinates = new HashMap<>();
        int key = 0;
        for (int y = 0; y < BOARDSIZE; y++) {
            for (int x = 0; x < BOARDSIZE; x++) {
                listOfCoordinates.put(key, new int[]{x, y});
                key++;
            }
        }
        return listOfCoordinates;
    }

    private void loadGrid() {
        int i;
        int j;
        // i = row, j = column
        for (i = 0; i < BOARDSIZE; i++) {
            for (j = 0; j < BOARDSIZE; j++) {
                Image image = new Image(BoardController.class.getClassLoader().getResourceAsStream("Empty.png"));
//                System.out.println("I: " + i + " J: " + j);
                Image blackImage = new Image(BoardController.class.getClassLoader().getResourceAsStream("Black.png"));
                Image whiteImage = new Image(BoardController.class.getClassLoader().getResourceAsStream("White.png"));
                ImageView imageView = new ImageView();
                imageView.setFitHeight(cellHeight - 5);
                imageView.setFitWidth(cellWidth - 5);
                if (i == 3 && j == 3 || i == 4 && j == 4) {
                    imageView.setImage(whiteImage);
                } else if (i == 3 && j == 4 || i == 4 && j == 3) {
                    imageView.setImage(blackImage);
                } else {
                    imageView.setImage(image);
                }
                CustomLabel label = new CustomLabel();
                label.setPrefSize(cellWidth, cellHeight);
                label.setX(i);
                label.setY(j);
                label.setOnMouseClicked(this::clickToDoMove);
                gridPane.setHalignment(label, HPos.CENTER);
                label.setStyle(gridCellStyle);
                label.setGraphic(imageView);

                final int finali = i;
                final int finalj = j;
                Platform.runLater(() -> gridPane.add(label, finalj, finali));
            }
        }
        // @TODO beginopstelling doorgeven aan model?
        // of wordt die opstelling al meteen gezet wann het model (Othello / GameLogic) wordt aangemaakt
        gridPane.setStyle(preGameGridStyle);
    }

    public void loadPreGameBoardState() {
        System.out.println("PreGameBoardState Loaded!");
        Platform.runLater(() -> gridPane.getChildren().clear());
        Platform.runLater(this::loadGrid);
    }

    private void clickToDoMove(MouseEvent mouseEvent) {
        CustomLabel label = (CustomLabel) mouseEvent.getSource();

        if(this.othello.isLegitMove(label.getX(), label.getY(), '1')) {
            System.out.println("Uep");
            //replace the old label with a stone
            CustomLabel newLabel = this.makeLabel(label.getX(), label.getY(), "1");
            gridPane.getChildren().remove(label);
            gridPane.add(newLabel, label.getY(), label.getX());
            //update othello
            ArrayList<Integer[]> toSwap = othello.doTurn(label.getY(), label.getX(), '1');

            for (Integer[] coords : toSwap) {
                this.swap(coords);
            }
//            send moveRequest to the server
        }
        else {
            //Show notification that the move is incorrect
        }
    }

    private void swap(Integer[] coords) {
        ObservableList<Node> childrenList = gridPane.getChildren();

        for(Node label : childrenList) {
            if (gridPane.getRowIndex(label).equals(coords[0]) && gridPane.getColumnIndex(label).equals(coords[1])) {
                Platform.runLater(() -> gridPane.getChildren().remove(label));
            }
            Platform.runLater(() -> gridPane.add(makeLabel(coords[0], coords[1], "1"), coords[1], coords[0]));
        }
    }

    private CustomLabel makeLabel(int y, int x, String player) {
        CustomLabel label = new CustomLabel();
        ImageView imageView = new ImageView();
        imageView.setFitHeight(30.0);
        imageView.setFitWidth(30.0);
        label.setStyle(cellTakenStyle);
        if (player.equals("1")) {
            Image image = new Image(BoardController.class.getClassLoader().getResourceAsStream("./White.png"));
            imageView.setImage(image);
            label.setGraphic(imageView);
            label.setX(x);
            label.setY(y);
            gridPane.setHalignment(label, HPos.CENTER);
        } else {
            Image image = new Image(BoardController.class.getClassLoader().getResourceAsStream("./Black.png"));
            imageView.setImage(image);
            label.setGraphic(imageView);
            label.setX(x);
            label.setY(y);
            gridPane.setHalignment(label, HPos.CENTER);
        }
        return label;
    }

}
