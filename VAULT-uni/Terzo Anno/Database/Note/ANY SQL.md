L'operatore **ANY** restituisce un **valore booleano come risultato** (quindi è uno *scalare*)
- restituisce **VERO** se **QUALSIASI** dei valori della [[5. SQL#Subquery|subquery]] soddisfa la condizione
- **FALSO** altrimenti

ANY significa che la condizione **sarà vera** se l'operazione è vera **per uno qualsiasi dei valori dell'intervallo**.

## Esempio
Elenca il nome del prodotto se trova QUALSIASI record nella tabella Ordini con una quantità pari a 10 (questo restituirà VERO perché la colonna Quantità ha alcuni valori di 10):

```sql
SELECT Nome  
FROM Prodotti  
WHERE IdProdotto = ANY (
	SELECT IdProdotto
	FROM Ordini  
	WHERE Quantità = 10
)
```