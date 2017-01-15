package gui.base.route;

import dataModels.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import main.Main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.ErrorManager;

import static common.CommonFunc.emptyNullStr;
import static common.CommonFunc.strToFloat;
import static java.lang.Math.abs;

/**
 * Created by no-one on 24.12.16.
 */
public class RouteController {

    private static final int routeMargin = 120;

    @FXML private Button routeDeleteButton;
    @FXML private Label newRouteError;
    @FXML private TextField newRouteName;
    @FXML private Button newRouteRefresh;
    @FXML private Button newRoutePoint;
    @FXML private Button newRouteCreate;
    @FXML private AnchorPane newRouteFieldsBox;
    @FXML private ListView<Trasa> listView;
    @FXML private Tab newTab;
    @FXML private Tab listTab;
    @FXML private Pane routeGraph;

    private ArrayList<RouteSingleField> newRouteFields = new ArrayList<>();
    private ObservableList<Punkt_kontrolny> dronePoints = FXCollections.observableArrayList();
    private Trasa selectedRoute;

    public RouteController(){}

    @FXML
    private void initialize() {
        this.listTab.setOnSelectionChanged(event -> {
            if(this.listTab.isSelected())this.findRoutes();});
        this.listView.setCellFactory( tv -> {
            ListCell<Trasa> cell = new ListCell<Trasa>(){
                @Override
                protected void updateItem(Trasa t, boolean bln) {
                    super.updateItem(t, bln);
                    if (t != null) {
                        setText(t.toString());
                    }
                }
            };
            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! cell.isEmpty()) ) {
                    Trasa rowData = cell.getItem();
                    this.getDrawRoute(rowData);
                }
            });
            return cell ;
        });
        this.newRouteRefresh.setOnAction(e->{refreshNewRouteView();});
        this.newRoutePoint.setOnAction(e->{addNewRoutePoint();});
        this.newRouteCreate.setOnAction(e->{finishCreatingRoute();});
        this.routeDeleteButton.setOnAction(e->{this.deleteRoute();});
    }
    private void deleteRoute(){
        Task t = new Task(){
            protected Error call() {
                return Main.trasaService.delete(selectedRoute.getNazwa(), selectedRoute.getUzytkownik_id());
            }
        };
        t.setOnSucceeded(event -> {
            Error e = (Error) t.getValue();
            if(e != null)
                Main.gui.showDialog("Error","Failed to delete route", e.getMessage(), Alert.AlertType.ERROR);
            else {
                routeGraph.getChildren().clear();
                findRoutes();
                Main.gui.showDialog("Info", "Successfully deleted route", "", Alert.AlertType.INFORMATION);
            }
        });
        new Thread(t).start();
    };

    private void getDrawRoute(Trasa tr){
        Task t = new Task(){
            protected List<DataModel> call() throws SQLException {
                return Main.punktNaTrasieService.findPointsForGivenRoute(tr);
            }
        };
        t.setOnFailed(event -> {
            Main.gui.showDialog("Error","Failed to get route info",t.getException().getMessage(), Alert.AlertType.ERROR);
        });
        t.setOnSucceeded(event -> {
            ArrayList<Punkt_na_trasie> tPoints = (ArrayList<Punkt_na_trasie>)t.getValue();
            selectedRoute = tr;
            drawRoute(tPoints, true);
        });
        new Thread(t).start();
    }

    private void drawRoute(List<Punkt_na_trasie> dataList, boolean showDeleteBut) {
        routeGraph.getChildren().clear();
        if(dataList.size()<2)return;
        if(showDeleteBut){
            this.routeDeleteButton.setVisible(true);
            routeGraph.getChildren().add(this.routeDeleteButton);
        } else selectedRoute=null;


        Comparator<Punkt_na_trasie> comparator = (o1, o2) -> {
            if (o1.getNumer_kolejny() <= o2.getNumer_kolejny()) return -1;
            if (o1.getNumer_kolejny() > o2.getNumer_kolejny()) return 1;
            return 0;
        };
        dataList.sort(comparator);
        Float[] routeArea = getRouteArea(dataList);
        for (int i=0; i< dataList.size() ; i++) {
            Punkt_na_trasie pnt = dataList.get(i);
            Circle point = new Circle();
            double pointX = routeMargin/2+(pnt.getWspX()-routeArea[0])*((routeGraph.getWidth()-routeMargin)/(routeArea[1]-routeArea[0]));
            double pointY = abs(routeGraph.getHeight()-routeMargin/2-(pnt.getWspY()-routeArea[2])*((routeGraph.getHeight()-routeMargin)/(routeArea[3]-routeArea[2])));
            point.setCenterX(pointX);
            point.setCenterY(pointY);

            point.setRadius(12);
            Tooltip t = new Tooltip(pnt.getNumer_kolejny()+":   X:"+pnt.getWspX()+" Y:"+pnt.getWspY()+" Z:"+pnt.getWspZ());
            Tooltip.install(point, t);
            if (pnt.getPunkt_kontrolny_id()!=null) point.setFill(Paint.valueOf("#6ca850"));
            else point.setFill(Paint.valueOf("#1d1e24"));
            if(i<dataList.size()-1){
                Punkt_na_trasie ii = dataList.get(i+1);
                Line l = new Line();
                double point2X = routeMargin/2+(ii.getWspX()-routeArea[0])*((routeGraph.getWidth()-routeMargin)/(routeArea[1]-routeArea[0]));
                double point2Y = abs(routeGraph.getHeight()-routeMargin/2-(ii.getWspY()-routeArea[2])*((routeGraph.getHeight()-routeMargin)/(routeArea[3]-routeArea[2])));
                l.setStartX(pointX);
                l.setStartY(pointY);
                l.setEndX(point2X);
                l.setEndY(point2Y);
                l.setStrokeWidth(3);
                l.setStroke(Paint.valueOf("#447eb0"));
                routeGraph.getChildren().add(l);
            }
            routeGraph.getChildren().add(point);
        }
    }
    private Float[] getRouteArea(List<Punkt_na_trasie> dataList){
        Float[] result = new Float[]{999f,-999f,999f,-999f};
        for (Punkt_na_trasie p: dataList) {
            if(p.getWspX()<result[0]) result[0]=p.getWspX();
            if(p.getWspX()>result[1]) result[1]=p.getWspX();
            if(p.getWspY()<result[2]) result[2]=p.getWspY();
            if(p.getWspY()>result[3]) result[3]=p.getWspY();
        }
        if(Objects.equals(result[0], result[1])){
            result[0]-=15;
            result[1]+=15;
        }
        if(Objects.equals(result[2], result[3])){
            result[2]-=15;
            result[3]+=15;
        }
        return result;
    }

    public void refreshPermissions(Uzytkownik u){
        clearNewRouteForm();
        findRoutes();
    }

    private void findRoutes(){
        Task t = Main.gui.getRoutesTask(Main.authenticatedUser.getId());
        t.setOnFailed(event -> {
            Main.gui.showDialog("Error","Failed to get data",t.getException().getMessage(), Alert.AlertType.ERROR);
        });
        t.setOnSucceeded(event -> {
            List<Trasa> resultList = (List<Trasa>) t.getValue();
            this.updateListView(resultList);
        });
        new Thread(t).start();
    }
    private void updateListView(List<Trasa> dataList){
        ObservableList<Trasa> data = FXCollections.observableArrayList();
        for (Trasa m: dataList) data.add(m);
        this.listView.setItems(data);
        this.listView.refresh();
    }

    private void refreshNewRouteView(){
        List<Punkt_na_trasie> list = createRouteFromFields();
        this.drawRoute(list, false);
    }
    private void addNewRoutePoint(){
        RouteSingleField newField = new RouteSingleField(this.newRouteFields.size(), dronePoints);
        this.newRouteFields.add(newField);
        addNewFieldToRouteBox(newField);
        newField.getDel().setOnAction(e->{
            int index = Integer.parseInt(((Button)e.getSource()).getId());
            for(int i = index;i<this.newRouteFields.size();i++)
                this.newRouteFields.get(i).decNumber();
            this.newRouteFields.remove(index);
            redrawFieldRouteBox();
            if(this.newRouteFields.size()==0)
                this.clearNewRouteForm();
        });
    }
    private void finishCreatingRoute(){
        this.newRouteError.setText("");
        List<Punkt_na_trasie> list = createRouteFromFields();
        Trasa t = new Trasa();
        t.setUzytkownik_id(Main.authenticatedUser.getId());
        t.setNazwa(emptyNullStr(this.newRouteName.getText()));
        Error err = Main.trasaService.validate(t);
        if(err!= null){
            this.newRouteError.setText(err.getMessage());
            return;
        }
        if(list.size()<2){
            this.newRouteError.setText("Route must have at least 2 points");
            return;
        }
        for (Punkt_na_trasie punkt: list) {
            err = Main.punktNaTrasieService.validate(punkt);
            if(err!=null){
                this.newRouteError.setText(err.getMessage());
                return;
            }
        }

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                return Main.trasaService.insert(t,list);
            }
        };
        task.setOnSucceeded(event -> {
            Error e = (Error) task.getValue();
            if(e != null)
                Main.gui.showDialog("Error","Failed to create route", e.getMessage(), Alert.AlertType.ERROR);
            else {
                clearNewRouteForm();
                Main.gui.showDialog("Info", "Successfully created route", "", Alert.AlertType.INFORMATION);
            }
        });
        new Thread(task).start();
    }
    private void clearNewRouteForm(){
        this.newRouteName.setText("");
        this.newRouteFieldsBox.getChildren().clear();
        this.newRouteFields.clear();
        getAndSetDronePoints();
        addNewRoutePoint();
        refreshNewRouteView();
    }
    private void addNewFieldToRouteBox(RouteSingleField newField){
        this.newRouteFieldsBox.getChildren().add(newField.getSelectPoint());
        this.newRouteFieldsBox.getChildren().add(newField.getDel());
        this.newRouteFieldsBox.getChildren().add(newField.getX());
        this.newRouteFieldsBox.getChildren().add(newField.getY());
        this.newRouteFieldsBox.getChildren().add(newField.getZ());
        this.newRouteFieldsBox.setMinHeight(newField.getZ().getLayoutY()+50);
    }
    private void redrawFieldRouteBox(){
        this.newRouteFieldsBox.getChildren().clear();
        this.newRouteFields.forEach(this::addNewFieldToRouteBox);
    }
    private void getAndSetDronePoints(){
        this.dronePoints.clear();
        Task t = Main.gui.getDronePointsTask(false);
        t.setOnSucceeded(event -> {
            List<Punkt_kontrolny> resultList = (List<Punkt_kontrolny>) t.getValue();
            Punkt_kontrolny creteNewPoint = new Punkt_kontrolny(){
                public String toString(){
                    return this.getNazwa();
                }
            };
            creteNewPoint.setId(-1);
            creteNewPoint.setNazwa("-- your own point --");
            this.dronePoints.add(creteNewPoint);
            for (Punkt_kontrolny p: resultList)
                this.dronePoints.add(p);
            for (RouteSingleField f: this.newRouteFields) {
                f.getSelectPoint().setItems(this.dronePoints);
                if(this.dronePoints.size()>0)f.getSelectPoint().setValue(this.dronePoints.get(0));
            }

        });
        new Thread(t).start();
    }

    private List<Punkt_na_trasie> createRouteFromFields(){
        ArrayList<Punkt_na_trasie> list = new ArrayList<>();
        for (RouteSingleField field:this.newRouteFields) {
            Punkt_na_trasie p = new Punkt_na_trasie();
            p.setNumer_kolejny(Integer.valueOf(field.getDel().getId())+1);
            p.setTrasa_nazwa(this.newRouteName.getText());
            p.setTrasa_uzytkownik_id(Main.authenticatedUser.getId());
            if(field.getSelectPoint().getValue()==null || field.getSelectPoint().getValue().getId()==-1){
                p.setPolozenie_id(-1);
                p.setWspX(strToFloat(field.getX().getText()));
                p.setWspY(strToFloat(field.getY().getText()));
                p.setWspZ(strToFloat(field.getZ().getText()));
            } else {
                p.setPunkt_kontrolny_id(field.getSelectPoint().getValue().getId());
                p.setWspX(field.getSelectPoint().getValue().getWspx());
                p.setWspY(field.getSelectPoint().getValue().getWspy());
                p.setWspZ(field.getSelectPoint().getValue().getWspz());
            }
            list.add(p);
        }
        return list;
    }

}
