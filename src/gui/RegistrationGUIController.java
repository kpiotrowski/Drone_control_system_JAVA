package gui;

import common.Consts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

/**
 * Created by no-one on 18.11.16.
 */
public class RegistrationGUIController {
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField birthdate;
    @FXML
    private TextField phone;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button submitButton;
    @FXML
    private Label errorLabel;

    public RegistrationGUIController() {
    }

    @FXML
    private void initialize() {
        submitButton.setOnAction((event) -> {
            submitButton.setText("Klik");
        });
    }



}
