package gui.base.profile;

import common.CommonFunc;
import dataModels.Uzytkownik;
import javafx.concurrent.Task;
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
    @FXML private Label passwordErrorLabel;
    @FXML private TextField currentPas;
    @FXML private TextField profileId;
    @FXML private TextField profileName;
    @FXML private TextField profileSurname;
    @FXML private TextField profileLogin;
    @FXML private TextField profilePhone;
    @FXML private TextField profilePermis;
    @FXML private TextField profileDate;
    @FXML private Button changePassButton;
    @FXML private Button updateProfileButton;
    @FXML private Label profileErrorLabel;
    @FXML private TextField newPass;

    public void ProfileController(){}

    @FXML
    private void initialize() {
        this.changePassButton.setOnAction(event -> { this.changePassword();});
        this.updateProfileButton.setOnAction(event -> {this.updateProfile();});

    }

    public void profileTabUpdate(){
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
        this.passwordErrorLabel.setText("");
        String curP = this.currentPas.getText();
        String newP = this.newPass.getText();
        this.currentPas.setText("");
        this.newPass.setText("");
        if(curP.length()==0) {
            setProfileError("Nie podano obecnego hasła",1); return;
        }
        if(newP.length()==0) {
            setProfileError("Nie podano nowego hasła",1); return;
        }
        if(!CommonFunc.comparePass(curP, Main.authenticatedUser.getHaslo())) {
            setProfileError("Wprowadzone hasło jest nieprawidłowe",1);
            return;
        }
        Task t = new Task() {
            protected Error call() throws Exception {
                return Main.userService.changePassword(newP);
            }
        };
        t.setOnSucceeded(event -> {
            Error e = (Error) t.getValue();
            if(e != null)
                Main.gui.showDialog("error",e.toString(), "", Alert.AlertType.ERROR);
            else
                Main.gui.showDialog("success","Pomyslnie zmieniono hasło", "", Alert.AlertType.INFORMATION);
        });
        new Thread(t).start();
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
            Error valid = Main.userService.validate(uz);
            if(valid!=null){
                setProfileError(valid.toString(),0);
                return;
            }
            Task t = new Task() {
                protected Error call() { return Main.userService.update(uz); }
            };
            t.setOnSucceeded(event -> {
                Error e = (Error) t.getValue();
                if(e != null) Main.gui.showDialog("error",e.toString(), "", Alert.AlertType.ERROR);
                else {
                    Main.gui.showDialog("success", "Pomyslnie zaktualizowano dane", "", Alert.AlertType.INFORMATION);
                    profileTabUpdate();
                    Main.gui.mainCtrl.reloadTopInfo();
                }
            });
            new Thread(t).start();
        } catch (ParseException e) {
            setProfileError("Niepoprawny format daty! (poprawny:dd-MM-yyyy)",0);
        }
    }

    protected void setProfileError(String err, int error){
        if(error == 0 ) this.profileErrorLabel.setText(err);
        else this.passwordErrorLabel.setText(err);
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
