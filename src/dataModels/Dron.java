package dataModels;

import com.sun.istack.internal.NotNull;

import javax.xml.ws.soap.Addressing;
import java.lang.annotation.Target;

public class Dron extends DataModel{
  private Long id;
  private String nazwa;
  private String opis;
  private Double masa;
  private Long ilosc_wirnikow;
  private Double max_predkosc;
  private Double max_czas_lotu;
  private Double poziom_baterii;
  private Double wspx;
  private Double wspy;
  private Double wspz;
  private Long stan;
  private Long punkt_kontrolny_id;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNazwa() {
    return nazwa;
  }

  public void setNazwa(String nazwa) {
    this.nazwa = nazwa;
  }

  public String getOpis() {
    return opis;
  }

  public void setOpis(String opis) {
    this.opis = opis;
  }

  public Double getMasa() {
    return masa;
  }

  public void setMasa(Double masa) {
    this.masa = masa;
  }

  public Long getIlosc_wirnikow() {
    return ilosc_wirnikow;
  }

  public void setIlosc_wirnikow(Long ilosc_wirnikow) {
    this.ilosc_wirnikow = ilosc_wirnikow;
  }

  public Double getMax_predkosc() {
    return max_predkosc;
  }

  public void setMax_predkosc(Double max_predkosc) {
    this.max_predkosc = max_predkosc;
  }

  public Double getMax_czas_lotu() {
    return max_czas_lotu;
  }

  public void setMax_czas_lotu(Double max_czas_lotu) {
    this.max_czas_lotu = max_czas_lotu;
  }

  public Double getPoziom_baterii() {
    return poziom_baterii;
  }

  public void setPoziom_baterii(Double poziom_baterii) {
    this.poziom_baterii = poziom_baterii;
  }

  public Double getWspx() {
    return wspx;
  }

  public void setWspx(Double wspx) {
    this.wspx = wspx;
  }

  public Double getWspy() {
    return wspy;
  }

  public void setWspy(Double wspy) {
    this.wspy = wspy;
  }

  public Double getWspz() {
    return wspz;
  }

  public void setWspz(Double wspz) {
    this.wspz = wspz;
  }

  public Long getStan() {
    return stan;
  }

  public void setStan(Long stan) {
    this.stan = stan;
  }

  public Long getPunkt_kontrolny_id() {
    return punkt_kontrolny_id;
  }

  public void setPunkt_kontrolny_id(Long punkt_kontrolny_id) {
    this.punkt_kontrolny_id = punkt_kontrolny_id;
  }
}
