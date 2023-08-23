Si possono **applicare condizioni** sul **valore aggregato per ogni gruppo**, mediante la clausola `HAVING`. Così da poter selezionare solo delle determinate ennuple (proprio come con `WHERE`)

In generale si usa **quando la condizione coinvolge un operatore aggregato**, altrimenti (per un semplice attributo) si utilizza [[5. SQL#WHERE|WHERE]].

## Può essere messa prima di [[GROUPBY]]
Si, l'aggregazione all'interno della clausola `having` utilizzerà sempre il raggruppamento specificato da [[GROUPBY]].

## Esempi
### Esempio 1
I dipartimenti la cui media degli stipendi è maggiore di 1700 euro.

```sql
Select dipart, AVG(stipendio) FROM Impiegati  
Group by Dipart  
HAVING AVG(stipendio) > 1700
```

![[Pasted image 20230822174948.png]]

### Esempio 2
Dalla tabella Esami, per ogni matricola:
- conta il numero di esami
- il voto più basso
- il voto più alto
- il voto medio

```sql
SELECT Matricola, count(*) AS NEsami, min(Voto), max(Voto), avg(Voto) 
FROM Esami  
GROUP BY Matricola  
HAVING count(*) > 1;
```

Questa clausola `HAVING` agisce come un **filtro sugli insiemi raggruppati**. 
- seleziona solo gli studenti che hanno sostenuto più di un esame (il conteggio è maggiore di 1). 
	- gli studenti con un solo esame verranno esclusi dal risultato.

![[Pasted image 20230822175526.png]]