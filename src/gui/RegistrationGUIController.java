package gui;

import common.CommonFunc;
import dataModels.Uzytkownik;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.Main;

import java.security.MessageDigest;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;

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
    private TextField login;
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
            errorLabel.setVisible(false);
            try {
                Uzytkownik uz = parseForm();
                String ifValid = Main.userService.validate(uz);
                if(ifValid.length()>0){
                    errorLabel.setText(ifValid);
                    errorLabel.setVisible(true);
                    return;
                }
                Main.gui.hideRegForm();
                try {
                    Main.userService.insert(uz);
                    Main.gui.showDialog("sukces", "Pomyslnie zarejestrowano użytkownika.", "", Alert.AlertType.INFORMATION);
                    clearForm();
                } catch (SQLException e) {
                    Main.gui.showDialog("error", "Wystąpił błąd podczas rejestracji", e.toString(), Alert.AlertType.ERROR);
                }
            } catch (ParseException e) {
                errorLabel.setText("Niepoprawny format daty! (poprawny:dd-MM-yyyy)");
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
    }

}
