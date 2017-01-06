package dataModels;

import lombok.Getter;
import lombok.Setter;

public class Zadanie extends DataModel{
  @Getter @Setter Integer id;
  @Getter @Setter java.sql.Timestamp data_rozpoczenia;
  @Getter @Setter Float szacowana_dlugosc;
  @Getter @Setter Integer typ;
  @Getter @Setter String trasa_nazwa;
  @Getter @Setter Integer trasa_uzytkownik_id;
  @Getter @Setter Integer uzytkownik_id;
  @Getter @Setter Integer dron_id;
  @Getter@Setter Integer punkt_koncowy_id;
}
