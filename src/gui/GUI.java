package gui;

import common.Consts;
import common.FilterParam;
import dataModels.DataModel;
import dataModels.Punkt_kontrolny;
import gui.base.MainGUIController;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by no-one on 18.11.16.
 */
public class GUI {

    private Stage primaryStage;
    private Scene primaryScene;
    private Scene registrationScene;
    private Stage registrationStage;
    private Scene loginScene;
    private Stage loginStage;

    public MainGUIController mainCtrl;

    public GUI(){}

    public void initialize(Stage primaryStage) throws IOException {
        Parent mainScene = FXMLLoader.load(getClass().getResource("base/main.fxml"));
        this.primaryScene = new Scene(mainScene);

        Parent loginScene = FXMLLoader.load(getClass().getResource("login/login.fxml"));
        this.loginScene = new Scene(loginScene);

        Parent registrationScene = FXMLLoader.load(getClass().getResource("registration/registration.fxml"));
        this.registrationScene = new Scene(registrationScene);

        this.primaryStage = primaryStage;

        primaryStage.setTitle(Consts.mainTitle);
        primaryStage.setScene(this.primaryScene);
    }

    public void showRegForm(){
        if(this.registrationStage==null) {
            this.registrationStage = new Stage();
            this.registrationStage.setTitle(Consts.registrationTitle);
            this.registrationStage.setScene(registrationScene);
        }
        this.registrationStage.show();
    }
    public void hideRegForm(){ if(this.registrationStage != null) this.registrationStage.close();}

    public void showLogForm(){
        if(this.loginStage==null){
            this.loginStage = new Stage();
            this.loginStage.setTitle(Consts.loginTitle);
            this.loginStage.setScene(loginScene);
        }
        this.loginStage.show();
    }
    public void hideLogForm(){ if(this.loginStage != null) this.loginStage.close(); }

    public void showMainStage() { if(this.primaryStage !=null) this.primaryStage.show(); }
    public void hideMainStage() { if(this.primaryStage != null) this.primaryStage.close(); }

    public Alert showDialog(String tit, String mes, String lmes, Alert.AlertType typ){
        Alert alert = new Alert(typ);
        alert.setTitle(tit);
        alert.setHeaderText(mes);
        alert.setResizable(true);
        if(!lmes.equals("")) alert.setContentText(lmes);
        alert.show();
        return alert;
    }

    public void setDatabaseStatus(String str, boolean ok){
        this.mainCtrl.setDatabaseStatus(str, ok);
    }

    public Task getDronePointsTask(boolean free){
        ArrayList<FilterParam> filterList = new ArrayList<>();
        if(free) filterList.add(FilterParam.newF("max_ilosc_dronow-obecna_ilosc_dronow", ">", 0));
        Task t = new Task() {
            protected List<DataModel> call() throws SQLException {
                return Main.punktKontrolnyService.find(filterList);
            }
        };
        return t;
    }
    public Task getRoutesTask(Integer userId){
        Task t = new Task() {
            protected List<DataModel> call() throws SQLException {
                return Main.trasaService.find(Main.authenticatedUser.getId());
            }
        };
        return t;
    }
}
