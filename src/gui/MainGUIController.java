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
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import main.Controller;
import main.Main;

import java.io.IOException;

/**
 * Created by no-one on 17.11.16.
 */
public class MainGUIController extends Controller {

    public Button reload;
    public Tab usersId;
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label databaseLabel;
    @FXML
    private Parent profile;
    @FXML
    private ProfileController profileController;

    public MainGUIController(){}

    @FXML
    private void initialize() {
        Main.gui.mainCtrl = this;

        registerButton.setOnAction((e) -> {Main.gui.showRegForm();});
        loginButton.setOnAction((e) -> {Main.gui.showLogForm();});
        logoutButton.setOnAction((e) -> {this.logout();});
        reload.setOnAction(event -> {this.reload();});
    }

    public void afterLogin(){
        this.registerButton.setVisible(false);
        this.loginButton.setVisible(false);
        this.logoutButton.setVisible(true);
        reloadTopInfo();
        this.usernameLabel.setVisible(true);
        this.mainTabPane.setVisible(true);
        this.reload.setVisible(true);
        if(Main.authenticatedUser.getPoziom_uprawnien()>0) this.usersId.setDisable(false);
        this.profileController.profileTabUpdate();
    }

    public void reloadTopInfo(){
        this.usernameLabel.setText(Main.authenticatedUser.getImie()+" "+Main.authenticatedUser.getNazwisko());
    }


    public void reload(){
        this.profileController.refresh();
        reloadTopInfo();
    }

    private void logout(){
        this.reload.setVisible(false);
        this.mainTabPane.setVisible(false);
        this.registerButton.setVisible(true);
        this.loginButton.setVisible(true);
        this.logoutButton.setVisible(false);
        this.usernameLabel.setVisible(false);
        Main.authenticatedUser = null;
    }

    public void setDatabaseStatus(String str, boolean ok){
        this.databaseLabel.setText(str);
        if (ok)this.databaseLabel.setTextFill(Color.web("#99daff"));
        else this.databaseLabel.setTextFill(Color.RED);
    }

}
