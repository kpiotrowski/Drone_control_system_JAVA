package dataModels;

public class Uzytkownik extends DataModel{
  private Integer id;
  private String imie;
  private String nazwisko;
  private java.sql.Date data_urodzenia;
  private String login;
  private String telefon;
  private String haslo;
  private Integer poziom_uprawnien;

  public Integer getId() { return id; }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getImie() {
    return imie;
  }

  public void setImie(String imie) {
    this.imie = imie;
  }

  public String getNazwisko() {
    return nazwisko;
  }

  public void setNazwisko(String nazwisko) {
    this.nazwisko = nazwisko;
  }

  public java.sql.Date getData_urodzenia() { return data_urodzenia; }

  public void setData_urodzenia(java.sql.Date data_urodzenia) {
    this.data_urodzenia = data_urodzenia;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getTelefon() {
    return telefon;
  }

  public void setTelefon(String telefon) {
    this.telefon = telefon;
  }

  public String getHaslo() {
    return haslo;
  }

  public void setHaslo(String haslo) {
    this.haslo = haslo;
  }

  public Integer getPoziom_uprawnien() {
    return poziom_uprawnien;
  }

  public void setPoziom_uprawnien(Integer poziom_uprawnien) {
    this.poziom_uprawnien = poziom_uprawnien;
  }
}
