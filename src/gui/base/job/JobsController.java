package gui.base.job;

import dataModels.Punkt_kontrolny;
import dataModels.Uzytkownik;
import dataModels.Zadanie;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

/**
 * Created by no-one on 06.01.17.
 */
public class JobsController {

    public TableView<Zadanie> tableView;
    public TableColumn columnId;
    public TableColumn columnDataStart;
    public TableColumn columnType;
    public TableColumn columnRouteName;
    public TableColumn columnUserName;
    public TableColumn columnDrone;
    public TableColumn columnFinishPoint;

    public JobsController(){}

    @FXML
    private void initialize() {
        this.tableView.setRowFactory( tv -> {
            TableRow<Zadanie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Zadanie rowData = row.getItem();
                    this.setInfoForm(rowData);
                }
            });
            return row ;
        });


    }

    public void refreshPermissions(Uzytkownik uz){
        if (uz.getPoziom_uprawnien() == 0){
        //    this.tabPane.getSelectionModel().select(0);
        //    this.editTab.setDisable(true);
        //    this.createTab.setDisable(true);
        } else {
        //    this.editTab.setDisable(false);
        //    this.createTab.setDisable(false);
        }
    }

    private void setInfoForm(Zadanie z){
        //TODO IMPLEMENT THIS
    }
}
