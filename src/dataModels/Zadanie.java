package dataModels;

import lombok.Getter;
import lombok.Setter;

public class Zadanie extends DataModel{
  public static final int STATUS_NOWE_ZADANIE = 0;
  public static final int STATUS_PRZYDZIELONO_DRONA = 1;
  private static final int STATUS_W_REALIZACJI = 2;
  private static final int STATUS_NIEUDANE = 3;
  public static final int STATUS_ZAKONCZONE = 4;
  public static final String[] statusStr= new String[]{"new","drone assigned","in progress","failed","finished"};

  private static final int TYPE_VIDEO = 0;
  private static final int TYPE_PHOTOS = 1;
  public static final int TYPE_MOVE_TO_POINT = 2;
  public static final String[] typesStr = new String[]{"make video","take_photos","move to point"};

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

  public String getStanString(){
    if(this.stan==null) return "other";
    switch (this.stan){
      case STATUS_NOWE_ZADANIE : return statusStr[0];
      case STATUS_PRZYDZIELONO_DRONA: return statusStr[1];
      case STATUS_W_REALIZACJI: return statusStr[2];
      case STATUS_NIEUDANE: return statusStr[3];
      case STATUS_ZAKONCZONE: return statusStr[4];
      default: return "other";
    }
  };

  public String getTypeString(){
    if(this.typ==null) return"other";
    switch (this.typ){
      case TYPE_VIDEO: return typesStr[0];
      case TYPE_PHOTOS: return typesStr[1];
      case TYPE_MOVE_TO_POINT: return typesStr[2];
      default: return "other";
    }
  }



}
