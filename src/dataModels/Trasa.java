package dataModels;

public class Trasa extends DataModel {
  private String nazwa;
  private Long uzytkownik_id;

  public String getNazwa() {
    return nazwa;
  }

  public void setNazwa(String nazwa) {
    this.nazwa = nazwa;
  }

  public Long getUzytkownik_id() {
    return uzytkownik_id;
  }

  public void setUzytkownik_id(Long uzytkownik_id) {
    this.uzytkownik_id = uzytkownik_id;
  }
}
