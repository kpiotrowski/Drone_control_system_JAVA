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

import static common.CommonFunc.emptyNullStr;
import static common.CommonFunc.strToFloat;
import static common.CommonFunc.strToInteger;

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
    @FXML private ChoiceBox droneCreateDronePoint;

    @FXML private TextField droneFindSpeed;
    @FXML private ChoiceBox droneFindDronePoint;
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
    @FXML private TableColumn tabDronePointId;
    @FXML private TableColumn tabDroneKoorZ;
    @FXML private TableColumn tabDroneKoorY;
    @FXML private TableColumn tabDroneKoorX;
    @FXML private TableColumn tabDroneState;
    @FXML private TableColumn tabDroneBattery;
    @FXML private TableColumn tabDroneFlightTime;
    @FXML private TableColumn tabDroneSpeed;
    @FXML private TableColumn tabDroneRotors;
    @FXML private TableColumn tabDroneWeight;
    @FXML private TableColumn tabDroneName;
    @FXML private TableColumn tabDroneId;
    @FXML private TableView tableView;
    @FXML private Button droneFindButton;
    @FXML private Button droneCreateButton;
    @FXML private Button droneEdiButton;
    @FXML private Button droneDeleteAction;
    @FXML private Tab tabEdit;
    @FXML private TabPane tabPane;
    @FXML private Tab createTab;

    private List<DataModel> foundDrones;
    private List<DataModel> freePoints;

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
        this.tabDroneState.setCellValueFactory(new PropertyValueFactory<>("stan"));
        this.tabDroneBattery.setCellValueFactory(new PropertyValueFactory<>("poziom_baterii"));
        this.tabDroneFlightTime.setCellValueFactory(new PropertyValueFactory<>("max_czas_lotu"));
        this.tabDroneSpeed.setCellValueFactory(new PropertyValueFactory<>("max_predkosc"));
        this.tabDroneRotors.setCellValueFactory(new PropertyValueFactory<>("ilosc_wirnikow"));
        this.tabDroneWeight.setCellValueFactory(new PropertyValueFactory<>("masa"));
        this.tabDroneName.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        this.tabDroneId.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    public void afterLogin(){
        this.getPoints(this.droneFindDronePoint, false);
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

    private void getPoints(ChoiceBox box, boolean free){
        ArrayList<FilterParam> filterList = new ArrayList<>();
        if(free==true) filterList.add(FilterParam.newF("max_ilosc_dronow-obecna_ilosc_dronow", ">", 0));
        Task t = new Task() {
            protected List<DataModel> call() throws SQLException {
                return Main.punktKontrolnyService.find(filterList);
            }
        };
        t.setOnSucceeded(event -> {
            List<DataModel> resultList = (List<DataModel>) t.getValue();
            this.freePoints = resultList;
            this.setSelectBoxValues(box, resultList);
        });
        new Thread(t).start();
    }

    private void setSelectBoxValues(ChoiceBox box, List<DataModel> dataList){
        ObservableList<Punkt_kontrolny> data = FXCollections.observableArrayList();
        for (DataModel m: dataList) {
            Punkt_kontrolny d = (Punkt_kontrolny) m;
            data.add(d);
        }
        box.setItems(data);
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
                filterList.add(FilterParam.newF("Punkt_Kontrolny_id", "=", ((Punkt_kontrolny)(this.droneFindDronePoint.getValue())).getId()));

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

    private void deleteAction(){
        Integer id = strToInteger(this.droneEditId.getText());
        Task t = new Task() {
            protected Error call() { return Main.droneService.delete(id); }
        };
        t.setOnSucceeded(event -> {
            Error e = (Error) t.getValue();
            if(e!=null)  Main.gui.showDialog("Błąd","Niepowodzenie usuwania drona", e.toString(), Alert.AlertType.ERROR);
            else Main.gui.showDialog("Info", "Pomyślnie usunięto drona", "", Alert.AlertType.INFORMATION);
        });
        new Thread(t).start();
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
            d.setPunkt_kontrolny_id(((Punkt_kontrolny)(this.droneCreateDronePoint.getValue())).getId());
        d.setPoziom_baterii(100f);
        d.setStan(0);
        return d;
    }
    private void clearCreateForm(){
        this.droneCreateName.setText("");
        this.droneCreateWeight.setText("");
        this.droneCreateRotors.setText("");
        this.droneCreateSpeed.setText("");
        this.droneCreateFlightTime.setText("");
        this.droneCreateDesc.setText("");
        this.droneCreateDronePoint.setValue(null);
    }

    private void creteAction(){
        this.droneCreateError.setText("");
        Dron d = this.parseCreateForm();
        Error valid = Main.droneService.validate(d);
        if(valid!=null){
            this.droneCreateError.setText(valid.toString());
            return;
        }
        Task t = new Task() {
            protected Error call() throws Exception {
                return Main.droneService.insert(d);
            }
        };
        t.setOnSucceeded(event -> {
            Error e = (Error) t.getValue();
            if(e != null)
                Main.gui.showDialog("Błąd","Niepowodzenie dodawania nowego drona", e.toString(), Alert.AlertType.ERROR);
            else {
                this.clearCreateForm();
                Main.gui.showDialog("INfo", "Pomyślnie dodano nowego drona", "", Alert.AlertType.INFORMATION);
            }
        });
        t.setOnFailed(event -> {
            System.out.println();t.getException().toString();
        });
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
        if(d.getId()==null) return new Error("Nieprawidłowe pole ID");
        if(d.getPoziom_baterii()==null) return new Error("Nieprawodłowy poziom baterii");
        return null;
    }


}
