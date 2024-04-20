DROP SEQUENCE sqRacer;
DROP SEQUENCE sqRace;
DROP TABLE racer CASCADE CONSTRAINTS;
DROP TABLE race CASCADE CONSTRAINTS;
DROP TABLE participation CASCADE CONSTRAINTS;

select id, racename, racedate from race;
select idrace, idracer, racetype, racertime from participation;

CREATE SEQUENCE sqRacer;
CREATE SEQUENCE sqRace;
CREATE TABLE race
( id INTEGER,
  racename VARCHAR2(30),
  racedate DATE,
  CONSTRAINT pkRace PRIMARY KEY (id)
);

CREATE TABLE racer
( id INTEGER,
  racername VARCHAR2(30),
  racermale VARCHAR2(1),
  CONSTRAINT pkRacer PRIMARY KEY (id),
  CONSTRAINT uqRacer UNIQUE (racername)
);

select race.id as raceid, racer.id as racerid, racername, racermale, racetype, racertime from racer
inner join participation on participation.idRacer = racer.id
inner join race on race.id = participation.idRace
where upper(racername) LIKE upper('%?%') and race.id = ?
order by racetype, racername;


CREATE TABLE participation
( idRace INTEGER,
  idRacer INTEGER,
  racetype VARCHAR2(20),
  racertime DATE,
  CONSTRAINT pkPart PRIMARY KEY (idRace, idRacer),
  CONSTRAINT fkPartRace FOREIGN KEY (idRace) REFERENCES race,
  CONSTRAINT fkPartRacer FOREIGN KEY (idRacer) REFERENCES racer,
  CONSTRAINT ckRaceType CHECK(racetype IN ('Quarter','Half','Full', 'UNDEFINED'))
);

INSERT INTO race VALUES(sqRace.NEXTVAL, 'Kirchenmarathon 2018',TO_DATE('31.08.2018','DD.MM.YYYY'));
INSERT INTO race VALUES(sqRace.NEXTVAL, 'Mostlauf 2017',TO_DATE('31.03.2017','DD.MM.YYYY'));
INSERT INTO race VALUES(sqRace.NEXTVAL, 'Rabensteiner Berglauf 2017',TO_DATE('11.06.2017','DD.MM.YYYY'));
INSERT INTO race VALUES(sqRace.NEXTVAL, 'Wasserlauf Liebenfels 2022',TO_DATE('11.06.2022','DD.MM.YYYY'));


INSERT INTO racer VALUES(sqRacer.NEXTVAL, 'Jokurt Karin','0');
INSERT INTO racer VALUES(sqRacer.NEXTVAL, 'Nakurt Norbert','1');
INSERT INTO racer VALUES(sqRacer.NEXTVAL, 'Jokurt Kerstin','0');
INSERT INTO racer VALUES(sqRacer.NEXTVAL, 'Kernstein Susi','0');
INSERT INTO racer VALUES(sqRacer.NEXTVAL, 'Weinstein Werner','1');
INSERT INTO racer VALUES(sqRacer.NEXTVAL, 'Obersteiner Annalena','0');

INSERT INTO participation VALUES(1,2,'Full',TO_DATE('04:21:18','HH24:MI:SS'));
INSERT INTO participation VALUES(1,3,'Full',TO_DATE('04:11:12','HH24:MI:SS'));
INSERT INTO participation VALUES(1,4,'Full',TO_DATE('03:21:12','HH24:MI:SS'));
INSERT INTO participation VALUES(2,2,'Half',TO_DATE('00:41:12','HH24:MI:SS'));
INSERT INTO participation VALUES(2,4,'Half',TO_DATE('00:51:12','HH24:MI:SS'));
INSERT INTO participation VALUES(2,5,'Half',TO_DATE('00:53:42','HH24:MI:SS'));
INSERT INTO participation VALUES(2,3,'Half',TO_DATE('00:21:42','HH24:MI:SS'));
INSERT INTO participation VALUES(2,1,'Half',TO_DATE('01:21:32','HH24:MI:SS'));
INSERT INTO participation VALUES(3,3,'Quarter',TO_DATE('00:23:12','HH24:MI:SS'));
INSERT INTO participation VALUES(3,6,'Quarter',TO_DATE('00:21:42','HH24:MI:SS'));
INSERT INTO participation VALUES(3,5,'Quarter',TO_DATE('00:21:32','HH24:MI:SS'));
INSERT INTO participation VALUES(4,2,'Full',TO_DATE('04:21:12','HH24:MI:SS'));
INSERT INTO participation VALUES(4,4,'Full',TO_DATE('04:21:24','HH24:MI:SS'));
INSERT INTO participation VALUES(4,5,'Full',TO_DATE('04:23:12','HH24:MI:SS'));
INSERT INTO participation VALUES(4,3,'Quarter',TO_DATE('00:21:12','HH24:MI:SS'));
INSERT INTO participation VALUES(4,1,'Quarter',TO_DATE('00:18:12','HH24:MI:SS'));


COMMIT;

