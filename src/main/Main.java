package main;

import common.Consts;
import dataModels.Uzytkownik;
import databaseController.MySQLController;
import gui.GUI;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import service.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class Main extends Application{

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
        gui = new GUI();
        gui.initialize(primaryStage);

        Alert info = gui.showDialog("database connect", "Connecting to the database", "", Alert.AlertType.INFORMATION);
        info.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);

        Task t = new Task(){
            @Override
            protected MySQLController call() throws SQLException, IOException {
                FileInputStream in = new FileInputStream("config.conf");
                Properties conf = new Properties();
                conf.load(in);
                MySQLController my = new MySQLController(conf.getProperty("DATABASE_USER"),
                        conf.getProperty("DATABASE_PASS"), conf.getProperty("DATABASE_HOST"), Consts.dbPort);
                in.close();
                return my;
            };
        };
        t.setOnSucceeded(e -> {
            MySQLController con = (MySQLController) t.getValue();
            gui.setDatabaseStatus("Connected to the database",true);
            gui.showMainStage();
            info.close();
            con.pinger();

            userService = new UserService(con);
            polozenieService = new PolozenieService(con);
            punktKontrolnyService = new PunktKontrolnyService(con);
            punktNaTrasieService = new PunktNaTrasieService(con);
            zadanieService = new ZadanieService(con);
            trasaService = new TrasaService(con);
            droneService = new DroneService(con, punktKontrolnyService);
        });
        t.setOnFailed(e -> {
            if(t.getException() instanceof SQLException)
                gui.showDialog("database error", "Error when connecting to database", t.getException().toString() ,Alert.AlertType.ERROR);
            else
                gui.showDialog("database error", "Incorrect config file", "" ,Alert.AlertType.ERROR);
            info.close();
        });
        new Thread(t).start();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
