## Cos'è?
Se due tabelle contengono dei **dati in comune**, allora possono essere correlate mediante una [[Join|join]].

Le **colonne** delle due tabelle che creano la **correlazione** rappresentano la **stessa entità**, i loro valori appartengono cioè allo **stesso dominio**

In genere le colonne delle due tabelle considerate sono **legate da un vincolo di chiave esterna** (ma non è obbligatorio).

## Implementazione in SQL
Per essere specifici vogliamo implementare l'[[Theta-join|equi-join]], fra due tabelle ne generiamo una terza:
- le cui **righe** sono **tutte e sole** quelle ottenute dal **prodotto cartesiano** delle  due tabelle di partenza
	- i cui **valori delle colonne** espresse dalla condizione del join sono uguali.

All'interno della `SELECT`:
- nella `FROM` vengono indicate le due tabelle coinvolte
- nella `WHERE` viene espresso il collegamento fra le due tabelle mediante la **condizione di join**

Siano `Tab1(A1, A2)` e `Tab2(A3, A4)` due relazioni

```sql
SELECT  Tab1.A1, Tab2.A4
FROM Tab1, Tab2
WHERE Tab1.A2 = Tab2.A3
```

![[Pasted image 20230823105229.png]]

che in algebra relazionale sarebbe
$$\pi_{A1, A4}(\sigma_{A2=A3}(Tab1 \bowtie Tab2) )$$ 
Quindi la join consiste di 
- un [[Prodotto cartesiano|prodotto cartesiano]] **FROM**
- una [[Selezione|selezione]] **WHERE**
- una [[Proiezione|proiezione]] **SELECT**

## Clausola JOIN
Lo standard ANSI prevede una clausola esplicita per effettuare il join di due tabelle

```sql
SELECT Attributi
FROM Tab1 
JOIN Tab2 ON CondizioneJoin
```

### Esempio
Selezionare per ciascun veicolo la descrizione della relativa categoria utilizzando l’operatore JOIN.
- Categorie(<u>Cod_cat</u>, Nome_cat)
- Veicoli(<u>Targa</u>, Cod_mod, Categoria\*, Cilindrata, Cod_comb, cav.Fis, Velocità, Posti, Imm)

```sql
SELECT Targa, Veicoli.Categoria, Nome_cat
FROM Categorie
JOIN Veicoli on Categorie.Cod_cat = Veicoli.Categoria
```

## Clausola EQUI-JOIN e NATURAL-JOIN
Se vogliamo eseguire una [[Theta-join|equi-join]] che sia anche una [[Join#Join naturale|join naturale]] (creato **su tutte le colonne** che hanno **il medesimo nome** su entrambe le tabelle) abbiamo una clausola standard in SQL:

```sql
SELECT listaAttributi
FROM Tab1 NATURAL JOIN Tab2
```

### Esempio
Che succede se facciamo il natural join fra `Deputati` e `Commissioni`?
- DEPUTATI(<u>Codice</u>, Cognome, Nome, CodCommissione\*, Provincia\*, Collegio\*)
- COMMISSIONI(<u>CodCommissione</u>, Nome, Presidente)

```sql
SELECT *
FROM Deputati
NATURAL JOIN Commissioni
```

Le tabelle coinvolte sono `Deputati` e `Commissioni`. Entrambe le tabelle hanno una colonna chiamata `CodCommissione`. Pertanto, il `NATURAL JOIN` verrà eseguito in base a questa colonna, in quanto è l'unica colonna con lo stesso nome in entrambe le tabelle. Il risultato includerà tutte le colonne di entrambe le tabelle solo per le righe in cui il valore di `CodCommissione` è uguale in ambedue le tabelle.

Questa query è equivalente alla Join implicita:
```sql
SELECT * 
FROM Deputati, Commissioni 
WHERE Deputati.CodCommissione = Commissioni.CodCommissione
```

## Join su più tabelle
Talvolta un'interrogazione può coinvolgere più di due tabelle

### Esempio
Nome della fabbrica del modello dell’automobile con targa W34534R
- Fabbriche(IdFabbrica, Nome)
- Modelli(Cod_mod, Nome, IdFabbrica\*, NumVers)
- Veicoli(Targa, Cod_mod\*, Cod_cat, Cilindrata, Cod_comb, cav.Fisc, Velocità, Posti, Imm)

```sql
SELECT
FROM Fabbriche, Modelli, Veicoli
WHERE Fabbriche.IdFabbrica = Modelli.IdFabbrica and Modelli.Cod_mod = Veicoli.Cod_mod
	and Veicoli.targa = "W34534R"
```

## Esempi
### Esempio 1
Supponiamo che nel registro automobilistico siano presenti le seguenti tabelle:
- Categorie(<u>Cod_cat</u>, Nome_cat)
- Veicoli(<u>Targa</u>, Cod_mod, Categoria\*, Cilindrata, Cod_comb, cav.Fis, Velocità, Posti, Imm)

Vogliamo selezionare per ciascun veicolo la descrizione della relativa categoria.
Le due tabelle sono legate da un vincolo di chiave esterna tra gli attributi `Cod_cat` (in Categorie) e `Categoria` (in Veicoli)

```sql
SELECT Targa, Veicoli.Categoria, Nome_cat
FROM Categorie , Veicoli 
WHERE Veicoli.Categoria = Categorie.Cod_Cat
```

![[Pasted image 20230823110159.png]]

### Esempio 2
Elencare i padri di persone che guadagnano più di 2000 euro al mese (reddito/12)
- Maternità(Madre, Figlio)
- Paternità(Padre, Figlio)
- Persone(Nome, Eta, Reddito)

```sql
SELECT distinct Padre
FROM Paternità, Persone
WHERE Paternità.Figlio = Nome AND Reddito/12 > 2000
```