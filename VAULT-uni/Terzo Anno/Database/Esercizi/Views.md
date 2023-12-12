## Creare una vista
Supponiamo di avere il seguente schema ddl:

```sql
create table Studente(
	matricola int primary key,
	nome varchar(32),
	cognome varchar(32),
	dob Date
);

create table Materie(
	codice int primary key,
	cdl varchar(7),
	materia varchar(32)
);

create table Esami(
	codice_materia foreign key references Materie(codice),
	matricola foreign key references Studente(matricola),
	voto int check (voto between 18 and 31)
);

insert into Studente
values
	(1, 'Flavio', 'Romano', '2002-02-04'),
	(2, 'Mario', 'Rossi', '2001-04-11'),
	(3, 'Giorgio', 'Bruni', '1999-07-09');

insert into Materie
values
	(101, 'INF-L31', 'Basi di dati'),
	(105, 'INF-L31', 'Reti'),
	(107, 'INF-L31', 'Webapp'),
	(108, 'MAT-L01', 'Algebra'),
	(109, 'MAT-L01', 'Geometria');

insert into Esami
values
	(101, 1, 28),
	(105, 1, 25),
	(107, 1, 24),
	(108, 2, 18),
	(109, 3, 22),
	(108, 3, 19);
```

vogliamo creare una vista per visualizzare gli studenti e i rispettivi esami sostenuti:

```sql
create view StudentiEsami
as
	select
	  Studente.*, Materie.*, Esami.voto 
	from
	  Studente, Esami, Materie
	where
	  Studente.matricola = Esami.matricola
	  and
	  Esami.codice_materia = Materie.codice;
```

così possiamo facilmente visualizzare la query con una semplice 
```sql
select
  *
from
  StudentiEsami
```