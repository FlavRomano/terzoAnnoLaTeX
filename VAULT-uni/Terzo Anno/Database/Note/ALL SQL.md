L'operatore `ALL` restituisce un **valore booleano** come risultato (cioè uno *scalare*)
- restituisce **VERO** se **TUTTI** i **valori** della [[5. SQL#Subquery|subquery]] **soddisfano la condizione**

viene utilizzato con le istruzioni [[5. SQL#SELECT|SELECT]], [[5. SQL#WHERE|WHERE]] e [[HAVING]]

ALL significa che **la condizione sarà vera** solo se **l'operazione è vera per tutti i valori dell'intervallo**.

## Esempio
Trova il nome del prodotto 
- se **TUTTI** i record della tabella Ordini hanno una quantità pari a 10. 

```sql
SELECT Nome  
FROM Prodotti  
WHERE IdProdotto = ALL (
	SELECT IdProdotto  
	FROM Ordini  
	WHERE Quantità = 10
)
```

| IdDettagli | IdOrdine | IdProdotto | Quantità |
| ---------- | -------- | ---------- | -------- |
| 1          | 10248    | 11         | 12       |
| 2          | 10248    | 42         | 10       |
| 3          | 10248    | 72         | 5        |
| 4          | 10249    | 14         | 9        |
| 5          | 10249    | 51         | 40       |
| 6          | 10250    | 41         | 10       |
| 7          | 10250    | 51         | 35       |
| 8          | 10250    | 65         | 15       |
| 9          | 10251    | 22         | 6        |
| 10         | 10251    | 57         | 15       |

Ovviamente restituirà FALSO perché la colonna Quantità ha molti valori diversi (non solo il valore 10).
