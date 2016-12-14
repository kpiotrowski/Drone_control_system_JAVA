package dataModels;

import com.sun.istack.internal.NotNull;

import javax.xml.ws.soap.Addressing;
import java.lang.annotation.Target;
import lombok.Getter;
import lombok.Setter;

public class Dron extends DataModel {
    @Getter @Setter private Integer id;
    @Getter @Setter private String nazwa;
    @Getter @Setter private String opis;
    @Getter @Setter private Float masa;
    @Getter @Setter private Integer ilosc_wirnikow;
    @Getter @Setter private Float max_predkosc;
    @Getter @Setter private Float max_czas_lotu;
    @Getter @Setter private Float poziom_baterii;
    @Getter @Setter private Float wspx;
    @Getter @Setter private Float wspy;
    @Getter @Setter private Float wspz;
    @Getter @Setter private Integer stan;
    @Getter @Setter private Integer punkt_kontrolny_id;
}