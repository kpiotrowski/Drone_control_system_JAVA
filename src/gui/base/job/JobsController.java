package gui.base.job;

import common.FilterParam;
import dataModels.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;
import main.Main;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static common.CommonFunc.parseDateTimeToSQL;
import static common.CommonFunc.strToInteger;
import static dataModels.Zadanie.statusStr;
import static dataModels.Zadanie.typesStr;

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
    public Label jobFindError;

    public Button jobFindButton;

    public TextField jobFindUser;
    public ChoiceBox<Pair> jobFindType;
    public TextField jobFindStartDate;
    public ChoiceBox<Trasa> jobFindRoute;
    public ChoiceBox<Punkt_kontrolny> jobFindFinishPoint;
    public TextField jobFindDrone;
    public Label jobFindUserLabel;
    public ChoiceBox<Pair> jobFindStatus;
    public TabPane tabPane;
    public Tab findTab;


    public JobsController(){}

    @FXML
    private void initialize() {
        this.findTab.setOnSelectionChanged(event -> {
            if(this.findTab.isSelected())this.reloadData();});
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
        this.jobFindButton.setOnAction(event -> {this.find();});

        ObservableList<Pair> data = FXCollections.observableArrayList();
        for(int i=0;i<typesStr.length;i++){
            Pair<Integer,String> p = new Pair<Integer, String>(i,typesStr[i]){
                @Override
                public String toString() {
                    return getValue();
                }
            };
            data.add(p);
        }
        jobFindType.setItems(data);

        ObservableList<Pair> data2 = FXCollections.observableArrayList();
        for(int i=0;i< statusStr.length;i++){
            Pair<Integer,String> p = new Pair<Integer, String>(i,statusStr[i]){
                @Override
                public String toString() {
                    return getValue();
                }
            };
            data2.add(p);
        }
        jobFindStatus.setItems(data2);
    }

    public void refreshPermissions(Uzytkownik uz){
        if (uz.getPoziom_uprawnien() == 0){
            this.jobFindUserLabel.setVisible(false);
            this.jobFindUser.setVisible(false);
        } else {
            this.jobFindUserLabel.setVisible(true);
            this.jobFindUser.setVisible(true);
        }
        reloadData();
    }

    private void reloadData(){
        Task t = Main.gui.getDronePointsTask(false);
        t.setOnSucceeded(event -> {
            List<Punkt_kontrolny> resultList = (List<Punkt_kontrolny>) t.getValue();
            setPointValues(jobFindFinishPoint, resultList);
        });
        new Thread(t).start();

        Task tt;
        if(Main.authenticatedUser.getPoziom_uprawnien()==0)
            tt = Main.gui.getRoutesTask(Main.authenticatedUser.getId());
        else tt = Main.gui.getRoutesTask(null);
        tt.setOnSucceeded(event -> {
            List<Trasa> resultList2 = (List<Trasa>) tt.getValue();
            setRouteValues(jobFindRoute, resultList2);
        });
        new Thread(tt).start();


        //TODO IMPLEMENT THIS
    }

    private void setInfoForm(Zadanie z){
        //TODO IMPLEMENT THIS
    }

    private void clearFindForm(){
        this.jobFindError.setText("");
        jobFindUser.setText("");
        jobFindType.setValue(null);
        jobFindStartDate.setText("");
        jobFindRoute.setValue(null);
        jobFindFinishPoint.setValue(null);
        jobFindDrone.setText("");
        jobFindStatus.setValue(null);
    }
    private void find(){
        this.jobFindError.setText("");
        ArrayList<FilterParam> filterList = new ArrayList<>();
        try {
            if(jobFindUser.getText().length()>0)
                filterList.add(FilterParam.newF("Uzytkownik_id","=",strToInteger(jobFindUser.getText())));
            if(jobFindType.getValue()!=null)
                filterList.add(FilterParam.newF("typ","=",jobFindType.getValue().getKey()));
            if(jobFindStartDate.getText().length()>0)
                filterList.add(FilterParam.newF("data_rozpoczecia",">",parseDateTimeToSQL(jobFindStartDate.getText())));
            if(jobFindRoute.getValue()!=null){
                filterList.add(FilterParam.newF("Trasa_nazwa","LIKE",jobFindRoute.getValue().getNazwa()));
                filterList.add(FilterParam.newF("Trasa_Uzytkownik_id","=",jobFindRoute.getValue().getUzytkownik_id()));
            }
            if(jobFindFinishPoint.getValue()!=null)
                filterList.add(FilterParam.newF("Punkt_koncowy_id","=",jobFindFinishPoint.getValue().getId()));
            if(jobFindDrone.getText().length()>0)
                filterList.add(FilterParam.newF("Dron_id","=",strToInteger(jobFindDrone.getText())));
            if(jobFindStatus.getValue()!=null)
                filterList.add(FilterParam.newF("stan","=",jobFindStatus.getValue().getKey()));

            Task t = new Task() {
                protected List<DataModel> call() throws SQLException {
                    return Main.zadanieService.find(filterList);
                }
            };
            t.setOnFailed(event -> {
                Main.gui.showDialog("Błąd","Błąd podczas wyszukiwania danych",t.getException().getMessage(), Alert.AlertType.ERROR);
            });
            t.setOnSucceeded(event -> {
                List<Zadanie> resultList = (List<Zadanie>) t.getValue();
                this.updateTableView(resultList);
                clearFindForm();
            });
            new Thread(t).start();
        } catch (ParseException e) {
            this.jobFindError.setText(e.getMessage());
        }
    }

    private void updateTableView(List<Zadanie> dataList){
        ObservableList<Zadanie> data = FXCollections.observableArrayList();
        for (Zadanie m: dataList) data.add(m);
        this.tableView.setItems(data);
    }

    private void setPointValues(ChoiceBox<Punkt_kontrolny> box, List<Punkt_kontrolny> dataList){
        ObservableList<Punkt_kontrolny> dataP = FXCollections.observableArrayList();
        for (Punkt_kontrolny m: dataList) dataP.add(m);
        box.setItems(dataP);
    }
    private void setRouteValues(ChoiceBox<Trasa> box, List<Trasa> dataListT){
        ObservableList<Trasa> dataT = FXCollections.observableArrayList();
        for (Trasa m: dataListT) dataT.add(m);
        box.setItems(dataT);
    }

}
