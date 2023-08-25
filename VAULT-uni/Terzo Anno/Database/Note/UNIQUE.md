Questo vincolo è utilizzato nella **definizione dell'attributo**
- indica che **non ci possono essere due valori uguali** in quella colonna

è una **chiave della relazione** ma non una [[chiave#Chiave Primaria|chiave primaria]].

Il vincolo di unicità può anche essere riferito a **coppie** o **insiemi di attributi**
- per gli attributi dell'insieme considerato, ogni singolo valore **deve apparire una sola volta**
- non ci devono essere due righe per cui l'insieme di valori corrispondenti a quegli attributi siano uguali.

n.b: Il vincolo `UNIQUE` **non impone NOT NULL**, per questo non è [[chiave#Chiave Primaria|chiave primaria]].

## Esempi
```sql
CREATE TABLE Impiegato(  
	Matricola CHAR(6) PRIMARY KEY,  
	Codice_fiscale CHAR(16) UNIQUE, -- nella definizione dell'attributo
	Nome VARCHAR(20) NOT NULL,  
	Cognome VARCHAR(20) NOT NULL,  
	Dipart VARCHAR(15),  
	Stipendio NUMBER(9) DEFAULT 0,  
	FOREIGN KEY(Dipart) REFERENCES Dipartimento(NomeDip), 
	UNIQUE (Cognome,Nome) -- riferito a coppie o insieme di attributi
)
```