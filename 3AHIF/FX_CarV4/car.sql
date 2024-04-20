DROP SEQUENCE sqCar;
DROP SEQUENCE sqRepair;
DROP TABLE rents CASCADE CONSTRAINTS;
DROP TABLE repairs CASCADE CONSTRAINTS;
DROP TABLE cars CASCADE CONSTRAINTS;

CREATE SEQUENCE sqCar START WITH 1;
CREATE SEQUENCE sqRepair START WITH 10 INCREMENT BY 10;

CREATE TABLE cars 
(	idCar INTEGER,
	nameCar VARCHAR2(50),
	dateCar DATE,
    imageCar BLOB,
	CONSTRAINT pkCars PRIMARY KEY (idCar)
);
CREATE TABLE repairs
(	idRepair INTEGER,
	dateRepair DATE NOT NULL,
	textRepair VARCHAR2(50),
	amount NUMBER(10,2),
	idCar INTEGER,
	CONSTRAINT pkRepair PRIMARY KEY (idRepair),
	CONSTRAINT fkRepair FOREIGN KEY (idCar) REFERENCES cars,
	CONSTRAINT ckRepairAmount CHECK (amount BETWEEN 50 AND 2500)
);
CREATE TABLE rents (
	customer varchar(100),
	startDate date,
	endDate date,
	amount number,
	idCar integer,
	constraint pk_rent primary key(idCar, startDate),
	constraint fk_rent_idCar FOREIGN key(idCar) references cars(idcar)
);

INSERT INTO cars VALUES(sqCar.NEXTVAL, 'Isetta', TO_DATE('21.01.2001','DD.MM.YYYY'));
INSERT INTO cars VALUES(sqCar.NEXTVAL, 'Ro 80', TO_DATE('11.12.1978','DD.MM.YYYY'));
INSERT INTO repairs VALUES(sqRepair.NEXTVAL, TO_DATE('11.12.2019 11:30','DD.MM.YYYY HH24:MI'), 'brake front', 200.99, 1);
INSERT INTO repairs VALUES(sqRepair.NEXTVAL, TO_DATE('13.12.2019 10:45','DD.MM.YYYY HH24:MI'), 'service 120k', 400.99, 1);
INSERT INTO repairs VALUES(sqRepair.NEXTVAL, TO_DATE('11.01.2020 11:30','DD.MM.YYYY HH24:MI'), 'screen',  2300.99, 2);
INSERT INTO repairs VALUES(sqRepair.NEXTVAL, TO_DATE('13.03.2020 10:15','DD.MM.YYYY HH24:MI'), 'service 140k',  1200.99, 2);
INSERT INTO rents VALUES('Karl', to_date('12.03.2020', 'DD.MM.YYYY'), to_date('12.03.2021', 'DD.MM.YYYY'), 100, 1);
INSERT INTO rents VALUES('Siri', to_date('06.12.2021', 'DD.MM.YYYY'), to_date('18.12.2021', 'DD.MM.YYYY'), 50, 2);
INSERT INTO rents VALUES('Siri', to_date('06.12.2022', 'DD.MM.YYYY'), to_date('18.12.2022', 'DD.MM.YYYY'), 50, 2);
INSERT INTO rents VALUES('Siri', to_date('06.11.2021', 'DD.MM.YYYY'), to_date('18.12.2021', 'DD.MM.YYYY'), 50, 2);
INSERT INTO rents VALUES('Karl', to_date('03.11.2021', 'DD.MM.YYYY'), to_date('13.12.2021', 'DD.MM.YYYY'), 50, 2);

COMMIT;

------------------------------------------------------------------------------

SELECT * FROM cars;
SELECT * FROM repairs;
SELECT * FROM rents;

select customer, startdate, enddate, amount, idcar from rents where idCar = 2;

select * from rents;
delete from rents where startDate = to_date('03.11.2021', 'DD.MM.YYYY');
rollback;
select customer, startDate, endRent, amount, idCar from rents;


select startDate, endDate from rents
where sD between startDate and endDate
OR eD between startDate and endDate
OR startDate between sD and eD;

UPDATE cars SET imageCar = NULL WHERE idCar = 1;

select idCar, nameCar, dateCar from cars where idcar = (select max(idcar) from cars);

select max(idRepair) from repairs;

select * from cars;

select idrepair, daterepair, textrepair, amount, idcar from repairs;
select idrepair, daterepair, textrepair, amount, idcar from repairs where idCar = 1;