package gui.base.job;

import common.FilterParam;
import common.RunOnFinish;
import dataModels.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import main.Main;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static common.CommonFunc.*;
import static common.CommonTask.emptyRunOnFinish;
import static common.CommonTask.onSuccessSimpleError;
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
    public Tab infoTab;
    
    public TextField jobInfoId;
    public TextField jobInfoStartDate;

    public Button jobInfoUpdate;
    public Button jobInfoDelete;
    public Button jobInfoDownloadResults;
    public Tab createTab;
    public Label jobCreateError;
    public Button jobCreateButton;
    public ChoiceBox<Pair<Integer, String>> jobCreateType;
    public TextField jobCreateStartDate;
    public ChoiceBox<Trasa> jobCreateRoute;
    public TextField jobCreateDrone;
    public ChoiceBox<Punkt_kontrolny> jobCreateFinishPoint;
    public Label jobInfoError;
    public Color x1;

    private Zadanie selectedJob;

    private Control[] createForm;
    private Control[] infoForm;
    private Control[] findForm;
    private List<Zadanie> tableList;

    public JobsController(){}

    @FXML
    private void initialize() {
        this.jobCreateButton.setOnAction(event -> this.create());
        this.jobInfoUpdate.setOnAction(event -> this.update());
        this.jobInfoDelete.setOnAction(event -> this.delete());
        this.jobInfoDownloadResults.setOnAction(event -> this.downloadResults());
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
        this.columnUserId.setCellValueFactory(new PropertyValueFactory<>("uzytkownik_id"));
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
        this.jobCreateType.setOnAction(event -> {
            if(this.jobCreateType.getValue()!=null) {
                if (this.jobCreateType.getValue().getKey() == Zadanie.TYPE_MOVE_TO_POINT) {
                    this.jobCreateDrone.setDisable(false);
                    this.jobCreateFinishPoint.setDisable(false);
                    this.jobCreateRoute.setDisable(true);
                    this.jobCreateRoute.setValue(null);
                } else {
                    this.jobCreateDrone.setDisable(true);
                    this.jobCreateFinishPoint.setDisable(true);
                    this.jobCreateDrone.setText("");
                    this.jobCreateFinishPoint.setValue(null);
                    this.jobCreateRoute.setDisable(false);
                }
            }
        });
        this.createForm = new Control[]{   jobCreateError,jobCreateType,jobCreateStartDate,jobCreateRoute,
                jobCreateDrone,jobCreateFinishPoint};
        this.infoForm = new Control[]{this.jobInfoDownloadResults,this.jobInfoId,this.jobInfoDelete,this.jobInfoUpdate,
                this.jobInfoStartDate
        };
        this.findForm = new Control[]{jobFindError,jobFindUser,jobFindType,jobFindStartDate,jobFindRoute,
                jobFindFinishPoint,jobFindDrone,jobFindStatus
        };
    }

    public void refreshPermissions(Uzytkownik uz){
        Integer maxjobType=Zadanie.TYPE_MOVE_TO_POINT;
        if (uz.getPoziom_uprawnien() == 0){
            this.jobFindUserLabel.setVisible(false);
            this.jobFindUser.setVisible(false);
            maxjobType--;
        } else {
            this.jobFindUserLabel.setVisible(true);
            this.jobFindUser.setVisible(true);
        }
        ObservableList<Pair<Integer,String>> data = FXCollections.observableArrayList();
        for(int i=0;i<typesStr.length && i<=maxjobType;i++){
            Pair<Integer,String> p = new Pair<Integer, String>(i,typesStr[i]){
                public String toString() {return getValue();}
            };
            data.add(p);
        }
        jobCreateType.setItems(data);
        reloadData();
    }

    private Zadanie parseCreateForm(){
        Zadanie z = new Zadanie();
        if(jobCreateType.getValue()!=null) z.setTyp(jobCreateType.getValue().getKey());
        try {
            z.setData_rozpoczenia(parseDateTimeToSQL(jobCreateStartDate.getText()));
        } catch (ParseException e) {
            this.jobCreateError.setText("Incorrect datetime format. Correct: dd-MM-yyyy HH:mm");
            return null;
        }
        if(jobCreateRoute.getValue()!=null){
            z.setTrasa_uzytkownik_id(jobCreateRoute.getValue().getUzytkownik_id());
            z.setTrasa_nazwa(jobCreateRoute.getValue().getNazwa());
        }
        z.setDron_id(strToInteger(jobCreateDrone.getText()));
        z.setStan(Zadanie.STATUS_NOWE_ZADANIE);
        z.setUzytkownik_id(Main.authenticatedUser.getId());
        if(jobCreateFinishPoint.getValue()!=null) z.setPunkt_koncowy_id(jobCreateFinishPoint.getValue().getId());
        return z;
    }

    private void create(){
        Zadanie z = parseCreateForm();
        if(z==null) return;
        Error err = Main.zadanieService.validate(z);
        if(err != null) {
            this.jobCreateError.setText(err.getMessage());
            return;
        }
        Task t = new Task() {
            @Override
            protected Object call() throws Exception {
                return Main.zadanieService.insert(z);
            }
        };
        t.setOnSucceeded(
                onSuccessSimpleError(t, "Successfully created job.", "Failed to create job", new RunOnFinish() {
                    public void run() {clearForm(createForm);}
                }));
        new Thread(t).start();
    }

    private void update(){
        this.jobInfoError.setText("");
        Zadanie zz = new Zadanie();
        zz.setId(this.selectedJob.getId());
        String datetime = this.jobInfoStartDate.getText();
        if(datetime==null){
            this.jobFindError.setText("Start date cannot be empty");
            return;
        }
        try {
            zz.setData_rozpoczenia(parseDateTimeToSQL(datetime));
            Task t = new Task() {
                protected Object call() {
                    return Main.zadanieService.update(zz);
                }
            };
            t.setOnSucceeded(
                    onSuccessSimpleError(t,"Successfully updated job info","Failed to update drone info",emptyRunOnFinish())
            );
            new Thread(t).start();
        } catch (ParseException e) {
            this.jobInfoError.setText("Incorrect datetime format");
        }

    }
    private void delete(){
        Integer id = this.selectedJob.getId();
        Integer droneId = this.selectedJob.getDron_id();
        Task t = new Task() {
            protected Error call() { return Main.zadanieService.delete(id,droneId); }
        };
        t.setOnSucceeded(
                onSuccessSimpleError(t, "Successfully deleted job", "Failed to delete job", new RunOnFinish() {
                    public void run() {
                        tableList.remove(selectedJob);
                        updateTableView(tableList);
                        clearInfoForm();
                    }
                }));
        new Thread(t).start();
    }

    private void downloadResults(){
        Integer id = this.selectedJob.getId();
        Task t = new Task() {
            @Override
            protected dataModels.File call() throws Exception {
                return Main.zadanieService.downloadFile(id);
            }
        };
        t.setOnFailed(event -> {
            Main.gui.showDialog("RESULT","Failed to download results.","", Alert.AlertType.ERROR);
        });
        t.setOnSucceeded(event -> {
            dataModels.File data = (dataModels.File) t.getValue();
            data.setData(Base64.getDecoder().decode(data.getData()));
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save file");
            fileChooser.setInitialFileName(data.getName()+"."+data.getType());
            File file = fileChooser.showSaveDialog(Main.gui.getStage());
            if (file != null) {
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(data.getData());
                } catch (IOException ex) {
                    Main.gui.showDialog("RESULT","Failed save file.",ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
        new Thread(t).start();
    }

    private void reloadData(){
        Task t = Main.gui.getDronePointsTask(false);
        t.setOnSucceeded(event -> {
            List<Punkt_kontrolny> resultList = (List<Punkt_kontrolny>) t.getValue();
            setPointValues(jobFindFinishPoint, resultList);
            setPointValues(jobCreateFinishPoint, resultList);
        });
        new Thread(t).start();

        Task tt;
        if(Main.authenticatedUser.getPoziom_uprawnien()==0)
            tt = Main.gui.getRoutesTask(Main.authenticatedUser.getId());
        else tt = Main.gui.getRoutesTask(null);
        tt.setOnSucceeded(event -> {
            List<Trasa> resultList2 = (List<Trasa>) tt.getValue();
            setRouteValues(jobFindRoute, resultList2);
            setRouteValues(jobCreateRoute,resultList2);
        });
        new Thread(tt).start();
    }

    private void setInfoForm(Zadanie z){
        this.jobInfoId.setText(String.valueOf(z.getId()));
        this.jobInfoStartDate.setText(sqlTimestampToString(z.getData_rozpoczenia()));
        if(z.getStan()<= Zadanie.STATUS_NOWE_ZADANIE) this.jobInfoUpdate.setDisable(false);
        else this.jobInfoUpdate.setDisable(true);
        if(z.getStan()<= Zadanie.STATUS_PRZYDZIELONO_DRONA) this.jobInfoDelete.setDisable(false);
        else this.jobInfoDelete.setDisable(true);
        if(z.getStan()==Zadanie.STATUS_ZAKONCZONE) this.jobInfoDownloadResults.setDisable(false);
        else this.jobInfoDownloadResults.setDisable(true);
        this.selectedJob=z;
        this.tabPane.getSelectionModel().select(this.infoTab);
    }

    private void clearInfoForm(){
        clearForm(this.infoForm);
        this.selectedJob=null;
    }

    private void find(){
        this.jobFindError.setText("");
        ArrayList<FilterParam> filterList = new ArrayList<>();
        try {
            if (Main.authenticatedUser.getPoziom_uprawnien()==0)
                filterList.add(FilterParam.newF("Uzytkownik_id","=",Main.authenticatedUser.getId()));
            else if(jobFindUser.getText().length()>0)
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
                Main.gui.showDialog("Error","Failed to get jobs",t.getException().getMessage(), Alert.AlertType.ERROR);
            });
            t.setOnSucceeded(event -> {
                tableList = (List<Zadanie>) t.getValue();
                this.updateTableView(tableList);
                clearForm(this.findForm);
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
