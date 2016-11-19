CREATE TABLE Dron
  (
    id    INTEGER NOT NULL,
    nazwa VARCHAR (256) NOT NULL ,
    opis  VARCHAR (2048) ,
    masa FLOAT(6) NOT NULL ,
    ilosc_wirnikow INTEGER NOT NULL ,
    max_predkosc FLOAT (4) NOT NULL ,
    max_czas_lotu FLOAT (2) NOT NULL,
    poziom_baterii FLOAT (4) NOT NULL ,
    wspX FLOAT (10) ,
    wspY FLOAT (10) ,
    wspZ FLOAT (10) ,
    stan               INTEGER NOT NULL ,
    Punkt_kontrolny_id INTEGER,
    PRIMARY KEY ( id )
  ) ;

CREATE TABLE Polozenie
  (
    wspX INTEGER NOT NULL ,
    wspY INTEGER NOT NULL ,
    wspZ INTEGER NOT NULL ,
    id   INTEGER NOT NULL ,
    PRIMARY KEY ( id )
  ) ;
CREATE TABLE Punkt_kontrolny
  (
    id                  INTEGER NOT NULL,
    opis                VARCHAR (2048) ,
    max_ilosc_dronow    INTEGER NOT NULL ,
    obecna_ilosc_dronow INTEGER NOT NULL ,
    wspX                INTEGER NOT NULL ,
    wspY                INTEGER NOT NULL ,
    wspZ                INTEGER NOT NULL ,
    PRIMARY KEY ( id )
  ) ;

CREATE TABLE Punkt_na_trasie
  (
    numer_kolejny       INTEGER NOT NULL ,
    Trasa_nazwa         VARCHAR (256) NOT NULL ,
    Trasa_Uzytkownik_id INTEGER NOT NULL ,
    Punkt_kontrolny_id  INTEGER ,
    Polozenie_id        INTEGER
  ) ;
ALTER TABLE Punkt_na_trasie ADD CONSTRAINT FKArc_1 CHECK ( ( (Polozenie_id IS NOT NULL) AND (Punkt_kontrolny_id IS NULL) ) OR ( (Punkt_kontrolny_id IS NOT NULL) AND (Polozenie_id IS NULL) ) OR ( (Polozenie_id IS NULL) AND (Punkt_kontrolny_id IS NULL) ) ) ;
ALTER TABLE Punkt_na_trasie ADD CONSTRAINT Punkt_na_trasie_PK PRIMARY KEY ( numer_kolejny, Trasa_nazwa, Trasa_Uzytkownik_id ) ;


CREATE TABLE Trasa
  (
    nazwa         VARCHAR (256) NOT NULL ,
    Uzytkownik_id INTEGER NOT NULL
  ) ;
ALTER TABLE Trasa ADD CONSTRAINT Trasa_PK PRIMARY KEY ( nazwa, Uzytkownik_id ) ;


CREATE TABLE Uzytkownik
  (
    id               INTEGER NOT NULL ,
    imie             VARCHAR (512) NOT NULL ,
    nazwisko         VARCHAR (512) NOT NULL ,
    data_urodzenia   DATE ,
    email            VARCHAR (128) NOT NULL ,
    telefon          VARCHAR (64) ,
    haslo            VARCHAR (256) NOT NULL ,
    poziom_uprawnien INTEGER NOT NULL ,
    PRIMARY KEY ( id )
  ) ;

CREATE TABLE Zadanie
  (
    id               INTEGER NOT NULL,
    data_rozpoczenia DATE NOT NULL ,
    szacowana_dlugosc FLOAT (6) NOT NULL ,
    typ                 INTEGER NOT NULL ,
    Trasa_nazwa         VARCHAR (256) ,
    Trasa_Uzytkownik_id INTEGER ,
    Uzytkownik_id       INTEGER NOT NULL ,
    Dron_id             INTEGER NOT NULL ,
    PRIMARY KEY ( id, Uzytkownik_id, Dron_id )
  ) ;

ALTER TABLE Dron ADD CONSTRAINT Dron_Punkt_kontrolny_FK FOREIGN KEY ( Punkt_kontrolny_id ) REFERENCES Punkt_kontrolny ( id ) ON
DELETE SET NULL ;

ALTER TABLE Punkt_na_trasie ADD CONSTRAINT Punkt_Punkt_kontrolny_FK FOREIGN KEY ( Punkt_kontrolny_id ) REFERENCES Punkt_kontrolny ( id ) ;

ALTER TABLE Punkt_na_trasie ADD CONSTRAINT Punkt_na_trasie_Polozenie_FK FOREIGN KEY ( Polozenie_id ) REFERENCES Polozenie ( id ) ;

ALTER TABLE Punkt_na_trasie ADD CONSTRAINT Punkt_na_trasie_Trasa_FK FOREIGN KEY ( Trasa_nazwa, Trasa_Uzytkownik_id ) REFERENCES Trasa ( nazwa, Uzytkownik_id ) ;

ALTER TABLE Trasa ADD CONSTRAINT Trasa_Uzytkownik_FK FOREIGN KEY ( Uzytkownik_id ) REFERENCES Uzytkownik ( id ) ;

ALTER TABLE Zadanie ADD CONSTRAINT Zadanie_Dron_FK FOREIGN KEY ( Dron_id ) REFERENCES Dron ( id ) ;

ALTER TABLE Zadanie ADD CONSTRAINT Zadanie_Trasa_FK FOREIGN KEY ( Trasa_nazwa, Trasa_Uzytkownik_id ) REFERENCES Trasa ( nazwa, Uzytkownik_id ) ;

ALTER TABLE Zadanie ADD CONSTRAINT Zadanie_Uzytkownik_FK FOREIGN KEY ( Uzytkownik_id ) REFERENCES Uzytkownik ( id ) ;
