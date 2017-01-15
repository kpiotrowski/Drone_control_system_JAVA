package gui.registration;

import common.CommonFunc;
import dataModels.Uzytkownik;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.Main;

import java.security.MessageDigest;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by no-one on 18.11.16.
 */
public class RegistrationGUIController {
    public Color x2;
    public Font x1;
    @FXML private TextField name;
    @FXML private TextField surname;
    @FXML private TextField birthdate;
    @FXML
    private TextField phone;
    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML private Button submitButton;
    @FXML private Label errorLabel;

    public RegistrationGUIController() {}

    @FXML
    private void initialize() {
        submitButton.setOnAction((event) -> {
            errorLabel.setVisible(false);
            try {
                Uzytkownik uz = parseForm();
                Error ifValid = Main.userService.validate(uz);
                if(ifValid!=null){
                    errorLabel.setText(ifValid.getMessage());
                    errorLabel.setVisible(true);
                    return;
                }
                Main.gui.hideRegForm();
                Error e = Main.userService.insert(uz);
                if(e==null) {
                    Main.gui.showDialog("success", "Successfully registered user.", "", Alert.AlertType.INFORMATION);
                    clearForm();
                } else
                    Main.gui.showDialog("error", "Failed to register user.", e.getMessage(), Alert.AlertType.ERROR);
            } catch (ParseException e) {
                errorLabel.setText("Incorrect date format! (correct:dd-MM-yyyy)");
                errorLabel.setVisible(true);
            }
        });
    }

    @FXML
    private Uzytkownik parseForm() throws ParseException {
        Uzytkownik uz = new Uzytkownik();
        uz.setImie(CommonFunc.emptyNullStr(name.getText()));
        uz.setNazwisko(CommonFunc.emptyNullStr(surname.getText()));
        uz.setLogin(CommonFunc.emptyNullStr(login.getText()));
        uz.setTelefon(CommonFunc.emptyNullStr(phone.getText()));
        uz.setPoziom_uprawnien(0);
        uz.setHaslo(CommonFunc.hashPass(password.getText()));
        if(birthdate.getText().length() > 0 )
            uz.setData_urodzenia(CommonFunc.parseDateToSQL(birthdate.getText()));
        else
            uz.setData_urodzenia(null);
        return uz;
    }

    private void clearForm(){
        name.setText("");
        surname.setText("");
        login.setText("");
        phone.setText("");
        password.setText("");
        birthdate.setText("");
        errorLabel.setVisible(false);
    }

}
