package gui.login;

import common.Consts;
import dataModels.Uzytkownik;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.sql.SQLException;

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
            String loginS = login.getText();
            String passS = password.getText();

            if(loginS.length()==0) {
                this.showError("Login jest wymagany");
                return;
            }
            if(passS.length()==0){
                this.showError("Hasło jest wymagane");
                return;
            }
            Uzytkownik auth = null;
            try {
                auth = Main.userService.authenticate(loginS,passS);
                if(auth!=null) {
                    actionAfterLogin(auth);
                    clearForm();
                } else{
                    this.showError("Niepowodzenie podczas autoryzacji użytkownika");
                }
            } catch (SQLException e) {
                this.showError(e.toString());
            }
        });
    }

    private void showError(String err) {
        errorLabel.setText(err);
        errorLabel.setVisible(true);
        return;
    }

    private void clearForm(){
        login.setText("");
        password.setText("");
    }

    private void actionAfterLogin(Uzytkownik auth){
        Main.gui.hideLogForm();
        Main.authenticatedUser = auth;
        Main.gui.mainCtrl.afterLogin();
    }
}
