package dataModels;

public class Zadanie extends DataModel{
  private Long id;
  private java.sql.Date data_rozpoczenia;
  private Double szacowana_dlugosc;
  private Long typ;
  private String trasa_nazwa;
  private Long trasa_uzytkownik_id;
  private Long uzytkownik_id;
  private Long dron_id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public java.sql.Date getData_rozpoczenia() {
    return data_rozpoczenia;
  }

  public void setData_rozpoczenia(java.sql.Date data_rozpoczenia) {
    this.data_rozpoczenia = data_rozpoczenia;
  }

  public Double getSzacowana_dlugosc() {
    return szacowana_dlugosc;
  }

  public void setSzacowana_dlugosc(Double szacowana_dlugosc) {
    this.szacowana_dlugosc = szacowana_dlugosc;
  }

  public Long getTyp() {
    return typ;
  }

  public void setTyp(Long typ) {
    this.typ = typ;
  }

  public String getTrasa_nazwa() {
    return trasa_nazwa;
  }

  public void setTrasa_nazwa(String trasa_nazwa) {
    this.trasa_nazwa = trasa_nazwa;
  }

  public Long getTrasa_uzytkownik_id() {
    return trasa_uzytkownik_id;
  }

  public void setTrasa_uzytkownik_id(Long trasa_uzytkownik_id) {
    this.trasa_uzytkownik_id = trasa_uzytkownik_id;
  }

  public Long getUzytkownik_id() {
    return uzytkownik_id;
  }

  public void setUzytkownik_id(Long uzytkownik_id) {
    this.uzytkownik_id = uzytkownik_id;
  }

  public Long getDron_id() {
    return dron_id;
  }

  public void setDron_id(Long dron_id) {
    this.dron_id = dron_id;
  }
}
