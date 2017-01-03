package dataModels;

import lombok.Getter;
import lombok.Setter;

public class Trasa extends DataModel {
  @Getter @Setter String nazwa;
  @Getter @Setter Integer uzytkownik_id;

  public String toString(){
    return this.nazwa+"#"+this.uzytkownik_id;
  }
}
