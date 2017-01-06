package gui.base;

import gui.base.drone.DroneController;
import gui.base.job.JobsController;
import gui.base.points.PointsController;
import gui.base.profile.ProfileController;
import gui.base.route.RouteController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import main.Controller;
import main.Main;

/**
 * Created by no-one on 17.11.16.
 */
public class MainGUIController extends Controller {

    @FXML private Button reload;
    @FXML private TabPane mainTabPane;
    @FXML private Button registerButton;
    @FXML private Button loginButton;
    @FXML private Button logoutButton;
    @FXML private Label usernameLabel;
    @FXML private Label databaseLabel;
    @FXML private Parent profile;
    @FXML private ProfileController profileController;
    @FXML private Parent points;
    @FXML private PointsController pointsController;
    @FXML private DroneController droneController;
    @FXML private RouteController routeController;
    @FXML private JobsController jobsController;

    @FXML
    private void initialize() {
        Main.gui.mainCtrl = this;
        registerButton.setOnAction((e) -> {Main.gui.showRegForm();});
        loginButton.setOnAction((e) -> {Main.gui.showLogForm();});
        logoutButton.setOnAction((e) -> {this.logout();});
        reload.setOnAction(event -> {this.reload();});
    }

    public void afterLogin(){
        this.registerButton.setVisible(false);
        this.loginButton.setVisible(false);
        this.logoutButton.setVisible(true);
        this.usernameLabel.setVisible(true);
        this.mainTabPane.setVisible(true);
        this.reload.setVisible(true);
        this.profileController.profileTabUpdate();
        this.reloadTopInfo();
        this.reloadPermissions();
        this.droneController.afterLogin();
    }

    public void reloadTopInfo(){
        this.usernameLabel.setText(Main.authenticatedUser.getImie()+" "+Main.authenticatedUser.getNazwisko());
    }

    public void reload(){
        this.profileController.refresh();
        reloadTopInfo();
        reloadPermissions();
    }

    private void reloadPermissions(){
        this.pointsController.refreshPermissions(Main.authenticatedUser);
        this.droneController.refreshPermissions(Main.authenticatedUser);
        this.routeController.refreshPermissions(Main.authenticatedUser);
        this.jobsController.refreshPermissions(Main.authenticatedUser);
    }

    private void logout(){
        this.reload.setVisible(false);
        this.mainTabPane.setVisible(false);
        this.registerButton.setVisible(true);
        this.loginButton.setVisible(true);
        this.logoutButton.setVisible(false);
        this.usernameLabel.setVisible(false);
        Main.authenticatedUser = null;
    }

    public void setDatabaseStatus(String str, boolean ok){
        this.databaseLabel.setText(str);
        if (ok)this.databaseLabel.setTextFill(Color.web("#99daff"));
        else this.databaseLabel.setTextFill(Color.RED);
    }

}
