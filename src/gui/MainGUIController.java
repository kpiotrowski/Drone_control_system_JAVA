package gui;

import common.Consts;
import dataModels.Uzytkownik;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

/**
 * Created by no-one on 17.11.16.
 */
public class MainGUIController {

    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Label usernameLabel;

    private Uzytkownik user;

    public MainGUIController(){}

    @FXML
    private void initialize() {
        registerButton.setOnAction((e) -> {
            Main.gui.showRegForm();
        });
        loginButton.setOnAction((e) -> {
            Main.gui.showLogForm();
        });
    }
}
