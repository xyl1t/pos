DROP SEQUENCE sqRacer;
DROP TABLE racer CASCADE CONSTRAINTS;

CREATE SEQUENCE sqRacer;
CREATE TABLE racer 
( id INTEGER,
  racername VARCHAR2(30),
  racermale VARCHAR2(1),
  racetype VARCHAR2(20),
  racertime DATE,
  CONSTRAINT pkRacer PRIMARY KEY (id),
  CONSTRAINT uqRacer UNIQUE (racername),
  CONSTRAINT ckRaceType CHECK(racetype IN ('Quarter','Half','Full'))
);

INSERT INTO racer VALUES(sqRacer.NEXTVAL, 'Jokurt Karin','0','Full',TO_DATE('04:21:12','HH24:MI:SS'));
INSERT INTO racer VALUES(sqRacer.NEXTVAL, 'Nakurt Norbert','1','Full',TO_DATE('03:31:32','HH24:MI:SS'));
INSERT INTO racer VALUES(sqRacer.NEXTVAL, 'Jokurt Kerstin','0','Full',TO_DATE('04:11:16','HH24:MI:SS'));
INSERT INTO racer VALUES(sqRacer.NEXTVAL, 'Kernstein Susi','0','Quarter',TO_DATE('00:41:12','HH24:MI:SS'));
INSERT INTO racer VALUES(sqRacer.NEXTVAL, 'Weinstein Werner','1','Quarter',TO_DATE('00:51:12','HH24:MI:SS'));
INSERT INTO racer VALUES(sqRacer.NEXTVAL, 'Obersteiner Annalena','0','Half',TO_DATE('01:51:12','HH24:MI:SS'));

COMMIT;
