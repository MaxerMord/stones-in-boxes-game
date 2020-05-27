package stonesinboxes.javafx.controller;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;
import stonesinboxes.results.GameResult;
import stonesinboxes.results.GameResultDao;
import stonesinboxes.state.StonesInBoxesState;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Slf4j
public class GameController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultDao gameResultDao;

    private String player1Name;
//    private StonesInBoxesState gameState1;
    private IntegerProperty steps1 = new SimpleIntegerProperty();
    private String player2Name;
//    private StonesInBoxesState gameState2;
    private IntegerProperty steps2 = new SimpleIntegerProperty();

    private StonesInBoxesState gameState;
    private Instant startTime;
    private List<Image> cubeImages;

    @FXML
    private Label messageLabel;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Label steps1Label;

    @FXML
    private Label steps2Label;

//    @FXML
//    private Label steps1Labe;

    @FXML
    private Label stopWatchLabel;

    private Timeline stopWatchTimeline;

    @FXML
    private Button resetButton;

    @FXML
    private Button giveUpButton;

    private BooleanProperty gameOver = new SimpleBooleanProperty();

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }
    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    /**
     * ==============
     */
    @FXML
    public void initialize() {
        cubeImages = List.of(
                new Image(getClass().getResource("/images/cube0.png").toExternalForm()),
                new Image(getClass().getResource("/images/cube6.png").toExternalForm())
        );
        steps1Label.textProperty().bind(steps1.asString());
        steps2Label.textProperty().bind(steps2.asString());
        gameOver.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                log.info("Game is over");
                log.debug("Saving result to database...");
                gameResultDao.persist(createGameResult());
                stopWatchTimeline.stop();
            }
        });
        resetGame();
    }

    private void resetGame() {
        gameState = new StonesInBoxesState(StonesInBoxesState.NEAR_GOAL);
        steps1.set(0);
        steps2.set(0);
        startTime = Instant.now();
        gameOver.setValue(false);
        displayGameState();
        createStopWatch();
        Platform.runLater(() -> messageLabel.setText("Good luck, " + player1Name +
                ", " + player2Name + "!"));
    }

    /**
     * need to change
     */
    private void displayGameState() {
        for (int i = 0; i < 15; i++) {
                ImageView view = (ImageView) gameGrid.getChildren().get(i);
                if (view.getImage() != null) {
                    log.trace("Image({}) = {}", i, view.getImage().getUrl());
                }
                view.setImage(cubeImages.get(gameState.getTray()[i]));//----

        }
    }


    public void handleClickOnCube(MouseEvent mouseEvent) throws Exception{
        int i = GridPane.getRowIndex((Node) mouseEvent.getSource() );
        //int col = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        log.debug("Box ({}) is pressed", i);
        if (! gameState.isFinished() && player1Name.equalsIgnoreCase(player1Name)) {
            steps1.set(steps1.get() + 1);
            gameState.pickBox(i);
            gameState.pick2Box(i);
            if (gameState.isFinished()) {
                gameOver.setValue(true);
                log.info("Player {} has solved the game in {} steps", player1Name,
                        steps1.get());
                messageLabel.setText("Congratulations, " + player1Name + "!");
                resetButton.setDisable(true);
                giveUpButton.setText("Finish");
            }
        }
        displayGameState();
    }

    public void handleResetButton(ActionEvent actionEvent)  {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Resetting game...");
        stopWatchTimeline.stop();
        resetGame();
    }

    public void handleGiveUpButton(ActionEvent actionEvent) throws IOException {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        log.debug("{} is pressed", buttonText);
        if (buttonText.equals("Give Up")) {
            log.info("The game has been given up");
        }
        gameOver.setValue(true);
        log.info("Loading high scores scene...");
        fxmlLoader.setLocation(getClass().getResource("/fxml/highscores.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private GameResult createGameResult() {
        GameResult result = GameResult.builder()
                .player1(player1Name)
                .winner(gameState.isFinished())
                .steps1(steps1.get())
                .player2(player2Name)
                .winner(gameState.isFinished())
                .steps2(steps2.get())
                .duration(Duration.between(startTime, Instant.now()))
                .build();
        return result;
    }

    private void createStopWatch() {
        stopWatchTimeline = new Timeline(new KeyFrame(javafx.util.Duration.ZERO, e -> {
            long millisElapsed = startTime.until(Instant.now(), ChronoUnit.MILLIS);
            stopWatchLabel.setText(DurationFormatUtils.formatDuration(millisElapsed, "HH:mm:ss"));
        }), new KeyFrame(javafx.util.Duration.seconds(1)));
        stopWatchTimeline.setCycleCount(Animation.INDEFINITE);
        stopWatchTimeline.play();
    }

}

