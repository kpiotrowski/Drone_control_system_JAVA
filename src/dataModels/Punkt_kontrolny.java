package dataModels;

public class Punkt_kontrolny extends DataModel{
  private Long id;
  private String opis;
  private Long max_ilosc_dronow;
  private Long obecna_ilosc_dronow;
  private Long wspx;
  private Long wspy;
  private Long wspz;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOpis() {
    return opis;
  }

  public void setOpis(String opis) {
    this.opis = opis;
  }

  public Long getMax_ilosc_dronow() {
    return max_ilosc_dronow;
  }

  public void setMax_ilosc_dronow(Long max_ilosc_dronow) {
    this.max_ilosc_dronow = max_ilosc_dronow;
  }

  public Long getObecna_ilosc_dronow() {
    return obecna_ilosc_dronow;
  }

  public void setObecna_ilosc_dronow(Long obecna_ilosc_dronow) {
    this.obecna_ilosc_dronow = obecna_ilosc_dronow;
  }

  public Long getWspx() {
    return wspx;
  }

  public void setWspx(Long wspx) {
    this.wspx = wspx;
  }

  public Long getWspy() {
    return wspy;
  }

  public void setWspy(Long wspy) {
    this.wspy = wspy;
  }

  public Long getWspz() {
    return wspz;
  }

  public void setWspz(Long wspz) {
    this.wspz = wspz;
  }
}
