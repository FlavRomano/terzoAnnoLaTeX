Il comando **DELETE** permette di cancellare una (o più) righe dalla tabella
```sql
DELETE FROM nome_tabella 
[WHERE Condizione]
```

Per eliminare un elemento bisogna individuare quale: 
- mediante la clausola [[5. SQL#WHERE|WHERE]], dove viene stabilita una condizione che individua l’elemento (o gli elementi) da cancellare.  
	- spesso un particolare elemento può essere individuato mediante il suo valore nella chiave primaria.

**Operazione non reversibile**

## Esempi
### Esempio 1
Cancellare dalla tabella `Esami` i dati relativi allo studente il cui numero di matricola è ‘123456’

```sql
DELETE FROM Esami 
WHERE Matricola = ‘123456’
```

Attenzione, come scritto prima, **operazione non reversibile**. Quindi
```sql
DELETE FROM Esami 
```

cancella l'intera tabella `Esami`

### Esempio 2
Eliminare tutte le righe della tabella esami in cui il numero di matricola non si trova nella tabella Studenti.
```sql
DELETE FROM Esami  
WHERE Matricola NOT IN(
	SELECT Matricola 
	FROM Studenti
)
```