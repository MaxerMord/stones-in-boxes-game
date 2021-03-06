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
import javafx.scene.control.RadioButton;
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
import javax.persistence.Column;
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

    @Column(nullable = false)
    private String player1Name;
    @Column(nullable = false)
    private String player2Name;

    private StonesInBoxesState gameState;
    private IntegerProperty steps = new SimpleIntegerProperty();
    private Instant startTime;
    private List<Image> boxImages;

    @FXML
    private RadioButton rb;

    @FXML
    private Label messageLabel;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Label stepsLabel;


    @FXML
    private Label stopWatchLabel;

    private Timeline stopWatchTimeline;

    @FXML
    private Label playerLabel;

    @FXML
    private Button resetButton;

    @FXML
    private Button giveUpButton;


    private BooleanProperty gameOver = new SimpleBooleanProperty();

    /**
     * Setters
     */

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    private int count = 1;

    /**
     * Game initializer.
     */
    @FXML
    public void initialize() {
        boxImages = List.of(
                new Image(getClass().getResource("/images/cube0.png").toExternalForm()),
                new Image(getClass().getResource("/images/cube6.png").toExternalForm())
        );
        stepsLabel.textProperty().bind(steps.asString());
        gameOver.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                log.info("Game is over");
                log.debug("Saving result to database...");
                try {
                    gameResultDao.persist(createGameResult());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                stopWatchTimeline.stop();
            }
        });
        resetGame();
    }

    private void resetGame() {
        gameState = new StonesInBoxesState(StonesInBoxesState.INITIAL);
        steps.set(0);
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
            view.setImage(boxImages.get(gameState.getTray()[i]));

        }
    }

    /**
     * Event handler while clicking on box. Player1 can click if count = 0, Player2 can click
     * if count = 1.
     *
     * @param mouseEvent click
     */
    public void handleClickOnBox(MouseEvent mouseEvent) {
        int col = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        log.debug("Box ({}) is pressed", col);
        playerLabel.setText(player1Name);

        if (rb.isSelected() && !gameState.isFinished() && gameState.canPick2(col)) {
            gameState.pick2Box(col);
            steps.set(steps.get() + 1);
            if (count == 1) playerLabel.setText(player1Name);
            else playerLabel.setText(player2Name);
            if (count < 2) {
                count ++;
            } else {
                count = 0;
            }

        } else if (!gameState.isFinished() && rb.isSelected()) {
            log.info("Adjacent two boxes from {} are unavailable, switch to pick 1 box or choose other.");
        }
        if (! rb.isSelected() && ! gameState.isFinished() && gameState.canPick1(col)){
            gameState.pickBox(col);
            steps.set(steps.get() + 1);
            if (count == 1) playerLabel.setText(player1Name);
            else playerLabel.setText(player2Name);
            if (count < 2) {
                count ++;
            } else {
                count = 0;
            }
        }
        if (gameState.isFinished()) {
            gameOver.setValue(true);
            if (count == 0) {
                log.info("Player {} has won the game in {} steps", player1Name,
                        steps.get());
                messageLabel.setText("Congratulations, " + player1Name + "!");
            } else {
                log.info("Player {} has won the game in {} steps", player2Name,
                        steps.get());
                messageLabel.setText("Congratulations, " + player1Name + "!");
            }

            gameGrid.setDisable(true);
            resetButton.setDisable(true);
            playerLabel.setText("");
            giveUpButton.setText("Finish");
        }

//        countLable.setText(Integer.toString(count));
        displayGameState();
    }

    public void handleResetButton(ActionEvent actionEvent) {
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

    private GameResult createGameResult() throws Exception{
        GameResult result = null;
        if (count == 1) {
            result = GameResult.builder()
                    .player(player1Name)
                    .solved(gameState.isFinished())
                    .duration(Duration.between(startTime, Instant.now()))
                    .steps(steps.get())
                    .build();
        } else {
            result = GameResult.builder()
                    .player(player2Name)
                    .solved(gameState.isFinished())
                    .duration(Duration.between(startTime, Instant.now()))
                    .steps(steps.get())
                    .build();
        }
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

