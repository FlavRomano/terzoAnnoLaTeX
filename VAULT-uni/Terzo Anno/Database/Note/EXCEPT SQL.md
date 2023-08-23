L’operatore **EXCEPT** utilizza 
- come operandi due tabelle ottenute mediante due `SELECT` 
- ha come risultato una nuova tabella che contiene **tutte le righe della prima che non si trovano nella seconda**.

Realizza la differenza dell’algebra relazionale. Anche qui si può specificare l’opzione ALL per indicare la volontà di mantenere i duplicati.
## Esempio
Impiegati il cui nome non coincide col cognome di qualche altro impiegato
```sql
SELECT Nome  
FROM Impiegato  
EXCEPT  
SELECT Cognome as Nome 
FROM Impiegato
```

equivalente a 

```sql
SELECT Nome
FROM Impiegato
WHERE Nome <> ALL(
	SELECT Cognome
	FROM Impiegato
)
```