package stonesinboxes.javafx.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
public class LaunchController {

    @Inject
    private FXMLLoader fxmlLoader;

    @FXML
    private TextField player1NameTextField;
    @FXML
    private TextField player2NameTextField;
    @FXML
    private Label errorLabel;

    public void startAction(ActionEvent actionEvent) throws IOException {
        if (player1NameTextField.getText().isEmpty() || player2NameTextField.getText().isEmpty()) {
            errorLabel.setText("Enter players' names");
            }else {
            fxmlLoader.setLocation(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().setPlayer1Name(player1NameTextField.getText());
            fxmlLoader.<GameController>getController().setPlayer2Name(player2NameTextField.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
                stage.show();
                log.info("Name of player1: {}, loading game scene", player1NameTextField.getText());
                log.info("Name of player2: {}, loading game scene", player2NameTextField.getText());
            }


    }


}
