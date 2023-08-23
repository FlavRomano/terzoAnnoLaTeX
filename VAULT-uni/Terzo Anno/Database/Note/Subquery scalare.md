La **subquery scalare** è un comando `SELECT` che **restituisce un solo valore** (da qui il nome *scalare*).
## Esempio
```sql
SELECT MAX(Cilindrata)
FROM Veicoli
--
SELECT Cod_Categoria 
FROM Veicoli 
WHERE Targa=“123456”
```
