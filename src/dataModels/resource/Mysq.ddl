CREATE TABLE Dron
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  nazwa VARCHAR(256) NOT NULL,
  opis VARCHAR(2048),
  masa FLOAT(12,6) NOT NULL,
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
  data_rozpoczenia DATETIME NOT NULL,
  szacowana_dlugosc FLOAT(12,6),
  typ INT(11) NOT NULL,
  Trasa_nazwa VARCHAR(256),
  Trasa_Uzytkownik_id INT(11),
  Uzytkownik_id INT(11) NOT NULL,
  Dron_id INT(11),
  Punkt_koncowy_id INT(11),
  stan INT(11) DEFAULT '0' NOT NULL,
  File_id INT(11),
  CONSTRAINT `PRIMARY` PRIMARY KEY (id, Uzytkownik_id)
);
CREATE TABLE Plik
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  data LONGBLOB,
  name VARCHAR(256),
  type VARCHAR(128)
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
ALTER TABLE Zadanie ADD FOREIGN KEY (Punkt_koncowy_id) REFERENCES Punkt_kontrolny (id);
ALTER TABLE Zadanie ADD FOREIGN KEY (File_id) REFERENCES Plik (id) ON DELETE SET NULL ON UPDATE CASCADE;
CREATE INDEX Zadanie_Dron_FK ON Zadanie (Dron_id);
CREATE INDEX Zadanie_Plik_id_fk ON Zadanie (File_id);
CREATE INDEX Zadanie_Punkt_kontrolny_id_fk ON Zadanie (Punkt_koncowy_id);
CREATE INDEX Zadanie_Trasa_FK ON Zadanie (Trasa_nazwa, Trasa_Uzytkownik_id);
CREATE INDEX Zadanie_Uzytkownik_FK ON Zadanie (Uzytkownik_id);
CREATE UNIQUE INDEX Plik_id_uindex ON Plik (id);

SET GLOBAL event_scheduler=ON;



DROP EVENT IF EXISTS assign_drone;
DELIMITER $$
CREATE EVENT assign_drone ON SCHEDULE EVERY 5 MINUTE
DO
  BEGIN
    DECLARE done INTEGER DEFAULT 0;
    DECLARE jobID INTEGER DEFAULT NULL;
    DECLARE droneId INTEGER DEFAULT NULL;
    DECLARE empty_jobs CURSOR FOR
      SELECT z.id FROM Zadanie z WHERE stan=0 AND data_rozpoczenia < DATE_ADD(NOW(),INTERVAL 1 DAY) AND data_rozpoczenia > DATE_SUB(NOW(),INTERVAL 1 DAY) ORDER BY data_rozpoczenia ;
    DECLARE start_job CURSOR FOR
      SELECT z.id, z.Dron_id FROM Zadanie z WHERE stan=1 AND data_rozpoczenia < NOW() ORDER BY data_rozpoczenia ;
    DECLARE finish_job CURSOR FOR
      SELECT z.id, z.Dron_id FROM Zadanie z WHERE stan=2 AND z.modified < DATE_SUB(NOW(), INTERVAL 5 MINUTE) ORDER BY data_rozpoczenia;


    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    /*Set failed old jobs*/
    UPDATE Zadanie SET stan=3 WHERE stan=0 AND data_rozpoczenia<DATE_SUB(NOW(),INTERVAL 1 DAY);
    SELECT ROW_COUNT() INTO @affected_rows;
    IF @affected_rows=0 THEN
      ROLLBACK;
    ELSE
      COMMIT;
    END IF;

    /*Assign drone for new jobs*/
    OPEN empty_jobs;
    LoopLabel: LOOP
      FETCH empty_jobs INTO jobID;
      IF done THEN
        LEAVE LoopLabel;
      END IF;
      SELECT id INTO droneId FROM Dron WHERE stan=0;
      IF droneId IS NULL THEN
        LEAVE LoopLabel;
      END IF;
      UPDATE Zadanie SET stan=1,Dron_id=droneId WHERE id = jobID;
      SELECT ROW_COUNT() INTO @affected_rows1;
      UPDATE Dron SET stan=1 WHERE id = droneId;
      SELECT ROW_COUNT() INTO @affected_rows2;
      IF @affected_rows1!=@affected_rows2 THEN
        ROLLBACK;
      ELSE
        COMMIT;
      END IF;
    END LOOP;
    CLOSE empty_jobs;

    /*Start assigned jobs*/
    SET done=0;
    OPEN start_job;
    LoopLabel: LOOP
      FETCH start_job INTO jobID, droneId;
      if done THEN
        LEAVE LoopLabel;
      END IF;
      UPDATE Zadanie SET stan=2 WHERE id = jobID;
      SELECT ROW_COUNT() INTO @affected_rows1;
      UPDATE Dron SET stan=2 WHERE id = droneId;
      SELECT ROW_COUNT() INTO @affected_rows2;
      IF @affected_rows1!=@affected_rows2 THEN
        ROLLBACK;
      ELSE
        COMMIT;
      END IF;
    END LOOP;
    CLOSE start_job;

    /*Finish jobs*/
    SET done=0;
    OPEN finish_job;
    LoopLabel: LOOP
      FETCH finish_job INTO jobID, droneId;
      if done THEN
        LEAVE LoopLabel;
      END IF;
      UPDATE Zadanie SET stan=4,Dron_id=null,File_id=1 WHERE id = jobID;
      SELECT ROW_COUNT() INTO @affected_rows1;
      UPDATE Dron SET stan=0 WHERE id = droneId;
      SELECT ROW_COUNT() INTO @affected_rows2;
      IF @affected_rows1!=@affected_rows2 THEN
        ROLLBACK;
      ELSE
        COMMIT;
      END IF;
    END LOOP;
    CLOSE finish_job;
  END;
DELIMITER ;