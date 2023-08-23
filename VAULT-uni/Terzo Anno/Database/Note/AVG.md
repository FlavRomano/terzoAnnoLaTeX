La funzione `AVG` calcola la **media** (average) dei valori non nulli di una colonna.  
- `ALL` permette di calcolare la media fra tutti i valori non nulli
	- di default
- `DISTINCT` permette di calcolare la media fra tutti i valori distinti

## Esempio
Calcolare la media degli stipendi degli impiegati del dipartimento di Produzione e che hanno meno di 30 anni

```sql
SELECT AVG(stipendi)  
FROM Impiegati  
WHERE Dipart=‘Produzione’ AND eta<30
```