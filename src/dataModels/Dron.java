package dataModels;

import com.sun.istack.internal.NotNull;

import javax.xml.ws.soap.Addressing;
import java.lang.annotation.Target;

public class Dron extends DataModel{
  private Integer id;
  private String nazwa;
  private String opis;
  private Float masa;
  private Integer ilosc_wirnikow;
  private Float max_predkosc;
  private Float max_czas_lotu;
  private Float poziom_baterii;
  private Float wspx;
  private Float wspy;
  private Float wspz;
  private Integer stan;
  private Integer punkt_kontrolny_id;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNazwa() {
    return nazwa;
  }

  public void setNazwa(String nazwa) { this.nazwa = nazwa; }

  public String getOpis() {
    return opis;
  }

  public void setOpis(String opis) {
    this.opis = opis;
  }

  public Float getMasa() {
    return masa;
  }

  public void setMasa(Float masa) {
    this.masa = masa;
  }

  public Integer getIlosc_wirnikow() {
    return ilosc_wirnikow;
  }

  public void setIlosc_wirnikow(Integer ilosc_wirnikow) {
    this.ilosc_wirnikow = ilosc_wirnikow;
  }

  public Float getMax_predkosc() {
    return max_predkosc;
  }

  public void setMax_predkosc(Float max_predkosc) {
    this.max_predkosc = max_predkosc;
  }

  public Float getMax_czas_lotu() {
    return max_czas_lotu;
  }

  public void setMax_czas_lotu(Float max_czas_lotu) {
    this.max_czas_lotu = max_czas_lotu;
  }

  public Float getPoziom_baterii() {
    return poziom_baterii;
  }

  public void setPoziom_baterii(Float poziom_baterii) {
    this.poziom_baterii = poziom_baterii;
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

  public Integer getStan() {
    return stan;
  }

  public void setStan(Integer stan) {
    this.stan = stan;
  }

  public Integer getPunkt_kontrolny_id() {
    return punkt_kontrolny_id;
  }

  public void setPunkt_kontrolny_id(Integer punkt_kontrolny_id) {
    this.punkt_kontrolny_id = punkt_kontrolny_id;
  }
}
