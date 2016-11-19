package gui;

import common.Consts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

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

    public GUI(){}

    public GUI(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;

        Parent mainScene = FXMLLoader.load(getClass().getResource("main.fxml"));
        this.primaryScene = new Scene(mainScene);

        Parent loginScene = FXMLLoader.load(getClass().getResource("login.fxml"));
        this.loginScene = new Scene(loginScene);

        Parent registrationScene = FXMLLoader.load(getClass().getResource("registration.fxml"));
        this.registrationScene = new Scene(registrationScene);

        primaryStage.setTitle(Consts.mainTitle);
        primaryStage.setScene(this.primaryScene);
        //primaryStage.show();
    }

    public void showRegForm(){
        this.registrationStage = new Stage();
        this.registrationStage.setTitle(Consts.registrationTitle);
        this.registrationStage.setScene(registrationScene);
        this.registrationStage.show();
    }
    public void hideRegForm(){
        if(this.registrationStage != null) this.registrationStage.close();
    }

    public void hideMainStage() { if(this.primaryStage != null) this.primaryStage.close(); }
    public void showMainStage() { if(this.primaryStage !=null) this.primaryStage.show(); }

    public Alert showDialog(String tit, String mes, String lmes, Alert.AlertType typ){
        Alert alert = new Alert(typ);
        alert.show();
        alert.setTitle(tit);
        alert.setHeaderText(mes);
        alert.setResizable(true);
        if(lmes!=""){
            alert.setContentText(lmes);
        }
        return alert;
    }

}
