La funzione `SUM` calcola la somma dei valori di una colonna.  
- `ALL` permette di sommare tutti i valori non nulli
	- di default
- `DISTINCT` permette di sommare tutti i valori distinti

## Esempio
Calcolare la somma degli stipendi mensili degli impiegati del settore Produzione.

```sql
SELECT SUM (ALL stipendio) FROM Impiegati  
WHERE Dipart=‘Produzione’
```