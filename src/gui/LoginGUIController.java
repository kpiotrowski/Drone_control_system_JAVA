package gui;

import common.Consts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by no-one on 18.11.16.
 */
public class LoginGUIController {
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private Button submitButton;
    @FXML
    private Label errorLabel;

    public LoginGUIController(){}

    @FXML
    private void initialize() {
        submitButton.setOnAction((event) -> {
            submitButton.setText("Dupa");
        });
    }
}
