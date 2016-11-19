package dataModels;

public class Polozenie extends DataModel{
  private Long wspx;
  private Long wspy;
  private Long wspz;
  private Long id;

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
