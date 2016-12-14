package gui.base.points;

import common.FilterParam;
import dataModels.DataModel;
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
 * Created by no-one on 20.11.16.
 */
public class PointsController {
    @FXML private TableColumn<Object, Object> columnNazwa;
    @FXML private TableColumn<Object, Object> columnDronesNow;
    @FXML private TableColumn<Object, Object> columnMaxDrones;
    @FXML private TableColumn<Object, Object> columnKoorZ;
    @FXML private TableColumn<Object, Object> columnKoorY;
    @FXML private TableColumn<Object, Object> columnKoorX;
    @FXML private TableColumn<Object, Object> columnId;
    @FXML private Button pointEditButtonDelete;
    @FXML private TableView<Punkt_kontrolny> tableView;
    @FXML private Tab findTab;
    @FXML private Tab editTab;
    @FXML private Tab createTab;
    @FXML private TabPane tabPane;

    @FXML private TextField pointFindName;
    @FXML private TextField pointFindKoorXFrom;
    @FXML private TextField pointFindKoorXTo;
    @FXML private TextField pointFindKoorYFrom;
    @FXML private TextField pointFindKoorYTo;
    @FXML private TextField pointFindKoorZFrom;
    @FXML private TextField pointFindKoorZTo;
    @FXML private TextField pointFindFreePlaces;
    @FXML private Button pointFindButton;
    @FXML private Label pointFindError;

    @FXML private TextField pointCreateMaxDrones;
    @FXML private TextField pointCreateKoorZ;
    @FXML private TextField pointCreateKoorY;
    @FXML private TextField pointCreateKoorX;
    @FXML private TextField pointCreateName;
    @FXML private Label pointCreateError;
    @FXML private Button pointCreateButton;

    @FXML private Label pointEditError;
    @FXML private Button pointEditButton;
    @FXML private TextField pointEditDronesNow;
    @FXML private TextField pointEditKoorX;
    @FXML private TextField pointEditName;
    @FXML private TextField pointEditID;
    @FXML private TextField pointEditKoorY;
    @FXML private TextField pointEditKoorZ;
    @FXML private TextField pointEditMaxDrones;

    public PointsController(){}

    @FXML
    private void initialize(){
        this.pointFindButton.setOnAction(event -> { this.findAction();});
        this.pointCreateButton.setOnAction(event -> {this.createAction();});
        this.pointEditButton.setOnAction(event -> {this.editAction();});
        this.pointEditButtonDelete.setOnAction(event -> {this.deleteAction();});
        this.columnNazwa.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        this.columnDronesNow.setCellValueFactory(new PropertyValueFactory<>("obecna_ilosc_dronow"));
        this.columnMaxDrones.setCellValueFactory(new PropertyValueFactory<>("max_ilosc_dronow"));
        this.columnKoorZ.setCellValueFactory(new PropertyValueFactory<>("wspz"));
        this.columnKoorY.setCellValueFactory(new PropertyValueFactory<>("wspy"));
        this.columnKoorX.setCellValueFactory(new PropertyValueFactory<>("wspx"));
        this.columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.tableView.setRowFactory( tv -> {
            TableRow<Punkt_kontrolny> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Punkt_kontrolny rowData = row.getItem();
                    this.setEditForm(rowData);
                }
            });
            return row ;
        });
    }

    private void setEditForm(Punkt_kontrolny p){
        this.pointEditDronesNow.setText(String.valueOf(p.getObecna_ilosc_dronow()));
        this.pointEditKoorX.setText(String.valueOf(p.getWspx()));
        this.pointEditName.setText(p.getNazwa());
        this.pointEditID.setText(String.valueOf(p.getId()));
        this.pointEditKoorY.setText(String.valueOf(p.getWspz()));
        this.pointEditKoorZ.setText(String.valueOf(p.getWspz()));
        this.pointEditMaxDrones.setText(String.valueOf(p.getMax_ilosc_dronow()));
        this.tabPane.getSelectionModel().select(editTab);
        this.pointEditButtonDelete.setDisable(false);
        this.pointEditButton.setDisable(false);
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

    private Punkt_kontrolny parseCreateForm(){
        Punkt_kontrolny p = new Punkt_kontrolny();
        p.setMax_ilosc_dronow(strToInteger(this.pointCreateMaxDrones.getText()));
        p.setObecna_ilosc_dronow(0);
        p.setNazwa(this.pointCreateName.getText());
        p.setWspx(strToFloat(this.pointCreateKoorX.getText()));
        p.setWspy(strToFloat(this.pointCreateKoorY.getText()));
        p.setWspz(strToFloat(this.pointCreateKoorZ.getText()));
        return p;
    }
    private void clearCreateForm(){
        this.pointCreateError.setText("");
        this.pointCreateKoorX.setText("");
        this.pointCreateKoorY.setText("");
        this.pointCreateKoorZ.setText("");
        this.pointCreateName.setText("");
        this.pointCreateMaxDrones.setText("");
    }

    private Punkt_kontrolny parseEditForm(){
        Punkt_kontrolny p = new Punkt_kontrolny();
        p.setNazwa(this.pointEditName.getText());
        p.setMax_ilosc_dronow(strToInteger(this.pointEditMaxDrones.getText()));
        p.setObecna_ilosc_dronow(strToInteger(this.pointEditDronesNow.getText()));
        p.setId(strToInteger(this.pointEditID.getText()));
        return p;
    }
    private Error validateEditForm(Punkt_kontrolny p){
        if(p.getId()==null) return new Error("ID nie może być puste");
        if(p.getMax_ilosc_dronow()==null) return new Error("Błędna maksymlana ilość dronów");
        if(p.getObecna_ilosc_dronow()==null) return new Error("Błędna obecna ilość dronów");
        if(p.getMax_ilosc_dronow()<p.getObecna_ilosc_dronow()) return new Error("Maksymalna ilość dronów nie może być mniejsza niż obecna");
        return null;
    }

    protected void deleteAction(){
        Integer id = strToInteger(this.pointEditID.getText());
        Task t = new Task() {
            protected Error call() { return Main.punktKontrolnyService.delete(id); }
        };
        t.setOnSucceeded(event -> {
            Error e = (Error) t.getValue();
            if(e!=null)
                Main.gui.showDialog("Błąd","Niepowodzenie usuwania punktu kontrolnego", e.toString(), Alert.AlertType.ERROR);
            else
                Main.gui.showDialog("Info", "Pomyślnie usunięto punkt kontrolny", "", Alert.AlertType.INFORMATION);
        });
        new Thread(t).start();
    }

    protected void findAction(){
        ArrayList<FilterParam> filterList = new ArrayList<>();
        this.pointFindError.setText("");
        try {
            if (this.pointFindName.getText().length() > 0)
                filterList.add(FilterParam.newF("nazwa", "LIKE", this.pointFindName.getText()));
            if (this.pointFindKoorXFrom.getText().length() > 0)
                filterList.add(FilterParam.newF("wspX", ">", strToFloat(this.pointFindKoorXFrom.getText())));
            if (this.pointFindKoorXTo.getText().length() > 0)
                filterList.add(FilterParam.newF("wspX", "<", strToFloat(this.pointFindKoorXTo.getText())));
            if (this.pointFindKoorYFrom.getText().length() > 0)
                filterList.add(FilterParam.newF("wspY", ">", strToFloat(this.pointFindKoorYFrom.getText())));
            if (this.pointFindKoorYTo.getText().length() > 0)
                filterList.add(FilterParam.newF("wspY", "<", strToFloat(this.pointFindKoorYTo.getText())));
            if (this.pointFindKoorZFrom.getText().length() > 0)
                filterList.add(FilterParam.newF("wspZ", ">", strToFloat(this.pointFindKoorZFrom.getText())));
            if (this.pointFindKoorZTo.getText().length() > 0)
                filterList.add(FilterParam.newF("wspZ", "<", strToFloat(this.pointFindKoorZTo.getText())));
            if (this.pointFindFreePlaces.getText().length() > 0)
                filterList.add(FilterParam.newF("max_ilosc_dronow-obecna_ilosc_dronow", ">", strToInteger(this.pointFindFreePlaces.getText())));

            Task t = new Task() {
                protected List<DataModel> call() throws SQLException {
                    return Main.punktKontrolnyService.find(filterList);
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
            this.pointFindError.setText(e.getMessage());
        }
    }

    protected void updateTableView(List<DataModel> dataList){
        ObservableList<Punkt_kontrolny> data = FXCollections.observableArrayList();
        for (DataModel m: dataList) {
            Punkt_kontrolny p = (Punkt_kontrolny)m;
            data.add(p);
        }
        this.tableView.setItems(data);
    }

    protected void createAction(){
        this.pointCreateError.setText("");
        Punkt_kontrolny p = this.parseCreateForm();
        Error valid = Main.punktKontrolnyService.validate(p);
        if(valid!=null){
            this.setPointError(this.pointCreateError, valid.toString());
            return;
        }
        Task t = new Task() {
            protected Error call() throws Exception {
                return Main.punktKontrolnyService.insert(p);
            }
        };
        t.setOnSucceeded(event -> {
            Error e = (Error) t.getValue();
            if(e != null)
                Main.gui.showDialog("Błąd","Niepowodzenie dodawania nowego punktu kontrolnego", e.toString(), Alert.AlertType.ERROR);
            else {
                this.clearCreateForm();
                Main.gui.showDialog("INfo", "Pomyślnie dodano nowy punkt kontrolny", "", Alert.AlertType.INFORMATION);
            }
        });
        new Thread(t).start();
    }

    protected void editAction(){
        this.pointEditError.setText("");
        Punkt_kontrolny p = this.parseEditForm();
        Error valid = this.validateEditForm(p);
        if(valid!=null){
            this.setPointError(this.pointEditError, valid.toString());
            return;
        }

        Task t = new Task() {
            protected Error call() throws Exception {
                return Main.punktKontrolnyService.update(p);
            }
        };
        t.setOnSucceeded(event -> {
            Error e = (Error) t.getValue();
            if(e != null)
                Main.gui.showDialog("Błąd","Niepowodzenie aktualizacji punktu kontrolnego", e.toString(), Alert.AlertType.ERROR);
            else
                Main.gui.showDialog("Info","Pomyśłnie zaktualizowano punkt kontrolny","", Alert.AlertType.INFORMATION);
        });
        new Thread(t).start();
    }

    private void setPointError(Label l, String s) {l.setText(s);}

}
