package stonesinboxes.javafx.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
public class LauchController {
    @Inject
    private FXMLLoader fxmlLoader;
    @FXML
    private TextField player1NameTextField;
    @FXML
    private TextField player2NameTextField;
    @FXML
    private Label errorLabel;

    public void startAction(ActionEvent actionEvent) throws IOException{
        if (player1NameTextField.getText().isEmpty() && player2NameTextField.getText().isEmpty()){
            errorLabel.setText("Enter players' names");
        } else {
            fxmlLoader.setLocation(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();

        }
    }


}
