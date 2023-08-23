L'operatore EXISTS viene utilizzato per **verificare l'esistenza di qualsiasi record in una [[5. SQL#Subquery|subquery]]**.
- restituisce **VERO** se la subquery **restituisce uno o più** record.
- altrimenti **FALSO**

Tramite il predicato EXISTS è possibile effettuare il controllo sull’esistenza di righe che soddisfano specifiche condizioni.

## Esempi
### Esempio 1
Trovare l'insieme degli `IdReparto` dei reparti che hanno **almeno** un impiegato con la mansione "Programmatore".
- IMPIEGATI(<u>Matricola</u>, Cognome, Nome, Mansione, IdReparto\*, StipendioAnnuale, PremioProduzione, DataAssunzione)
- REPARTI(<u>IdReparto</u>,NomeReparto,Indirizzo,Città)

```sql
SELECT IdReparto 
FROM Reparti 
WHERE EXISTS (  
	SELECT * 
	FROM Impiegati 
	WHERE Mansione = ‘Programmatore’
)
```

### Esempio 2
Trovare i nomi di tutte le categorie per cui è presente **almeno** un veicolo
- Categorie(<u>Cod_cat</u>, Nome_cat)
- Veicoli(<u>Targa</u>, Cod_mod\*, Cod_cat, Cilindrata, Cod_comb, cav.Fisc, Velocità, Posti, Imm)

```sql
SELECT Nome_cat
FROM Categorie
WHERE EXISTS (
	SELECT *
	FROM Veicoli
	WHERE Categorie.Cod_cat = Veicoli.Cod_cat
)
```

### Esempio 3
Tutte le categorie per cui non è presente **nessun** veicolo
- Categorie(<u>Cod_cat</u>, Nome_cat)
- Veicoli(<u>Targa</u>, Cod_mod\*, Cod_cat, Cilindrata, Cod_comb, cav.Fisc, Velocità, Posti, Imm)

```sql
SELECT Nome_cat
FROM Categorie
WHERE NOT EXISTS (
	SELECT *
	FROM Veicoli
	WHERE Categorie.Cod_cat = Veicoli.Cod_cat
)
```

### Esempio 4
Gli studenti con **almeno** un voto sopra 27
- Studenti(Cognome, Matricola)
- Esami(IdEsame, Matricola, Voto, Data)

```sql
SELECT *
FROM Studenti
WHERE EXISTS (
	SELECT *
	FROM Esami
	WHERE Studenti.Matricola = Esami.Matricola and Esami.Voto > 27
)
```

### Esempio 5
I padri i cui figli guadagnano **tutti** più di 2000
- Paternità(Padre, Figlio)
- Persone(Nome, Eta, Reddito)

```sql
SELECT distinct Padre
FROM Paternità P1
WHERE NOT EXISTS (
	SELECT *
	FROM Paternità P2, Persone 
	WHERE P1.Padre = P2.Padre and P2.Figlio = Nome
		and Reddito <= 2000
)
```