package gui.base.job;

import dataModels.Uzytkownik;
import dataModels.Zadanie;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by no-one on 06.01.17.
 */
public class JobsController {

    public TableView<Zadanie> tableView;
    public TableColumn<Object, Object> columnId;
    public TableColumn<Object, Object> columnDataStart;
    public TableColumn<Object, Object> columnType;
    public TableColumn<Object, Object> columnRouteName;
    public TableColumn<Object, Object> columnUserId;
    public TableColumn<Object, Object> columnDrone;
    public TableColumn<Object, Object> columnFinishPoint;
    public TableColumn<Object, Object> columnStatus;

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
        this.columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.columnDataStart.setCellValueFactory(new PropertyValueFactory<>("data_rozpoczenia"));
        this.columnType.setCellValueFactory(new PropertyValueFactory<>("typeString"));
        this.columnRouteName.setCellValueFactory(new PropertyValueFactory<>("trasa_nazwa"));
        this.columnUserId.setCellValueFactory(new PropertyValueFactory<>("trasa_uzytkownik_id"));
        this.columnDrone.setCellValueFactory(new PropertyValueFactory<>("dron_id"));
        this.columnFinishPoint.setCellValueFactory(new PropertyValueFactory<>("punkt_koncowy_id"));
        this.columnStatus.setCellValueFactory(new PropertyValueFactory<>("stanString"));


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
