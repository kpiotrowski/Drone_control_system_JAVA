package gui.base.points;

import dataModels.Uzytkownik;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * Created by no-one on 20.11.16.
 */
public class PointsController {

    public Tab findTab;
    public Tab editTab;
    public Tab createTab;
    public TabPane tabPane;

    public PointsController(){}

    @FXML
    private void initialize(){

    }

    public void refreshPermissions(Uzytkownik uz){
        if (uz.getPoziom_uprawnien() == 0){
            this.tabPane.getSelectionModel().select(0);
            this.editTab.setDisable(true);
            this.createTab.setDisable(true);
        } else {
            this.editTab.setDisable(false);
            this.createTab.setDisable(false);
        }
    }

}
