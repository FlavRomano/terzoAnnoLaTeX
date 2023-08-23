L'istruzione **INSERT** permette di inserire un nuovo dato in una tabella 
```sql
INSERT INTO nome tabella[(ListaAttributi)] 
VALUES (ListaDiValori)
```

## Esempi
### Esempio 1
Supponiamo di avere definito la tabella:
- Esami(Corso, Insegnante, Matricola, Voto)

Dopo una 
```sql
INSERT INTO Esami(Corso, Matricola, Voto)
VALUES("DB", "1234", 20)
```

| Corso | Insegnante | Matricola | Voto |
| ----- | ---------- | --------- | ---- |
| DB    | NULL       | 1234      | 20   |

- Ai valori non attribuiti viene assegnato NULL
	- a meno che non sia specificato un diverso valore di default.  
- L’inserimento fallisce se NULL non è permesso per gli attributi mancanti.

### Esempio 2
E’ possibile effettuare un insert prendendo i dati da un’altra tabella.
Con questo tipo di insert si possono effettuare tanti inserimenti simultaneamente.

```sql
INSERT INTO Indirizzi_Studenti (Indirizzo, Telefono) 
SELECT Indirizzo, Telefono  
FROM Studenti
```
