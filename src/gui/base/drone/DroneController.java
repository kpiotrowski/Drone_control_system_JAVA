package gui.base.drone;

import common.FilterParam;
import common.RunOnFinish;
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

import static common.CommonFunc.*;
import static common.CommonTask.emptyRunOnFinish;
import static common.CommonTask.onSuccessSimpleError;

/**
 * Created by no-one on 19.11.16.
 */
public class DroneController {

    @FXML private TextField droneCreateName;
    @FXML private TextField droneCreateWeight;
    @FXML private TextField droneCreateRotors;
    @FXML private TextField droneCreateSpeed;
    @FXML private TextField droneCreateFlightTime;
    @FXML private TextArea droneCreateDesc;
    @FXML private ChoiceBox<Punkt_kontrolny> droneCreateDronePoint;

    @FXML private TextField droneFindSpeed;
    @FXML private ChoiceBox<Punkt_kontrolny> droneFindDronePoint;
    @FXML private Tab findTab;
    @FXML private TextField droneFindName;
    @FXML private TextField droneFindKoorXFrom;
    @FXML private TextField droneFindKoorXTo;
    @FXML private TextField droneFindKoorYFrom;
    @FXML private TextField droneFindKoorYTo;
    @FXML private TextField droneFindKoorZFrom;
    @FXML private TextField droneFindKoorZTo;
    @FXML private TextField droneFindFlightTime;
    @FXML private TextField droneFindWeightFrom;
    @FXML private TextField droneFindWeightTo;
    @FXML private CheckBox droneFindFree;
    @FXML private Label droneEditError;
    @FXML private TextField droneEditId;
    @FXML private TextField droneEditName;
    @FXML private TextField droneEditBattery;
    @FXML private TextArea droneEditDesc;
    @FXML private Label droneCreateError;
    @FXML private Label droneFindError;
    @FXML private TableColumn<Object, Object> tabDronePointId;
    @FXML private TableColumn<Object, Object> tabDroneKoorZ;
    @FXML private TableColumn<Object, Object> tabDroneKoorY;
    @FXML private TableColumn<Object, Object> tabDroneKoorX;
    @FXML private TableColumn<Object, Object> tabDroneState;
    @FXML private TableColumn<Object, Object> tabDroneBattery;
    @FXML private TableColumn<Object, Object> tabDroneFlightTime;
    @FXML private TableColumn<Object, Object> tabDroneSpeed;
    @FXML private TableColumn<Object, Object> tabDroneRotors;
    @FXML private TableColumn<Object, Object> tabDroneWeight;
    @FXML private TableColumn<Object, Object> tabDroneName;
    @FXML private TableColumn<Object, Object> tabDroneId;
    @FXML private TableView<Dron> tableView;
    @FXML private Button droneFindButton;
    @FXML private Button droneCreateButton;
    @FXML private Button droneEdiButton;
    @FXML private Button droneDeleteAction;
    @FXML private Tab tabEdit;
    @FXML private TabPane tabPane;
    @FXML private Tab createTab;

    private Dron selectedDrone;
    private Control[] findForm;
    private Control[] createForm;
    private Control[] editForm;
    private List<Dron> tableList;

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
            return row;
        });
        this.createTab.setOnSelectionChanged(event -> {
            if(this.createTab.isSelected())this.getPoints(this.droneCreateDronePoint, true);});
        this.findTab.setOnSelectionChanged(event -> {
            if(this.findTab.isSelected())this.getPoints(this.droneFindDronePoint, false);});
        this.droneDeleteAction.setOnAction(event -> {this.deleteAction();});
        this.droneCreateButton.setOnAction(event -> {this.creteAction();});
        this.droneFindButton.setOnAction(event -> {this.findAction();});
        this.droneEdiButton.setOnAction(event -> {this.editAction();});
        this.tabDronePointId.setCellValueFactory(new PropertyValueFactory<>("punkt_kontrolny_id"));
        this.tabDroneKoorZ.setCellValueFactory(new PropertyValueFactory<>("wspz"));
        this.tabDroneKoorY.setCellValueFactory(new PropertyValueFactory<>("wspy"));
        this.tabDroneKoorX.setCellValueFactory(new PropertyValueFactory<>("wspx"));
        this.tabDroneState.setCellValueFactory(new PropertyValueFactory<>("stanString"));
        this.tabDroneBattery.setCellValueFactory(new PropertyValueFactory<>("poziom_baterii"));
        this.tabDroneFlightTime.setCellValueFactory(new PropertyValueFactory<>("max_czas_lotu"));
        this.tabDroneSpeed.setCellValueFactory(new PropertyValueFactory<>("max_predkosc"));
        this.tabDroneRotors.setCellValueFactory(new PropertyValueFactory<>("ilosc_wirnikow"));
        this.tabDroneWeight.setCellValueFactory(new PropertyValueFactory<>("masa"));
        this.tabDroneName.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        this.tabDroneId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.findForm = new Control[]{this.droneFindError, this.droneFindError, this.droneFindName,
                this.droneFindKoorXFrom, this.droneFindKoorXTo, this.droneFindKoorYFrom, this.droneFindKoorYTo,
                this.droneFindKoorZFrom, this.droneFindKoorZTo, this.droneFindFlightTime, this.droneFindSpeed,
                this.droneFindWeightFrom, this.droneFindWeightTo, this.droneFindFree, this.droneFindDronePoint};
        this.createForm = new Control[]{this.droneCreateName, this.droneCreateWeight, this.droneCreateRotors,
                this.droneCreateSpeed, this.droneCreateFlightTime, this.droneCreateDesc, this.droneCreateDronePoint};
        this.editForm = new Control[]{this.droneEditId, this.droneEditName, this.droneEditBattery,
                this.droneEditDesc, this.droneDeleteAction, this.droneEdiButton,};
    }

    public void refreshPermissions(Uzytkownik uz) {
        clearForm(this.findForm);
        clearForm(this.createForm);
        clearEditForm();
        this.tableList= new ArrayList<>();
        updateTableView(this.tableList);
        if (uz.getPoziom_uprawnien() == 0){
            this.tabPane.getSelectionModel().select(0);
            this.tabEdit.setDisable(true);
            this.createTab.setDisable(true);
        } else {
            this.tabEdit.setDisable(false);
            this.createTab.setDisable(false);
        }
        this.getPoints(this.droneFindDronePoint, false);
        this.getPoints(this.droneCreateDronePoint, true);
    }

    private void findAction(){
        this.droneFindError.setText("");
        ArrayList<FilterParam> filterList = new ArrayList<>();
        try{
            if(this.droneFindName.getText().length() > 0)
                filterList.add(FilterParam.newF("nazwa", "LIKE", this.droneFindName.getText() ));
            if(this.droneFindKoorXFrom.getText().length() > 0)
                filterList.add(FilterParam.newF("wspx", ">=", strToFloat(this.droneFindKoorXFrom.getText()) ));
            if(this.droneFindKoorXTo.getText().length() > 0)
                filterList.add(FilterParam.newF("wspx", "<=", strToFloat(this.droneFindKoorXTo.getText()) ));
            if(this.droneFindKoorYFrom.getText().length() > 0)
                filterList.add(FilterParam.newF("wspy", ">=", strToFloat(this.droneFindKoorYFrom.getText()) ));
            if(this.droneFindKoorYTo.getText().length() > 0)
                filterList.add(FilterParam.newF("wspy", "<=", strToFloat(this.droneFindKoorYTo.getText()) ));
            if(this.droneFindKoorZFrom.getText().length() > 0)
                filterList.add(FilterParam.newF("wspz", ">=", strToFloat(this.droneFindKoorZFrom.getText()) ));
            if(this.droneFindKoorZTo.getText().length() > 0)
                filterList.add(FilterParam.newF("wspz", "<=", strToFloat(this.droneFindKoorZTo.getText()) ));
            if(this.droneFindFlightTime.getText().length() > 0)
                filterList.add(FilterParam.newF("max_czas_lotu", ">=", strToFloat(this.droneFindFlightTime.getText()) ));
            if(this.droneFindSpeed.getText().length() > 0)
                filterList.add(FilterParam.newF("max_predkosc", ">=", strToFloat(this.droneFindSpeed.getText()) ));
            if(this.droneFindWeightFrom.getText().length() > 0)
                filterList.add(FilterParam.newF("masa", ">=", strToFloat(this.droneFindWeightFrom.getText()) ));
            if(this.droneFindWeightTo.getText().length() > 0)
                filterList.add(FilterParam.newF("masa", "<=", strToFloat(this.droneFindWeightTo.getText()) ));
            if(this.droneFindFree.isSelected())
                filterList.add(FilterParam.newF("stan", "=", 0));
            if(this.droneFindDronePoint.getValue() != null)
                filterList.add(FilterParam.newF("Punkt_Kontrolny_id", "=", ((this.droneFindDronePoint.getValue())).getId()));

            Task t = new Task() {
                protected List<DataModel> call() throws SQLException {
                    return Main.droneService.find(filterList);
                }
            };
            t.setOnFailed(event -> {
                Main.gui.showDialog("Error","Failed to find drone data",t.getException().getMessage(), Alert.AlertType.ERROR);
            });
            t.setOnSucceeded(event -> {
                tableList = (List<Dron>) t.getValue();
                this.updateTableView(tableList);
                clearForm(findForm);
            });
            new Thread(t).start();
        } catch (NullPointerException e){
            this.droneFindError.setText(e.getMessage());
        }
    }

    private void updateTableView(List<Dron> dataList){
        ObservableList<Dron> data = FXCollections.observableArrayList();
        for (Dron m: dataList) {
            data.add(m);
        }
        this.tableView.setItems(data);
    }

    private void getPoints(ChoiceBox<Punkt_kontrolny> box, boolean free){
        Task t = Main.gui.getDronePointsTask(free);
        t.setOnSucceeded(event -> {
            List<DataModel> resultList = (List<DataModel>) t.getValue();
            this.setSelectBoxValues(box, resultList);
        });
        new Thread(t).start();
    }

    private void setSelectBoxValues(ChoiceBox<Punkt_kontrolny> box, List<DataModel> dataList){
        ObservableList<Punkt_kontrolny> data = FXCollections.observableArrayList();
        for (DataModel m: dataList) {
            Punkt_kontrolny d = (Punkt_kontrolny) m;
            data.add(d);
        }
        box.setItems(data);
    }

    private Dron parseCreateForm(){
        Dron d = new Dron();
        d.setNazwa(emptyNullStr(this.droneCreateName.getText()));
        d.setMasa(strToFloat(this.droneCreateWeight.getText()));
        d.setIlosc_wirnikow(strToInteger(this.droneCreateRotors.getText()));
        d.setMax_predkosc(strToFloat(this.droneCreateSpeed.getText()));
        d.setMax_czas_lotu(strToFloat(this.droneCreateFlightTime.getText()));
        d.setOpis(emptyNullStr(this.droneCreateDesc.getText()));
        if(this.droneCreateDronePoint.getValue() != null)
            d.setPunkt_kontrolny_id(((this.droneCreateDronePoint.getValue())).getId());
        d.setPoziom_baterii(100f);
        d.setStan(0);
        return d;
    }

    private void creteAction(){
        this.droneCreateError.setText("");
        Dron d = this.parseCreateForm();
        Error valid = Main.droneService.validate(d);
        if(valid!=null){
            this.droneCreateError.setText(valid.getMessage());
            return;
        }
        Task t = new Task() {
            protected Error call() throws Exception {
                return Main.droneService.insert(d);
            }
        };
        t.setOnSucceeded(
                onSuccessSimpleError(t, "Successfully added new drone", "Failed to create new drone", new RunOnFinish() {
                    public void run() {
                        clearForm(createForm);
                    }
                }));
        t.setOnFailed(event -> {
            System.out.println();t.getException().getMessage();
        });
        new Thread(t).start();
    }

    private void setDroneEditForm(Dron d){
        selectedDrone=d;
        this.droneEditId.setText(String.valueOf(d.getId()));
        this.droneEditName.setText(d.getNazwa());
        this.droneEditBattery.setText(String.valueOf(d.getPoziom_baterii()));
        this.droneEditDesc.setText(d.getOpis());
        this.tabPane.getSelectionModel().select(tabEdit);
        this.droneDeleteAction.setDisable(false);
        this.droneEdiButton.setDisable(false);
    }

    private void clearEditForm(){
        selectedDrone=null;
        clearForm(this.editForm);
    }

    private void editAction(){
        this.droneEditError.setText("");
        Dron d = parseEditForm();
        Error valid = validateEditForm(d);
        if(valid!=null){
            this.droneEditError.setText(valid.getMessage());
            return;
        }
        Task t = new Task() {
            protected Error call() throws Exception { return Main.droneService.update(d); }
        };
        t.setOnSucceeded(
                onSuccessSimpleError(t,"Successfully updated drone info", "Failed to update drone info",emptyRunOnFinish())
        );
        new Thread(t).start();
    }

    private Dron parseEditForm(){
        Dron d = new Dron();
        d.setId(strToInteger(this.droneEditId.getText()));
        d.setNazwa(emptyNullStr(this.droneEditName.getText()));
        d.setOpis(emptyNullStr(this.droneEditDesc.getText()));
        d.setPoziom_baterii(strToFloat(this.droneEditBattery.getText()));
        return d;
    }

    private Error validateEditForm(Dron d){
        if(d.getId()==null) return new Error("Incorrect ID");
        if(d.getPoziom_baterii()==null) return new Error("Incorrect battery level");
        return null;
    }

    private void deleteAction(){
        Task t = new Task() {
            protected Error call() { return Main.droneService.delete(selectedDrone.getId(),selectedDrone.getPunkt_kontrolny_id()); }
        };
        t.setOnSucceeded(
                onSuccessSimpleError(t, "Successfully deleted drone", "Failed to delete drone", new RunOnFinish() {
                    public void run() {
                        tableList.remove(selectedDrone);
                        updateTableView(tableList);
                        clearEditForm();
                    }
                }));
        new Thread(t).start();
    }
}
