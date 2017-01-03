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
public class RouteSingleField {

    @FXML @Getter private ChoiceBox<Punkt_kontrolny> selectPoint;
    @FXML @Getter private TextField x;
    @FXML @Getter private TextField y;
    @FXML @Getter private TextField z;
    @FXML @Getter private Button del;

    public void decNumber(){
        this.del.setId(String.valueOf(Integer.parseInt(this.del.getId())-1));
        this.recalculatePosition(Integer.parseInt(this.del.getId()));
    }

    public RouteSingleField(int num, ObservableList<Punkt_kontrolny> dronePoints){
        this.selectPoint = new ChoiceBox<>();
        this.selectPoint.setItems(dronePoints);
        if(dronePoints.size()>0)this.selectPoint.setValue(dronePoints.get(0));

        this.x = new TextField();
        this.y = new TextField();
        this.z = new TextField();
        this.del = new Button("X");
        this.del.setId(String.valueOf(num));
        this.x.setText("0");
        this.y.setText("0");
        this.z.setText("100");
        this.recalculatePosition(num);

        this.selectPoint.setOnAction(e->{
            if(this.selectPoint.getValue()==null || this.selectPoint.getValue().getId() == -1){
                this.x.setEditable(true);
                this.y.setEditable(true);
                this.z.setEditable(true);
                this.x.setText("0");
                this.y.setText("0");
                this.z.setText("100");
            } else {
                this.x.setEditable(false);
                this.y.setEditable(false);
                this.z.setEditable(false);
                this.x.setText(String.valueOf(this.selectPoint.getValue().getWspx()));
                this.y.setText(String.valueOf(this.selectPoint.getValue().getWspy()));
                this.z.setText(String.valueOf(this.selectPoint.getValue().getWspz()));
            }
        });
    }
    public void recalculatePosition(int num){
        this.selectPoint.setLayoutX(10);
        this.selectPoint.setLayoutY(num*70+10);
        this.selectPoint.setPrefWidth(200);
        this.del.setPrefWidth(30);
        this.del.setLayoutX(235);
        this.del.setLayoutY(num*70+10);
        this.x.setLayoutX(10);
        this.x.setPrefWidth(85);
        this.y.setLayoutX(95);
        this.y.setPrefWidth(85);
        this.z.setLayoutX(180);
        this.z.setPrefWidth(85);
        this.x.setLayoutY(num*70+37);
        this.y.setLayoutY(num*70+37);
        this.z.setLayoutY(num*70+37);
    }
}
