Il comando **UPDATE** permette di aggiornare alcuni dati
```sql
UPDATE Tabella  
SET Attributo = Espr 
WHERE Condizione
```

## Esempi
### Esempio 1
Dalla tabella Aule modificare il numero dell’aula da 3 a 7.
```sql
UPDATE Aule 
SET Aula = 7 
WHERE Aula = 3
```
### Esempio 2
Modificare il valore del reddito delle persone più giovani di 30 anni attribuendo loro un aumento del 10%.
```sql
UPDATE Persone  
SET Reddito = Reddito * 1.1 
WHERE Eta < 30
```