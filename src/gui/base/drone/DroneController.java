package gui.base.drone;

import common.FilterParam;
import dataModels.DataModel;
import dataModels.Dron;
import dataModels.Punkt_kontrolny;
import dataModels.Uzytkownik;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static common.CommonFunc.strToFloat;
import static common.CommonFunc.strToInteger;

/**
 * Created by no-one on 19.11.16.
 */
public class DroneController {

    public Label droneEditError;
    public TextField droneEditId;
    public TextField droneEditName;
    public TextField droneEditBattery;
    public TextArea droneEditDesc;
    public Label droneCreateError;
    public Label droneFindError;
    public TableColumn tabDronePointId;
    public TableColumn tabDroneKoorZ;
    public TableColumn tabDroneKoorY;
    public TableColumn tabDroneKoorX;
    public TableColumn tabDroneState;
    public TableColumn tabDroneBattery;
    public TableColumn tabDroneFlightTime;
    public TableColumn tabDroneSpeed;
    public TableColumn tabDroneRotors;
    public TableColumn tabDroneWeight;
    public TableColumn tabDroneName;
    public TableColumn tabDroneId;
    @FXML private TableView tableView;
    @FXML private Button droneFindButton;
    @FXML private Button droneCreateButton;
    @FXML private Button droneEdiButton;
    @FXML private Button droneDeleteAction;
    @FXML private Tab tabEdit;
    @FXML private TabPane tabPane;
    @FXML private Tab createTab;

    private List<DataModel> foundDrones;

    @FXML
    private void initialize() {
        this.tableView.setRowFactory( tv -> {
            TableRow<Dron> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Dron rowData = row.getItem();
                    this.setDroneEditForm(rowData);
                }
            });
            return row ;
        });
        this.droneDeleteAction.setOnAction(event -> {this.deleteAction();});
        this.droneCreateButton.setOnAction(event -> {this.creteAction();});
        this.droneFindButton.setOnAction(event -> {this.findAction();});
        this.droneEdiButton.setOnAction(event -> {this.editAction();});
        this.tabDronePointId.setCellValueFactory(new PropertyValueFactory<>("Punkt_Kontrolny_id"));
        this.tabDroneKoorZ.setCellValueFactory(new PropertyValueFactory<>("wspz"));
        this.tabDroneKoorY.setCellValueFactory(new PropertyValueFactory<>("wspy"));
        this.tabDroneKoorX.setCellValueFactory(new PropertyValueFactory<>("wspx"));
        this.tabDroneState.setCellValueFactory(new PropertyValueFactory<>("stan"));
        this.tabDroneBattery.setCellValueFactory(new PropertyValueFactory<>("poziom_baterii"));
        this.tabDroneFlightTime.setCellValueFactory(new PropertyValueFactory<>("max_czas_lotu"));
        this.tabDroneSpeed.setCellValueFactory(new PropertyValueFactory<>("max_predkosc"));
        this.tabDroneRotors.setCellValueFactory(new PropertyValueFactory<>("ilosc_wirnikow"));
        this.tabDroneWeight.setCellValueFactory(new PropertyValueFactory<>("masa"));
        this.tabDroneName.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        this.tabDroneId.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    public void refreshPermissions(Uzytkownik uz) {
        if (uz.getPoziom_uprawnien() == 0){
            this.tabPane.getSelectionModel().select(0);
            this.tabEdit.setDisable(true);
            this.createTab.setDisable(true);
        } else {
            this.tabEdit.setDisable(false);
            this.createTab.setDisable(false);
        }
    }
    protected void updateTableView(List<DataModel> dataList){
        foundDrones=dataList;
        ObservableList<Dron> data = FXCollections.observableArrayList();
        for (DataModel m: dataList) {
            Dron d = (Dron)m;
            data.add(d);
        }
        this.tableView.setItems(data);
    }

    private void setDroneEditForm(Dron d){
        this.droneEditId.setText(String.valueOf(d.getId()));
        this.droneEditName.setText(d.getNazwa());
        this.droneEditBattery.setText(String.valueOf(d.getPoziom_baterii()));
        this.droneEditDesc.setText(d.getOpis());
        this.tabPane.getSelectionModel().select(tabEdit);
    }

    private void editAction(){
        this.droneEditError.setText("");
        Dron d = parseEditForm();
        Error valid = validateEditForm(d);
        if(valid!=null){
            this.droneEditError.setText(valid.toString());
            return;
        }
        Task t = new Task() {
            protected Error call() throws Exception { return Main.droneService.update(d); }
        };
        t.setOnSucceeded(event -> {
            Error e = (Error) t.getValue();
            if(e != null)
                Main.gui.showDialog("Błąd","Niepowodzenie aktualizacji informacji o dronie", e.toString(), Alert.AlertType.ERROR);
            else
                Main.gui.showDialog("Info","Pomyśłnie zaktualizowano informacje o dronie","", Alert.AlertType.INFORMATION);
        });
        new Thread(t).start();
    }

    private void findAction(){
        this.droneFindError.setText("");
        ArrayList<FilterParam> filterList = new ArrayList<>();
        try{

            //TODO IMPLEMENT THIS


            Task t = new Task() {
                protected List<DataModel> call() throws SQLException {
                    return Main.droneService.find(filterList);
                }
            };
            t.setOnFailed(event -> {
                Main.gui.showDialog("Błąd","Błąd podczas wyszukiwania danych",t.getException().getMessage(), Alert.AlertType.ERROR);
            });
            t.setOnSucceeded(event -> {
                List<DataModel> resultList = (List<DataModel>) t.getValue();
                this.updateTableView(resultList);
            });
            new Thread(t).start();
        } catch (NullPointerException e){
            this.droneFindError.setText(e.getMessage());
        }
    }

    private void creteAction(){
        this.droneCreateError.setText("");
        //TODO IMPLEMENT THIS
    }

    private void deleteAction(){
        //TODO IMPLEMENT THIS

    }

    private Dron parseCreateForm(){
        Dron d = new Dron();


        //TODO IMPLEMENT THIS
        return null;
    }

    private Dron parseEditForm(){
        Dron d = new Dron();
        d.setId(strToInteger(this.droneEditId.getText()));
        d.setNazwa(this.droneEditName.getText());
        d.setOpis(this.droneEditDesc.getText());
        d.setPoziom_baterii(strToFloat(this.droneEditBattery.getText()));
        return d;
    }

    private Error validateEditForm(Dron d){
        if(d.getId()==null) return new Error("Nieprawidłowe pole ID");
        if(d.getPoziom_baterii()==null) return new Error("Nieprawodłowy poziom baterii");
        return null;
    }


}
