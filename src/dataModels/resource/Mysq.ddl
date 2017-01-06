CREATE TABLE Dron
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  nazwa VARCHAR(256) NOT NULL,
  opis VARCHAR(2048),
  masa FLOAT(6,4) NOT NULL,
  ilosc_wirnikow INT(11) NOT NULL,
  max_predkosc FLOAT NOT NULL,
  max_czas_lotu FLOAT NOT NULL,
  poziom_baterii FLOAT NOT NULL,
  wspX FLOAT(15,10),
  wspY FLOAT(15,10),
  wspZ FLOAT(15,10),
  stan INT(11) NOT NULL,
  Punkt_kontrolny_id INT(11)
);
CREATE TABLE Polozenie
(
  wspX INT(11) NOT NULL,
  wspY INT(11) NOT NULL,
  wspZ INT(11) NOT NULL,
  id INT(11) PRIMARY KEY NOT NULL
);
CREATE TABLE Punkt_kontrolny
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  nazwa VARCHAR(512),
  max_ilosc_dronow INT(11) NOT NULL,
  obecna_ilosc_dronow INT(11) NOT NULL,
  wspX FLOAT(12,6) NOT NULL,
  wspY FLOAT(12,6) NOT NULL,
  wspZ FLOAT(12,6) NOT NULL
);
CREATE TABLE Punkt_na_trasie
(
  numer_kolejny INT(11) NOT NULL,
  Trasa_nazwa VARCHAR(256) NOT NULL,
  Trasa_Uzytkownik_id INT(11) NOT NULL,
  Punkt_kontrolny_id INT(11),
  Polozenie_id INT(11),
  CONSTRAINT `PRIMARY` PRIMARY KEY (numer_kolejny, Trasa_nazwa, Trasa_Uzytkownik_id)
);
CREATE TABLE Trasa
(
  nazwa VARCHAR(256) NOT NULL,
  Uzytkownik_id INT(11) NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (nazwa, Uzytkownik_id)
);
CREATE TABLE Uzytkownik
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  imie VARCHAR(512) NOT NULL,
  nazwisko VARCHAR(512) NOT NULL,
  data_urodzenia DATE,
  login VARCHAR(128) NOT NULL,
  telefon VARCHAR(64),
  haslo VARCHAR(256) NOT NULL,
  poziom_uprawnien INT(11) NOT NULL
);
CREATE TABLE Zadanie
(
  id INT(11) NOT NULL AUTO_INCREMENT,
  data_rozpoczenia DATE NOT NULL,
  szacowana_dlugosc FLOAT(6,6) NOT NULL,
  typ INT(11) NOT NULL,
  Trasa_nazwa VARCHAR(256),
  Trasa_Uzytkownik_id INT(11),
  Uzytkownik_id INT(11) NOT NULL,
  Dron_id INT(11) NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (id, Uzytkownik_id, Dron_id)
);
ALTER TABLE Dron ADD FOREIGN KEY (Punkt_kontrolny_id) REFERENCES Punkt_kontrolny (id) ON DELETE SET NULL;
CREATE INDEX Dron_Punkt_kontrolny_FK ON Dron (Punkt_kontrolny_id);
ALTER TABLE Punkt_na_trasie ADD FOREIGN KEY (Trasa_nazwa, Trasa_Uzytkownik_id) REFERENCES Trasa (nazwa, Uzytkownik_id);
ALTER TABLE Punkt_na_trasie ADD FOREIGN KEY (Punkt_kontrolny_id) REFERENCES Punkt_kontrolny (id);
ALTER TABLE Punkt_na_trasie ADD FOREIGN KEY (Polozenie_id) REFERENCES Polozenie (id);
CREATE INDEX Punkt_na_trasie_Polozenie_FK ON Punkt_na_trasie (Polozenie_id);
CREATE INDEX Punkt_na_trasie_Trasa_FK ON Punkt_na_trasie (Trasa_nazwa, Trasa_Uzytkownik_id);
CREATE INDEX Punkt_Punkt_kontrolny_FK ON Punkt_na_trasie (Punkt_kontrolny_id);
ALTER TABLE Trasa ADD FOREIGN KEY (Uzytkownik_id) REFERENCES Uzytkownik (id);
CREATE INDEX Trasa_Uzytkownik_FK ON Trasa (Uzytkownik_id);
CREATE UNIQUE INDEX Uzytkownik_login_uindex ON Uzytkownik (login);
ALTER TABLE Zadanie ADD FOREIGN KEY (Trasa_nazwa, Trasa_Uzytkownik_id) REFERENCES Trasa (nazwa, Uzytkownik_id);
ALTER TABLE Zadanie ADD FOREIGN KEY (Uzytkownik_id) REFERENCES Uzytkownik (id);
ALTER TABLE Zadanie ADD FOREIGN KEY (Dron_id) REFERENCES Dron (id);
CREATE INDEX Zadanie_Dron_FK ON Zadanie (Dron_id);
CREATE INDEX Zadanie_Trasa_FK ON Zadanie (Trasa_nazwa, Trasa_Uzytkownik_id);
CREATE INDEX Zadanie_Uzytkownik_FK ON Zadanie (Uzytkownik_id);

DELIMITER $$
CREATE TRIGGER `PunktNaTrasieArcConstraint` BEFORE INSERT ON `Punkt_na_trasie`
FOR EACH ROW
  BEGIN
    IF NOT ( ( (NEW.Polozenie_id IS NOT NULL) AND (NEW.Punkt_kontrolny_id IS NULL) ) OR ( (NEW.Punkt_kontrolny_id IS NOT NULL) AND (NEW.Polozenie_id IS NULL) ) OR ( (NEW.Polozenie_id IS NULL) AND (NEW.Punkt_kontrolny_id IS NULL) ) ) THEN
      SIGNAL SQLSTATE '12345'
      SET MESSAGE_TEXT = 'Pomiędzy położeniem a punktem kontrolnym musi zachodzić relacja XOR';
    END IF;
  END$$
DELIMITER ;




























CREATE TABLE Dron
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  nazwa VARCHAR(256) NOT NULL,
  opis VARCHAR(2048),
  masa FLOAT(6,4) NOT NULL,
  ilosc_wirnikow INT(11) NOT NULL,
  max_predkosc FLOAT NOT NULL,
  max_czas_lotu FLOAT NOT NULL,
  poziom_baterii FLOAT NOT NULL,
  wspX FLOAT(15,10),
  wspY FLOAT(15,10),
  wspZ FLOAT(15,10),
  stan INT(11) NOT NULL,
  Punkt_kontrolny_id INT(11)
);
CREATE TABLE Polozenie
(
  wspX FLOAT(12,6) NOT NULL,
  wspY FLOAT(12,6) NOT NULL,
  wspZ FLOAT(12,6) NOT NULL,
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT
);
CREATE TABLE Punkt_kontrolny
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  nazwa VARCHAR(512),
  max_ilosc_dronow INT(11) NOT NULL,
  obecna_ilosc_dronow INT(11) NOT NULL,
  wspX FLOAT(12,6) NOT NULL,
  wspY FLOAT(12,6) NOT NULL,
  wspZ FLOAT(12,6) NOT NULL
);
CREATE TABLE Punkt_na_trasie
(
  numer_kolejny INT(11) NOT NULL,
  Trasa_nazwa VARCHAR(256) NOT NULL,
  Trasa_Uzytkownik_id INT(11) NOT NULL,
  Punkt_kontrolny_id INT(11),
  Polozenie_id INT(11),
  CONSTRAINT `PRIMARY` PRIMARY KEY (numer_kolejny, Trasa_nazwa, Trasa_Uzytkownik_id)
);
CREATE TABLE Trasa
(
  nazwa VARCHAR(256) NOT NULL,
  Uzytkownik_id INT(11) NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (nazwa, Uzytkownik_id)
);
CREATE TABLE Uzytkownik
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  imie VARCHAR(512) NOT NULL,
  nazwisko VARCHAR(512) NOT NULL,
  data_urodzenia DATE,
  login VARCHAR(128) NOT NULL,
  telefon VARCHAR(64),
  haslo VARCHAR(256) NOT NULL,
  poziom_uprawnien INT(11) NOT NULL
);
CREATE TABLE Zadanie
(
  id INT(11) NOT NULL AUTO_INCREMENT,
  data_rozpoczenia DATE NOT NULL,
  szacowana_dlugosc FLOAT(6,6) NOT NULL,
  typ INT(11) NOT NULL,
  Trasa_nazwa VARCHAR(256),
  Trasa_Uzytkownik_id INT(11),
  Uzytkownik_id INT(11) NOT NULL,
  Dron_id INT(11) NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (id, Uzytkownik_id, Dron_id)
);
ALTER TABLE Dron ADD FOREIGN KEY (Punkt_kontrolny_id) REFERENCES Punkt_kontrolny (id) ON DELETE SET NULL;
CREATE INDEX Dron_Punkt_kontrolny_FK ON Dron (Punkt_kontrolny_id);
CREATE UNIQUE INDEX Polozenie_id_uindex ON Polozenie (id);
ALTER TABLE Punkt_na_trasie ADD FOREIGN KEY (Trasa_nazwa, Trasa_Uzytkownik_id) REFERENCES Trasa (nazwa, Uzytkownik_id) ON DELETE CASCADE;
ALTER TABLE Punkt_na_trasie ADD FOREIGN KEY (Punkt_kontrolny_id) REFERENCES Punkt_kontrolny (id);
ALTER TABLE Punkt_na_trasie ADD FOREIGN KEY (Polozenie_id) REFERENCES Polozenie (id);
CREATE INDEX Punkt_na_trasie_Polozenie_id_fk ON Punkt_na_trasie (Polozenie_id);
CREATE INDEX Punkt_na_trasie_Trasa_FK ON Punkt_na_trasie (Trasa_nazwa, Trasa_Uzytkownik_id);
CREATE INDEX Punkt_Punkt_kontrolny_FK ON Punkt_na_trasie (Punkt_kontrolny_id);
ALTER TABLE Trasa ADD FOREIGN KEY (Uzytkownik_id) REFERENCES Uzytkownik (id);
CREATE INDEX Trasa_Uzytkownik_FK ON Trasa (Uzytkownik_id);
CREATE UNIQUE INDEX Uzytkownik_login_uindex ON Uzytkownik (login);
ALTER TABLE Zadanie ADD FOREIGN KEY (Trasa_nazwa, Trasa_Uzytkownik_id) REFERENCES Trasa (nazwa, Uzytkownik_id);
ALTER TABLE Zadanie ADD FOREIGN KEY (Uzytkownik_id) REFERENCES Uzytkownik (id);
ALTER TABLE Zadanie ADD FOREIGN KEY (Dron_id) REFERENCES Dron (id);
CREATE INDEX Zadanie_Dron_FK ON Zadanie (Dron_id);
CREATE INDEX Zadanie_Trasa_FK ON Zadanie (Trasa_nazwa, Trasa_Uzytkownik_id);
CREATE INDEX Zadanie_Uzytkownik_FK ON Zadanie (Uzytkownik_id);