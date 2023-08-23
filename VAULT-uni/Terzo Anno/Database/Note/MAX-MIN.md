Le funzioni `MAX` e `MIN` calcolano rispettivamente 
- il **maggiore** e il **minore** degli elementi di una colonna. 

L’argomento delle funzioni `max` e `min` può anche essere **un’espressione aritmetica**.

## Esempio
L’età della persona più anziana nella tabella persone
```sql
SELECT max(eta) FROM Persone
```

Il più basso dei voti assegnati all’esame di BD
```sql
SELECT min(BD) FROM EsamiBD
```