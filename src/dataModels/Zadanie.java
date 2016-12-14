package dataModels;

import lombok.Getter;
import lombok.Setter;

public class Zadanie extends DataModel{
  @Getter @Setter Long id;
  @Getter @Setter java.sql.Date data_rozpoczenia;
  @Getter @Setter Double szacowana_dlugosc;
  @Getter @Setter Long typ;
  @Getter @Setter String trasa_nazwa;
  @Getter @Setter Long trasa_uzytkownik_id;
  @Getter @Setter Long uzytkownik_id;
  @Getter @Setter Long dron_id;
}
