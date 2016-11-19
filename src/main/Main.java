package main;

import common.Consts;
import dataModels.Uzytkownik;
import databaseController.MySQLController;
import gui.GUI;
import gui.MainGUIController;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import service.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;

public final class Main extends Application{

    public static Uzytkownik authenticatedUser;

    public static GUI gui;
    public static UserService userService;
    public static DroneService droneService;
    public static PolozenieService polozenieService;
    public static PunktKontrolnyService punktKontrolnyService;
    public static ZadanieService zadanieService;
    public static TrasaService trasaService;
    public static PunktNaTrasieService punktNaTrasieService;

    @Override
    public void start(Stage primaryStage) throws IOException {
        gui = new GUI(primaryStage);
        Alert info = gui.showDialog("database connect", "Connecting to database", "", Alert.AlertType.INFORMATION);
        info.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);

        Task t = new Task(){
            @Override
            protected MySQLController call() throws SQLException {
                return new MySQLController(Consts.dbUser, Consts.dbPass, Consts.dbHost, Consts.dbPort);
            };
        };
        t.setOnSucceeded(e -> {
            MySQLController con = (MySQLController) t.getValue();
            gui.showMainStage();
            info.close();

            userService = new UserService(con);
            droneService = new DroneService(con);
            polozenieService = new PolozenieService(con);
            punktKontrolnyService = new PunktKontrolnyService(con);
            punktNaTrasieService = new PunktNaTrasieService(con);
            zadanieService = new ZadanieService(con);
            trasaService = new TrasaService(con);
        });
        t.setOnFailed(e -> {
            info.close();
            Alert error = gui.showDialog("database error", "Error when connecting to database", t.getException().toString() ,Alert.AlertType.ERROR);
        });
        new Thread(t).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
