
drop table repairs cascade constraints;
drop sequence seq_repair;

create sequence seq_repair
start with 1
increment by 1;

create table repairs (
	id integer,
	idCar integer,
	repairDate date,
	text varchar(1024),
	amount decimal,
	constraint pk_repairs primary key(id),
	constraint fk_repairs_idCar FOREIGN key(idCar) references cars(idcar),
	constraint ckrepairamount check (amount between 50 and 2500)
);

select sum(amount) from repairs where idCar = 1;
select sum(amount) from rent where idCar = 3;

select id, idCar, repairDate, text, amount from repairs;

update repairs
set text = 'test'
where id = 1;

insert into repairs values(
	seq_repair.nextval,
	1,
	to_date('1.1.2022', 'DD.MM.YYYY'),
	'description0',
	60
);

insert into repairs values(
	seq_repair.nextval,
	1,
	to_date('2.1.2022', 'DD.MM.YYYY'),
	'description1',
	70
);

insert into repairs values(
	seq_repair.nextval,
	2,
	to_date('3.1.2022', 'DD.MM.YYYY'),
	'description2',
	80
);

insert into repairs values(
	seq_repair.nextval,
	2,
	to_date('4.1.2022', 'DD.MM.YYYY'),
	'description3',
	90
);

select * from repairs;

delete from repairs where id = 1;

