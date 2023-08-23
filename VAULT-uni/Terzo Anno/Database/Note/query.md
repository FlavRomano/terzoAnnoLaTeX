I comandi di query permettono di 
- effettuare una ricerca dei dati presenti nel database
- che soddisfano particolari condizioni richieste dall'utente

```sql
SELECT s.Nome, e.Data
FROM Studenti s, Esami e
WHERE e.Materia = "BD" 
	AND e.Voto = 30 
	AND e.Matricola = s.Matricola
```