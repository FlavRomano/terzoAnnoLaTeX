A volte può essere richiesto di 
- calcolare operatori aggregati **non per l’intera tabella**
- ma **raggruppando le righe** i cui valori **coincidono su un certo attributo**.

Si usa la `GROUP BY`.

## Semantica 
1. La query inizia senza operatori aggregati e senza `group by`
2. Il risultato è **diviso in sottoinsiemi** aventi gli **stessi valori** per gli attributi indicati nel `group by`
3. Quindi l'operatore aggregato è calcolato su ogni sottoinsieme

## The good way
Quando si effettua un raggruppamento, questo deve essere effettuato 
- su tutti gli elementi della target list **che non sono operatori aggregati** 
- perché nel risultato deve apparire una riga per ogni gruppo

```sql
SELECT Dipart, AVG(stip) --\em{Dipart}
FROM Impiegati
GROUP BY Dipart --\em{Dipart}
```

- Gli attributi espressi non aggregati nella `select` devono essere inclusi tra quelli citati nella `GROUP BY`
- Gli attributi aggregati `(avg(e.Voto))` vanno scelti tra quelli non raggruppati

```sql
SELECT s.Nome, avg(e.Voto) -- e.Voto perché la tabella s viene usata per la groupBy
FROM Studenti s, Esami e 
WHERE s.Matricola = e.Matricola 
GROUP BY s.Matricola, s.Nome -- perché la colonna Matricola non è nella Select, Nome sì
```

### Sbagliatissimo
```sql
SELECT padre, avg(f.reddito), p.reddito  
FROM persone f 
JOIN paternita ON figlio = nome 
JOIN persone p ON padre = p.nome
GROUP BY padre
```

### Corretto
```sql
SELECT padre, avg(f.reddito)  
FROM persone f 
JOIN paternita ON figlio = nome 
JOIN persone p ON padre = p.nome 
GROUP BY padre
```

## Esempio 
La media degli stipendi degli impiegati per ogni dipartimento.

```sql
SELECT Dipart, AVG(stipendio) 
FROM Impiegati  
GROUP BY Dipart
```

![[Pasted image 20230822173442.png]]

Quindi, la query viene eseguita senza operatori aggregati e senza `group by`

```sql
SELECT Dipart
FROM Impiegati 
```

Il risultato viene diviso in sottoinsiemi aventi gli stessi valori per l'attributo `Dipart`

![[Pasted image 20230822173332.png]]

Infine viene applicato l'operatore aggregato su ogni sottoinsieme, cioè [[AVG]] dello stipendio

![[Pasted image 20230822173407.png]]

n.b: se non avessimo messo `GROUP BY` avremmo commesso un [[errore gravissimo]]