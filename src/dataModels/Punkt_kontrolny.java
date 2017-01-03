package dataModels;

import com.sun.istack.internal.NotNull;
import com.sun.javafx.beans.IDProperty;
import javafx.beans.property.SimpleStringProperty;
import jdk.nashorn.internal.objects.annotations.Property;
import lombok.Getter;
import lombok.Setter;

public class Punkt_kontrolny extends DataModel{
  @Getter @Setter Integer id;
  @Getter @Setter String nazwa;
  @Getter @Setter Integer max_ilosc_dronow;
  @Getter @Setter Integer obecna_ilosc_dronow;
  @Getter @Setter Float wspx;
  @Getter @Setter Float wspy;
  @Getter @Setter Float wspz;

  public String toString(){
    return "#"+this.id+"_"+this.nazwa;
  }
}
