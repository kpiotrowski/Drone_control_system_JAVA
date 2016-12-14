package dataModels;

import lombok.Getter;
import lombok.Setter;

public class Uzytkownik extends DataModel{
  @Getter @Setter Integer id;
  @Getter @Setter String imie;
  @Getter @Setter String nazwisko;
  @Getter @Setter java.sql.Date data_urodzenia;
  @Getter @Setter String login;
  @Getter @Setter String telefon;
  @Getter @Setter String haslo;
  @Getter @Setter Integer poziom_uprawnien;
}
