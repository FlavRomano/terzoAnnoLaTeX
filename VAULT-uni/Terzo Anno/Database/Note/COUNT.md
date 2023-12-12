Restituisce il numero di righe della tabella o il numero di valori di un particolare attributo. 
- Con `(*)` conta tutte le righe selezionate 
- con `ALL` conta tutti i valori **non nulli** delle righe selezionate
	- opzione di default
- con `DISTINCT` conta tutti i valori **non nulli distinti** delle righe selezionate. 

## Esempi 

### Esempio 1
Numero di figli di Franco

```sql
SELECT count(*) as NumFigliDiFranco 
FROM Paternita  
WHERE Padre = "Franco"
```

L'operatore `count(*)` viene applicato al risultato della query:

```sql
SELECT * FROM Paternita  
WHERE Padre = "Franco"
```

![[Pasted image 20230822171205.png]]

### Esempio 2
![[Pasted image 20230822171325.png]]

Contare il numero di studenti iscritti al corso di BD e di Laboratorio di Basi di Dati:

```sql
SELECT count(*) as “NumStud” FROM EsamiBD
```

Contare il numero di esami di BD superati positivamente:

```sql
SELECT count(ALL BD) “ContaBD” FROM EsamiBD
```

| ContaBD |
| ------- |
| 3       |

Numero di voti distinti dati all’esame di LBD:

```sql
SELECT count(distinct LBD) “ContDistLBD” FROM EsamiBD
```

| ContaDistLBD |
| ------------ |
| 2            | 