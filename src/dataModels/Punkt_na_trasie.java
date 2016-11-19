package dataModels;

public class Punkt_na_trasie extends DataModel{
  private Long numer_kolejny;
  private String trasa_nazwa;
  private Long trasa_uzytkownik_id;
  private Long punkt_kontrolny_id;
  private Long polozenie_id;

  public Long getNumer_kolejny() {
    return numer_kolejny;
  }

  public void setNumer_kolejny(Long numer_kolejny) {
    this.numer_kolejny = numer_kolejny;
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

  public Long getPunkt_kontrolny_id() {
    return punkt_kontrolny_id;
  }

  public void setPunkt_kontrolny_id(Long punkt_kontrolny_id) {
    this.punkt_kontrolny_id = punkt_kontrolny_id;
  }

  public Long getPolozenie_id() {
    return polozenie_id;
  }

  public void setPolozenie_id(Long polozenie_id) {
    this.polozenie_id = polozenie_id;
  }
}
