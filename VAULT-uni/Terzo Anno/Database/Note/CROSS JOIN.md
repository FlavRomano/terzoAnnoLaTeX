Il cross-join implementa il [[Prodotto cartesiano|prodotto cartesiano]], si realizza mediante 
- una `SELECT` che utilizza le due (o più) tabelle coinvolte (senza specificare nessuna condizione)

```sql
SELECT Categorie.*, Fabbriche.*
FROM Categorie, Fabbriche 
```
