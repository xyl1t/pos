select * from cars;

drop table rent cascade constraints;

create table rent (
    customer varchar(100),
    startDate date,
    endDate date,
    amount number,
    idCar integer,
    constraint pk_rent primary key(idCar, startDate),
    constraint fk_rent_idCar FOREIGN key(idCar) references cars(idcar)
);

insert into rent values(
	'Karl',
	to_date('12.03.2020', 'DD.MM.YYYY'),
	to_date('12.03.2021', 'DD.MM.YYYY'),
	100,
	1
);

insert into rent values(
	'Siri',
	to_date('06.12.2021', 'DD.MM.YYYY'),
	to_date('18.12.2021', 'DD.MM.YYYY'),
	50,
	2
);

insert into rent values(
	'Siri',
	to_date('06.12.2022', 'DD.MM.YYYY'),
	to_date('18.12.2022', 'DD.MM.YYYY'),
	50,
	2
);

insert into rent values(
	'Siri',
	to_date('06.11.2021', 'DD.MM.YYYY'),
	to_date('18.12.2021', 'DD.MM.YYYY'),
	50,
	2
);

insert into rent values(
	'Karl',
	to_date('03.11.2021', 'DD.MM.YYYY'),
	to_date('13.12.2021', 'DD.MM.YYYY'),
	50,
	2
);

commit;

select * from rent;
select customer, startdate, enddate, amount, idcar from rent where idCar = 2;

select * from rent;
delete from rent where startDate = to_date('03.11.2021', 'DD.MM.YYYY');
rollback;
select customer, startDate, endRent, amount, idCar from rent;


select startDate, endDate from rent
where sD between startDate and endDate
OR eD between startDate and endDate
OR startDate between sD and eD;

