package stonesinboxes.javafx.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class LauchController {
    @Inject
    private FXMLLoader fxmlLoader;

    private TextField player1NameTextField;

    private TextField player2NameTextField;

    private Label errorLabel;


}
