package dataModels;

import com.sun.javafx.collections.SortableList;
import lombok.Getter;
import lombok.Setter;

public class Punkt_na_trasie extends DataModel{
  @Getter @Setter Integer numer_kolejny;
  @Getter @Setter String trasa_nazwa;
  @Getter @Setter Integer trasa_uzytkownik_id;
  @Getter @Setter Integer punkt_kontrolny_id;
  @Getter @Setter Integer polozenie_id;

  @Getter @Setter Float wspX;
  @Getter @Setter Float wspY;
  @Getter @Setter Float wspZ;
}
