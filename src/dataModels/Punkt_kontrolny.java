package dataModels;

import com.sun.istack.internal.NotNull;
import com.sun.javafx.beans.IDProperty;
import javafx.beans.property.SimpleStringProperty;
import jdk.nashorn.internal.objects.annotations.Property;

public class Punkt_kontrolny extends DataModel{
  private Integer id;
  private String nazwa;
  private Integer max_ilosc_dronow;
  private Integer obecna_ilosc_dronow;
  private Float wspx;
  private Float wspy;
  private Float wspz;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNazwa() {
    return nazwa;
  }

  public void setNazwa(String nazwa) {this.nazwa = nazwa;}

  public Integer getMax_ilosc_dronow() {
    return max_ilosc_dronow;
  }

  public void setMax_ilosc_dronow(Integer max_ilosc_dronow) {
    this.max_ilosc_dronow = max_ilosc_dronow;
  }

  public Integer getObecna_ilosc_dronow() {
    return obecna_ilosc_dronow;
  }

  public void setObecna_ilosc_dronow(Integer obecna_ilosc_dronow) {
    this.obecna_ilosc_dronow = obecna_ilosc_dronow;
  }

  public Float getWspx() {
    return wspx;
  }

  public void setWspx(Float wspx) {
    this.wspx = wspx;
  }

  public Float getWspy() {
    return wspy;
  }

  public void setWspy(Float wspy) {
    this.wspy = wspy;
  }

  public Float getWspz() {
    return wspz;
  }

  public void setWspz(Float wspz) {
    this.wspz = wspz;
  }

  public String toString(){
    return this.id.toString()+":"+this.nazwa;
  }
}
