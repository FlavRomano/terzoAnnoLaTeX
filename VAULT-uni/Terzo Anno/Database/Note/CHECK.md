Un vincolo di **CHECK** richiede che
- una colonna soddisfi una condizione **per ogni riga della tabella**
- una combinazione di colonne soddisfi una condizione **per ogni riga della tabella**

```sql
CREATE TABLE nomeTabella (
	nomeColonna tipoColonna CHECK(expBool(nomeColonna))
	-- ...
	CHECK(expBool(nomeColonna))
)
```

L'**espressione booleana** viene valutata usando i valori della colonna che vengono inseriti o aggiornati nella riga.
- Se **CHECK** viene espresso come **vincolo di colonna**
	- può coinvolgere **solo l'attributo su cui è definito**
- Se **CHECK** viene espresso come **vincolo di tabella**
	- coinvolge **due o più attributi** su cui è definito

## Esempi
### Esempio 1
Nessuno stipendio degli impiegati può avere valore minore di 0 (espresso come vincolo di riga).
```sql
CREATE TABLE IMPIEGATO (  
	Matricola CHAR(6) PRIMARY KEY,  
	Nome CHAR(20) NOT NULL,  
	Cognome CHAR(20) NOT NULL,  
	Dipart CHAR(15),  
	Stipendio NUMERIC(9) DEFAULT 0 CHECK (Stipendio>=0), -- vincolo di colonna
	FOREIGN KEY(Dipart) REFERENCES Dipartimento(NomeDip), 
	UNIQUE (Cognome,Nome)
)
```
### Esempio 2
Nessuno stipendio degli impiegati può avere valore minore di 0 (espresso come vincolo di tabella).
```sql
CREATE TABLE IMPIEGATO (  
	Matricola CHAR(6) PRIMARY KEY,  
	Nome CHAR(20) NOT NULL,  
	Cognome CHAR(20) NOT NULL,  
	Dipart CHAR(15),  
	Stipendio NUMERIC(9) DEFAULT 0
	CHECK (Stipendio>=0) --
	FOREIGN KEY(Dipart) REFERENCES Dipartimento(NomeDip), 
	UNIQUE (Cognome,Nome)
)
```
### Esempio 3
I dipartimenti si possono trovare solo nelle locazioni di Boston, New York e Dallas.
```sql
CREATE TABLE Dipartimenti (
	dip_cod char(4) primary key,
	dip_nome varchar2(20) not null,
	dip_citta varchar2(15) not null,
	CHECK (dip_citta ="Boston" or dip_citta="New York" or dip_citta="Dallas")
)
```

### Esempio 4
Un vincolo check sullo stipendio e la commissione per evitare che la commissione sia più alta del salario.
```sql
Create table Pagamenti (  
	pag_cod char(6),  
	pag_codicef char(16) REFERENCES dipendenti(dipe_codicef), 
	pag_stipendio number(8,2),  
	pag_commissione number (8,2),  
	CHECK (pag_stipendio>pag_commissione) 
)
```