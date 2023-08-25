In SQL una vista può essere definita attraverso una [[6. DML e DDL#Definizione delle tabelle|CREATE]] seguito dalla keyword `VIEW`
```sql
CREATE VIEW nomeVista [(nomeAttributo1, nomeAttributo2, ...)] 
AS SELECT nomeColonna1{, nomeColonna2, ...} [with (local/cascaded) check option]
```

- i **nomi delle colonne** sono i nomi assegnati alle colonne della vista, che **corrispondono ordinatamente** alle **colonne elencate nella select**
	- se omettiamo questa lista di nomi, la vista avrà gli **stessi nomi delle colonne della tabella a cui si riferisce**.

## Eliminare una vista
Una vista si elimina col comando **DROP VIEW**

```sql
DROP VIEW nomeVista {Restrict/Cascade}
```

Abbiamo due diverse specifiche da poter aggiungere:
- con **RESTRICT** la vista viene eliminata **solo se non è riferita** nella definizione di altri oggetti.
- con **CASCADE** vengono eliminate sia la vista sia tutte le dipendenze **da tale vista** di altre definizioni dello schema

### Esempio
```sql
CREATE VIEW FabbricaVersioni (CodiceFabbrica, NumeroDiVersioni)
AS SELECT Cod_Fab, SUM(Num_Versioni)
FROM Modelli 
GROUP BY Cod_Fab
---
CREATE VIEW VistaBis
AS SELECT * FROM FabbricaVersioni -- vista che dipende da una vista
```
1. L'istruzione `DROP VIEW FabbricaVersioni CASCADE` elimina:
	- la vista `FabbricaVersioni`
	- la vista `VistaBis`, perché dipendeva da `FabbricaVersioni`
2. L'istruzione `DROP VIEW FabbricaVersioni RESTRICT` **impedisce la cancellazione di FabbricaVersioni**
	- finché è presente `VistaBis`, la vista `FabbricaVersioni` non può essere eliminata perché ha una dipendenza.

## Opzioni di modifica
### With Check Option
La clausola **With Check Option**, messa alla fine della definizione, garantisce
- integrità dei dati quando si eseguono operazioni di modifica (tramite una vista)
- si assicura che le operazioni di modifica soddisfino la clausola `WHERE` della [[5. SQL#Subquery|subquery]].

Basti vedere [[Vista#Esempi modifiche#Esempio 2|questo esempio]].

Assieme a questa clausola è possibile specificare **LOCAL** oppure **CASCACED**:
- se si usa **LOCAL** il controllo delle condizioni viene applicato **solo alla vista immediatamente coinvolta** nell'operazione di modifica
	- il controllo delle condizioni è limitato alla vista attraverso la quale è stata effettuata l'operazione di modifica (o inserimento).
- se si usa **CASCADED** il controllo delle condizioni viene applicato **sia alla vista coinvolta** dall'operazione di modifica **sia a tutte le viste sottostanti coinvolte nella gerarchia di viste**
	- tutte le viste di gruppo relative alla vista coinvolta devono poter soddisfare la condizione di controllo.
	- questa è l'opzione di default.

## Esempi
### Esempio 1
Creare una vista contenente: la matricola, il nome, il cognome, lo stipendio degli impiegati del dipartimento di Amministrazione il cui stipendio è maggiore di 1000 euro.

```sql
CRETE VIEW ImpiegatiAmministrazione (Matricola, Nome, Cognome, Stipendio)
AS SELECT Matr, Nome, Cognome, Stip
FROM Impiegato
WHERE Stip > 1000 and Dipartimento = "Amministrazione"
```

### Esempio 2
Creare una vista che contiene la targa e la cilindrata delle macchine con cilindrata $<1500$
- Veicoli(<u>Targa</u>, Cod_mod, Cod_cat, Cilindrata, Cod_comb, cav.Fisc, Velocità, Posti, Imm)

```SQL
CREATE VIEW PiccolaCilindrata (PC_Targa, PC_Cilindrata)
AS SELECT Targa, Cilindrata
FROM Veicoli
WHERE Cilindrata < 1500
```
### Esempio 3
Creare una vista che descrive la targa, il codice del modello e il nome della categoria dei veicoli.
- Categorie(<u>Cod_cat</u>, Nome_cat)
- Veicoli(<u>Targa</u>, Cod_mod, Cod_cat\*, Cilindrata, Cod_comb, cav.Fisc, Velocità, Posti, Imm)

```sql
CREATE VIEW CategorieCliente (Targa, CodiceModello, NomeCategoria)
AS SELECT Targa, Cod_Mod, Nome_cat
FROM Veicoli, Categorie
WHERE Veicoli.Cod_cat = Categorie.Cod_cat
```

### Esempio 4
Creare una vista che per ciascuna fabbrica riporti il numero globale delle versioni dei modelli prodotti
- Modelli(<u>Cod_Mod</u>, Nome_Mod, Cod_Fab, Cilindrata_Media, Num_Versioni)

```sql
CREATE VIEW FabbricaVersioni (CodiceFabbrica, NumeroDiVersioni)
AS SELECT Cod_Fab, SUM(Num_Versioni)
FROM Modelli 
GROUP BY Cod_Fab
```
È una vista di gruppo anche una vista che è definita in base ad una vista di gruppo: 
```sql
CREATE VIEW VistaBis
AS SELECT * FROM FabbricaVersioni -- (CodiceFabbrica, NumeroDiVersioni)
```

### Esempio 5
Calcolare la media del numero degli uffici distinti presenti in ogni dipartimento
```sql
CREATE view UfficiDipart (NomeDip, NroUffici) 
AS SELECT Dipart, COUNT(DISTINCT Ufficio)  
FROM Impiegato  
GROUP BY Dipart

SELECT AVG(NroUffici)
FROM UfficiDipart
```

invece **l'errore più grande al mondo dopo le due grandi guerre** sarebbe stata la seguente query:
```sql
SELECT AVG(COUNT(DISTINCT Ufficio)) 
FROM Impiegato  
GROUP BY Dipart
```
perché stiamo utilizzando **due operatori aggregati annidati**, impossibile solo a pensarlo.

## Esempi modifiche
### Esempio 1
Il personale della segreteria non può accedere ai dati sullo stipendio ma può modificare gli altri campi della tabella, aggiungere e/o cancellare tuple
- Impiegato( Nome, Cognome, Dipart, Ufficio, Stipendio)

```sql
CREATE VIEW Impiegato_viewSegreteria
AS SELECT Nome, Cognome, Dipart, Ufficio
FROM IMPIEGATO
INSERT INTO Impiegato_viewSegreteria VALUES (...)
```
Possono avvenire due cose:
1. Stipendio verrà inizializzato a `NULL`, tutto ok.
2. Se `STIPENDIO NOT NULL` allora l'operazione fallirà

### Esempio 2
Data la seguente vista
```sql
CREATE VIEW ImpiegatoRossi
AS SELECT *  
FROM Impiegato 
WHERE Cognome="Rossi" -- importante
```
Ha senso fare un'operazione del tipo
```sql
INSERT INTO ImpiegatoRossi ("Valentino", "Rossi", "Moto", 46)
```
Tuttavia è permesso fare
```sql
INSERT INTO ImpiegatoRossi ("Max", "Biagi", "Moto", 3)
```
ma finisce nella tabella di base, **non è visibile dalla vista** `ImpiegatoRossi`

Se avessimo aggiunto alla definizione della vista **With Check Option"** in questo modo:
```sql
CREATE VIEW ImpiegatoRossi
AS SELECT *  
FROM Impiegato 
WHERE Cognome="Rossi" 
WITH CHECK OPTION
```
l'inserimento di `Max Biagi` sarebbe fallito, mentre quello di `Rossi` sarebbe andato a buon fine.

### Esempio 3
- Categorie(<u>Cod_cat</u>, Nome_cat)
- Veicoli(<u>Targa</u>, Cod_mod, Cod_cat\*, Cilindrata, Cod_comb, cav.Fisc, Velocità, Posti, Imm)

La seguente vista è aggiornabile:
```sql
CREATE VIEW Vista1 (PC_Targa, PC_Cilindrata)
AS SELECT Targa, Cilindrata
FROM Veicoli
WHERE Cilindrata < 1500
```
mentre questa non lo è, perché coinvolge due tabelle
```sql
CREATE VIEW Vista2 (Targa, CodiceModello, NomeCategoria)
AS SELECT Targa, Cod_Mod, Nome_cat
FROM Veicoli, Categorie -- coinvolge due tabelle <- IMPORTANTE
WHERE Veicoli.Cod_cat = Categorie.Cod_cat
```
## L'interrogazione non standard
Il dipartimento che impiega il massimo budget in stipendi dei dipendenti.
### Soluzione senza vista
```sql
SELECT Dipartimento 
FROM Impiegati 
GROUP BY Dipartimento  
HAVING SUM(Stipendio) >= all (
	SELECT SUM(Stipendio) 
	FROM Impiegati 
	GROUP BY Dipartimento
)
```

- La clausola [[HAVING]] viene utilizzata per filtrare i risultati dei gruppi in base a una condizione. 
	- In questo caso, la condizione è che la somma degli stipendi (`SUM(Stipendio)`) per ciascun dipartimento deve essere maggiore o uguale **a tutti i totali degli stipendi dei dipartimenti restituiti dalla sottoquery**.
- La sottoquery calcola la somma degli stipendi **per ciascun dipartimento** nella tabella "Impiegati", raggruppando per dipartimento. 
	- Questo risultato viene utilizzato nella clausola `HAVING` della query principale.
	- È una [[Subquery scalare|subquery scalare]].

### Soluzione con vista
```sql
CREATE VIEW BudgetStipendi(Dipartimento,TotaleStipendi) 
AS SELECT Dipartimento, SUM(Stipendio)  
FROM Impiegato  
GROUP BY Dipartimento

SELECT Dipartimento 
FROM BudgetStipendi 
WHERE TotaleStipendi = (
	SELECT MAX(TotaleStipendi) 
	FROM BudgetStipendi
)
```
- La vista `BudgetStipendi` avrà due colonne: `Dipartimento` e `TotaleStipendi`. 
	- La vista è costruita selezionando i dati dalla tabella `Impiegato`. 
	- La clausola `GROUP BY Dipartimento` raggruppa i dati per dipartimento e quindi la funzione di aggregazione `SUM(Stipendio)` calcola **il totale degli stipendi per ciascun dipartimento**.
- Invece dopo eseguiamo una query sulla vista `BudgetStipendi` per selezionare il dipartimento o i dipartimenti **con il totale degli stipendi massimo**. 
	- La clausola `WHERE` filtra i risultati in base alla condizione che il `TotaleStipendi` nella vista sia uguale al massimo valore di `TotaleStipendi` restituito dalla subquery.