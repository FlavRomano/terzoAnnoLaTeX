Con il comando **ALTER TABLE** è possibile:
- **aggiungere** una **colonna** (`ADD Colonna`)
- **eliminare** una **colonna** (`DROP Colonna`)
- **modificare** una **colonna** (`MODIFY`)
- **aggiungere** un'assegnazione di valore **default** (`SET DEFAULT`)
	- **eliminare** un'assegnazione di valore **default** (`DROP DEFAULT`)
- **aggiungere** vincoli di tabella (`ADD CONSTRAINT`)
	- **eliminare** vincoli di tabella (`DROP CONSTRAINT`)
- and so on

## Aggiungere una colonna
In generale la keyword *COLUMN* può essere omessa

```sql
ALTER TABLE nomeTabella
ADD COLUMN nomeColonna tipoColonna defaultColonna vincoloColonna
```

in mancanza di specifiche, questa colonna **viene inserita in fondo allo schema**; altrimenti

```sql
ALTER TABLE nomeTabella
ADD COLUMN nomeColonna tipoColonna defaultColonna vincoloColonna FIRST[/AFTER] nomeColonnaK
```

In generale si può aggiungere una colonna in qualsiasi momento **se non viene specificato NOT NULL**, altrimenti lo si può fare **in tre passaggi**

## Eliminare una colonna
```sql
ALTER TABLE nomeTabella
DROP COLUMN nomeColonna RESTRICT -- oppure CASCADE
```

In SQL abbiamo le opzioni **RESTRICT** e **CASCADE**, se ne deve scegliere per forza una.
- con **RESTRICT** se in un'altra tabella si ha un [[vincolo di integrità referenziale]] con la colonna che si sta eliminando, l'esecuzione del comando per eliminare la colonna **fallisce**.
	- impedisce di eseguire l'azione se ciò potrebbe causare problemi di coerenza nei dati
- con **CASCADE** eliminando la colonna, **tutte le dipendenze logiche e vincoli** associate ad essa verranno eliminati.
	- eseguirà una *cascata* di eliminazioni, in cui tutte le dipendenze vengono rimosse insieme alla colonna target

## Modificare una colonna
Se si vogliono modificare le caratteristiche di una colonna dopo averla definita, occorre eseguire
```sql
ALTER TABLE nomeTabella
MODIFY nomeColonna tipoColonna defaultColonna vincoliColonna
```

## Assegnare un valore di default a una colonna (o eliminarlo)
Attraverso una `ALTER COLUMN` e un `SET DEFAULT`
```sql
ALTER TABLE nomeTabella
ALTER COLUMN nomeColonna 
SET DEFAULT valoreDefault
```

lo si può eliminare invece con una `DROP` al posto del `SET`

```sql
ALTER TABLE nomeTabella
ALTER COLUMN nomeColonna 
SET DEFAULT 
```

## Aggiungere vincoli di tabella (o eliminarlo)
Se si vuole aggiungere un vincolo di tabella si esegue il comando 

```sql
ALTER TABLE nomeTabella
ADD CONSTRAINT nomeVincolo VincoloDiTabella
```

- `nomeVincolo` sarà l'identificatore assegnato al vincolo
- `VincoloDiTabella` può essere `PRIMARY KEY`, `UNIQUE`, `CHECK`...

Se si vuole eliminare un vincolo di tabella si esegue il comando 

```sql
ALTER TABLE nomeTabella
DROP CONSTRAINT nomeVincolo RESTRICT -- o cascade
```

## Esempi
### Esempio 1
Aggiungere alla tabella Impiegato la colonna `nomecapo`.
```sql
ALTER TABLE Impiegato
ADD nomeCapo varchar(20) default "Rossi" NOT NULL
```

### Esempio 2
Supponendo che nella tabella Impiegato ci sia una colonna `nome` definita come `varchar(20`), modificarla in modo che diventi un `varchar(30)` e sia definito su di essa il vincolo `not null`.

```sql
ALTER TABLE Impiegato
MODIFY nome varchar(30) NOT NULL
```

### Esempio 3
Imporre il valore di default `Direzione Generale` ai valori della colonna `Dipart` in cui tale valore non è assegnato esplicitamente

```sql
ALTER TABLE Impiegato  
ALTER COLUMN Dipart  
SET DEFAULT "Direzione Generale"
```

### Esempio 4
Nella tabella `Impiegato`, aggiungere un vincolo di unicità alla coppia `(nome, cognome)`
```sql
ALTER TABLE Impiegato
ADD CONSTRAINT unique_Impiegato UNIQUE(nome, cognome)
```

### Esempio 5
- Nella tabella `Info_Personali`, aggiungere un vincolo di chiave primaria su `Id_impiegato`
```sql
ALTER TABLE Info_Personali
ADD CONSTRAINT pk_Impiegato PRIMARY KEY(Impiegato)
```
- Nella tabella `Info_Personali`, aggiungere un vincolo di chiave esterna su `Id_impiegato` che si riferisca alla chiave primaria `Id_impiegato`

```sql
ALTER TABLE Info_Personali
ADD CONSTRAINT fk_Impiegato FOREIGN KEY(Id_impiegato) REFERENCES Impiegati (Id_impiegato)
```
- Nella tabella `Info_Personali`, aggiungere un vincolo di unicità 
```sql
ALTER TABLE Info_Personali
ADD CONSTRAINT unique_cf UNIQUE(codice_fiscale)
```
- Nella tabella `Info_Personali`, aggiungere un vincolo check
```sql
ALTER TABLE Info_Personali
ADD CONSTRAINT check_stip CHECK (stipendio > 0)
```