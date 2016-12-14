package dataModels;

import lombok.Getter;
import lombok.Setter;

public class Punkt_na_trasie extends DataModel{
  @Getter @Setter Long numer_kolejny;
  @Getter @Setter String trasa_nazwa;
  @Getter @Setter Long trasa_uzytkownik_id;
  @Getter @Setter Long punkt_kontrolny_id;
  @Getter @Setter Long polozenie_id;
}
