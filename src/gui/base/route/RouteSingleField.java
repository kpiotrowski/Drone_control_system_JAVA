package gui.base.route;

import dataModels.Punkt_kontrolny;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by no-one on 02.01.17.
 */
class RouteSingleField {

    @FXML @Getter private ChoiceBox<Punkt_kontrolny> selectPoint;
    @FXML @Getter private TextField x;
    @FXML @Getter private TextField y;
    @FXML @Getter private TextField z;
    @FXML @Getter private Button del;
    private TextField[] fields;

    void decNumber(){
        this.del.setId(String.valueOf(Integer.parseInt(this.del.getId())-1));
        this.recalculatePosition(Integer.parseInt(this.del.getId()));
    }

    RouteSingleField(int num, ObservableList<Punkt_kontrolny> dronePoints){
        this.selectPoint = new ChoiceBox<>();
        this.selectPoint.setItems(dronePoints);
        if(dronePoints.size()>0)this.selectPoint.setValue(dronePoints.get(0));

        this.x = new TextField();
        this.y = new TextField();
        this.z = new TextField();
        this.fields = new TextField[]{x,y,z};

        this.del = new Button("X");
        this.del.setId(String.valueOf(num));
        for (TextField field : fields) field.setText("0");
        this.z.setText("100");
        this.recalculatePosition(num);

        this.selectPoint.setOnAction(e->{
            if(this.selectPoint.getValue()==null || this.selectPoint.getValue().getId() == -1){
                for (TextField field : fields) {
                    field.setEditable(true);
                    field.setText("0");
                }
                this.z.setText("100");
            } else {
                for (TextField field : fields) field.setEditable(false);
                this.x.setText(String.valueOf(this.selectPoint.getValue().getWspx()));
                this.y.setText(String.valueOf(this.selectPoint.getValue().getWspy()));
                this.z.setText(String.valueOf(this.selectPoint.getValue().getWspz()));
            }
        });
    }
    private void recalculatePosition(int num){
        this.selectPoint.setLayoutX(10);
        this.selectPoint.setLayoutY(num*73+10);
        this.selectPoint.setPrefWidth(220);
        this.del.setPrefWidth(30);
        this.del.setLayoutX(255);
        this.del.setLayoutY(num*73+10);
        this.x.setLayoutX(10);
        this.y.setLayoutX(102);
        this.z.setLayoutX(194);
        for (TextField field : fields) {
            field.setLayoutY(num*73+42);
            field.setStyle("-fx-pref-width: 90px;");
        }
    }
}
