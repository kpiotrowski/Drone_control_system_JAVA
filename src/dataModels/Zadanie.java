package dataModels;

import lombok.Getter;
import lombok.Setter;

public class Zadanie extends DataModel{
  static final int STATUS_NOWE_ZADANIE = 0;
  static final int STATUS_PRZYDZIELONO_DRONA = 1;
  static final int STATUS_W_REALIZACJI = 2;
  static final int STATUS_NIEUDANE = 3;
  static final int STATUS_ZAKOŃCZONE = 4;

  @Getter @Setter Integer id;
  @Getter @Setter java.sql.Timestamp data_rozpoczenia;
  @Getter @Setter Float szacowana_dlugosc;
  @Getter @Setter Integer typ;
  @Getter @Setter String trasa_nazwa;
  @Getter @Setter Integer trasa_uzytkownik_id;
  @Getter @Setter Integer uzytkownik_id;
  @Getter @Setter Integer dron_id;
  @Getter @Setter Integer punkt_koncowy_id;
  @Getter @Setter Integer stan;
  private String stanString;
  public String getStanString(){
    switch (this.stan){
      case STATUS_NOWE_ZADANIE : return "new";
      case STATUS_PRZYDZIELONO_DRONA: return "drone assigned";
      case STATUS_W_REALIZACJI: return "in progress";
      case STATUS_NIEUDANE: return "failed";
      case STATUS_ZAKOŃCZONE: return "finished";
      default: return "other";
    }
  };

}
