`LIKE` è usato per **effettuare ricerche wildcard** (con un placeholder) di una stringa di valori. Abbiamo due tipi di placeholder:
1. `%` denota zero o più caratteri
2. `_` denota un carattere

Il simbolo di escape è spesso `#`, lo si assegna con `ESCAPE "#"` dopo l'espressione in cui lo si è utilizzato.

## Esempi
- Data la tabella Persone, determinare quelle il cui nome termina per "a".

![[Pasted image 20230822163112.png]]

```sql
SELECT *  
FROM Persone  
Where nome LIKE ‘%a’
```

- Selezionare le sequenze di DNA in cui ci sono almeno due simboli ‘G’ che distano 3 caratteri.

```sql
SELECT *  
FROM DNA  
WHERE Sequenza LIKE ‘%G___G%’
```

- Modelli il cui nome inizia per `C_F`

```sql
SELECT *  
FROM Modelli  
WHERE nome_modello LIKE "C#_F%" ESCAPE "#"
```