package gui;

import common.CommonFunc;
import dataModels.Uzytkownik;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Main;

import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created by no-one on 19.11.16.
 */
public class ProfileController {

    public TextField currentPas;
    public TextField profileId;
    public TextField profileName;
    public TextField profileSurname;
    public TextField profileLogin;
    public TextField profilePhone;
    public TextField profilePermis;
    public TextField profileDate;
    public Button changePassButton;
    public Button updateProfileButton;
    public Label profileErrorLabel;
    public TextField newPass;

    public void ProfileController(){}

    @FXML
    private void initialize() {
        this.changePassButton.setOnAction(event -> { this.changePassword();});
        this.updateProfileButton.setOnAction(event -> {this.updateProfile();});

    }

    protected void profileTabUpdate(){
        if(Main.authenticatedUser==null) return;
        this.profileId.setText(String.valueOf(Main.authenticatedUser.getId()));
        this.profileName.setText(Main.authenticatedUser.getImie());
        this.profileSurname.setText(Main.authenticatedUser.getNazwisko());
        this.profileLogin.setText(Main.authenticatedUser.getLogin());
        this.profilePermis.setText(Main.authenticatedUser.getPoziom_uprawnien()==0 ? "normal" : "super");
        this.profilePhone.setText(Main.authenticatedUser.getTelefon());
        this.profileDate.setText(CommonFunc.sqlDateToString(Main.authenticatedUser.getData_urodzenia()));
    }

    protected void changePassword(){
        this.profileErrorLabel.setText("");
        String curP = this.currentPas.getText();
        String newP = this.newPass.getText();
        if(curP.length()==0) {
            setProfileError("Nie podano obecnego hasła"); return;
        }
        if(newP.length()==0) {
            setProfileError("Nie podano nowego hasła"); return;
        }
        if(!CommonFunc.comparePass(curP, Main.authenticatedUser.getHaslo())) {
            setProfileError("Wprowadzone hasło jest nieprawidłowe");
            return;
        }

        Error e = Main.userService.changePassword(newP);
        if(e != null)
            Main.gui.showDialog("error",e.toString(), "", Alert.AlertType.ERROR);
        else
            Main.gui.showDialog("success","Pomyslnie zmieniono hasło", "", Alert.AlertType.INFORMATION);
    }

    public void refresh(){
        try {
            Main.authenticatedUser = Main.userService.authUserReload();
            profileTabUpdate();
        } catch (SQLException e) {
            Main.gui.showDialog("error","Niepowodzenie podczas pobiedania danych użytkownika", e.toString(), Alert.AlertType.ERROR);

        }
    }

    protected void updateProfile(){
        profileErrorLabel.setText("");
        try {
            Uzytkownik uz = parseUpdateForm();
            String valid = Main.userService.validate(uz);
            if(valid.length()>0){
                profileErrorLabel.setText(valid);
                return;
            }
            Error e = Main.userService.update(uz);
            if(e != null)
                Main.gui.showDialog("error",e.toString(), "", Alert.AlertType.ERROR);
            else {
                Main.gui.showDialog("success", "Pomyslnie zaktualizowano dane", "", Alert.AlertType.INFORMATION);
                profileTabUpdate();
                Main.gui.mainCtrl.reloadTopInfo();
            }
        } catch (ParseException e) {
            profileErrorLabel.setText("Niepoprawny format daty! (poprawny:dd-MM-yyyy)");
        }
    }

    protected void setProfileError(String err){
        this.profileErrorLabel.setText(err);
    }

    @FXML
    private Uzytkownik parseUpdateForm() throws ParseException {
        Uzytkownik uz = new Uzytkownik();
        uz.setImie(CommonFunc.emptyNullStr(profileName.getText()));
        uz.setNazwisko(CommonFunc.emptyNullStr(profileSurname.getText()));
        uz.setTelefon(CommonFunc.emptyNullStr(profilePhone.getText()));
        if(profileDate.getText().length() > 0 )
            uz.setData_urodzenia(CommonFunc.parseDateToSQL(profileDate.getText()));
        else
            uz.setData_urodzenia(null);
        uz.setLogin(Main.authenticatedUser.getLogin());
        uz.setHaslo(Main.authenticatedUser.getHaslo());
        uz.setId(Main.authenticatedUser.getId());
        uz.setPoziom_uprawnien(Main.authenticatedUser.getPoziom_uprawnien());
        return uz;
    }
}
