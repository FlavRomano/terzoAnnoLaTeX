L'operatore **IN** viene utilizzato per **specificare** l'elenco di valori o la [[5. SQL#Subquery|subquery]] nella clausola **WHERE**. 
- Una [[5. SQL#Subquery|subquery]] o un elenco di valori deve essere specificato tra le parentesi

```sql
SELECT listaAttributi  
FROM Tab1
WHERE Col1 in (
	SELECT --subquery
);
```

## Esempio
### Esempio 1
Data la tabella Impiegati, trova 
- Id impiegato 
- Nome
- Cognome 
- stipendio 

degli impiegati con ID 1, 3, 5 e 6 se presenti.

```sql
SELECT EmpId, FirstName, LastName, Salary
FROM Employee
WHERE EmpId IN (1, 3, 5, 6)
```

### Esempio 2
Data la tabella Impiegati e Dipartimenti, trova 
- Id impiegato 
- Nome
- Cognome 
- Id dipartimento

degli impiegati che lavorano nei dipartimenti con Id maggiore di 2 (se presenti)
```sql
SELECT EmpId, FirstName, LastName, DeptId
FROM Employee
WHERE DeptId IN (
	SELECT DeptId 
	FROM Department 
	WHERE DeptId > 2
)
```