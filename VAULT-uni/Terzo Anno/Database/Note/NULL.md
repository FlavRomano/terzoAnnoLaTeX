SQL fornisce `NULL` come valore di un campo quando questo manca all'interno di una ennupla.

Introduce dei problemi:
- occorrono dei predicati per controllare se un valore è/non è `NULL`.
- la condizione `reddito > 8` è **vera** o **falsa** quando il reddito è uguale a `NULL`?
	- è **falsa**

Per questo abbiamo l'operatore `IS NULL`

## IS NULL
Verifica se il contenuto di un operando è `NULL`

```sql
SELECT *  
FROM Veicoli  
WHERE Cilindrata IS NULL
```

## Logica dei predicati a tre valori
I valori `NULL` rappresentano l'assenza di informazione:
- valore sconosciuto
- valore non disponibile
- attributo non applicabile

Ciascun valore `NULL` è quindi considerato **diverso** dagli altri valori `NULL`.

![[Pasted image 20230822164716.png]]

Il confronto logico con un valore che `NULL` da come output **sconosciuto** (indicato con **S**)

## Esempi
1. __
```sql
SELECT * 
FROM Persone 
WHERE Nome = 'Luigi' OR AnnoNascita = 2022
```

![[Pasted image 20230822164825.png]]
la query restituirà:

|  Nome | Cognome | AnnoNascita | LuogoDiNascita |
|-------|---------|-------------|----------------|
| Luigi | Bianchi |      (null) |           Roma |
| Mario |   Verdi |        2022 |           Pisa |

2. __
```sql
SELECT * 
FROM Persone 
WHERE Nome = 'Luigi' AND AnnoNascita = 2022;
```

![[Pasted image 20230822165011.png]]
la query non restituirà nulla.