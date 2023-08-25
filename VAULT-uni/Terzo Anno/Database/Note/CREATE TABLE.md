L'istruzione **CREATE TABLE** 
- definisce uno **schema di relazione** e ne crea un'**istanza vuota**
- specifica **attributi**, **domini** e **vincoli**

```sql
CREATE TABLE nome_tabella
(
	nome_colonna1 tipo_colonna1 clausola_default1 vincolo_di_colonna1
	-- ...
	nome_colonnak tipo_colonnak clausola_defaultk vincolo_di_colonnak
	vincoli_di_tabella
)
```
- `CREATE TABLE` e nome che la caratterizza
- lista delle colonne (attributi) con caratteristiche specifiche
- vincoli di tabella

Una volta creata, la tabella è **pronta per l'inserimento dei dati** che dovranno soddisfare i vincoli imposti.

```sql
DESCRIBE Tabella
```

permette di visualizzare lo schema di una tabella già creata.

## Esempi
### Esempio 1
```sql
CREATE TABLE IMPIEGATO 
(
	-- lista delle colonne con tipatura ed eventuali clausole e vincoli
	Matricola CHAR(6) PRIMARY KEY -- vincolo
	Nome CHAR(20) NOT NULL -- vincolo
	Cognome CHAR(20) NOT NULL
	Dipart CHAR(15)
	Stipendio NUMERIC(9) DEFAULT 0 -- vincolo 
	-- vincoli_di_tabella
	FOREIGN KEY(Dipart) REFERENCES Dipartimento(NomeDip)
	UNIQUE (Cognome, Nome)
)
```

### Esempio 2
```sql
CREATE TABLE Impiegati
(
	Codice CHAR(8) NOT NULL,
	Nome CHAR(20),  
	AnnoNascita INTEGER CHECK (AnnoNascita < 2000), 
	Qualifica CHAR(20) DEFAULT ‘Impiegato’, 
	Supervisore CHAR(8),  
	PRIMARY KEY pk_impiegato (Codice),  
	FOREIGN KEY fk_ Impiegati (Supervisore) REFERENCES Impiegati 
)

CREATE TABLE FamiliariACarico
(
	Nome CHAR(20),
	AnnoNascita INTEGER, 
	GradoParentela CHAR(10),
	CapoFamiglia CHAR(8)
	FOREIGN KEY fk_FamiliariACarico (CapoFamiglia) REFERENCES Impiegati
)
```

in questo caso abbiamo
- fk_FamiliariACarico* chiave esterna 
	- che referenzia la chiave primaria di Impiegati, cioè `pk_impiegato` che è fatta dai valori della colonna `Codice` 