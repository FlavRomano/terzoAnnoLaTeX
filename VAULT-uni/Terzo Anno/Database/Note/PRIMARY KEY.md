Il vincolo **PRIMARY KEY** è simile a [[UNIQUE]] ma
- definisce la [[chiave#Chiave Primaria|chiave primaria]] della relazione
	- cioè un attributo che individua univocamente un dato, **non può essere NULL**

Per questo diciamo che questo vincolo implica
- sia il vincolo [[UNIQUE]]
- sia il vincolo [[NOT NULL]]

Analogamente al vincolo [[UNIQUE]], anche il vincolo di chiave primaria può essere definito **su un insieme di elementi**. In tal caso la sintassi è simile a quella di `UNIQUE`.

Questo vincolo è fondamentale per identificare univocamente i **soggetti del dominio** e permette spesso il collegamento fra due tabelle.

## Esempio
```sql
CREATE TABLE Impiegato(
	Matricola CHAR(6) PRIMARY KEY, -- vincolo di colonna di chiave primaria
	Codice_fiscale CHAR(16) UNIQUE, 
	Nome VARCHAR(20) NOT NULL, 
	Cognome VARCHAR(20) NOT NULL, 
	Dipart VARCHAR(15),
	Stipendio NUMBER(9) DEFAULT 0,  
	FOREIGN KEY(Dipart) REFERENCES Dipartimento(NomeDip), 
	UNIQUE (Cognome,Nome)
)

CREATE TABLE Studente( 
	Nome VARCHAR(20), 
	Cognome VARCHAR(20),
	nascita DATE,  
	Corso_Laurea VARCHAR(15),  
	Facolta VARCHAR (20)  
	PRIMARY KEY(Cognome,Nome,Nascita) -- vincolo di tabella definito su un insieme di attr
)
```