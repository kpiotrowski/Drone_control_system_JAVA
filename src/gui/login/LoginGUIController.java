package gui.login;

import common.Consts;
import dataModels.Uzytkownik;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.sql.SQLException;

import static common.CommonFunc.clearForm;

/**
 * Created by no-one on 18.11.16.
 */
public class LoginGUIController {
    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML private Button submitButton;
    @FXML private Label errorLabel;
    private Control[] form;

    public LoginGUIController(){}

    @FXML
    private void initialize() {
        submitButton.setOnAction((event) -> {
            String loginS = login.getText();
            String passS = password.getText();

            if(loginS.length()==0) {
                this.showError("Login is required");
                return;
            }
            if(passS.length()==0){
                this.showError("Password is required");
                return;
            }
            Uzytkownik auth = null;
            try {
                auth = Main.userService.authenticate(loginS,passS);
                if(auth!=null) {
                    actionAfterLogin(auth);
                    clearForm(form);
                } else this.showError("Failed to authorize user.");

            } catch (SQLException e) {
                this.showError(e.getMessage());
            }
        });
        this.form = new Control[]{login,password,errorLabel};
    }

    private void showError(String err) {
        errorLabel.setText(err);
        errorLabel.setVisible(true);
    }

    private void actionAfterLogin(Uzytkownik auth){
        Main.gui.hideLogForm();
        Main.authenticatedUser = auth;
        Main.gui.mainCtrl.afterLogin();
    }
}
