package dataModels;

import com.sun.istack.internal.NotNull;

import javax.xml.ws.soap.Addressing;
import java.lang.annotation.Target;
import lombok.Getter;
import lombok.Setter;

public class Dron extends DataModel {
    static final int STATUS_WOLNY = 0;
    static final int STATUS_PRZYDZIELONY_DO_ZADANIA = 1;
    static final int STATUS_WYKONUJE_ZADANIE = 2;
    static final int STATUS_WYŁĄCZONY = 3;

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
    @Setter @Getter private Integer stan;
    @Getter @Setter private Integer punkt_kontrolny_id;

    public String getStanString(){
        //Zawsze chciałem to zrobić :)
        if(this.stan==null) return "other";
        return this.stan==STATUS_WOLNY ? "free" :
                (this.stan==STATUS_PRZYDZIELONY_DO_ZADANIA ? "assigned to job" :
                        (this.stan==STATUS_WYKONUJE_ZADANIE ? "job in progress" :
                                (this.stan==STATUS_WYŁĄCZONY ? "not available" : "other")));
    }
}