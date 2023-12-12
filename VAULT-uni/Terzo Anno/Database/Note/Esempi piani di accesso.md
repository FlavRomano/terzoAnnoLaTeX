## **SELECT-FROM-WHERE** 

```sql
SELECT A
FROM R
WHERE A BETWEEN 50 AND 100
```

![[Pasted image 20231101112945.png]]

> Al posto di **TableScan** e **Filter** potrei usare una **IndexFilter** con un filtro sull'attributo $A$, in questo caso $\psi = \texttt{A BETWEEN 50 AND 100}$.

## **SELECT-FROM-WHERE** con **DISTINCT**

```sql
SELECT DISTINCT A
FROM R
WHERE A BETWEEN 50 AND 100
```

![[Pasted image 20231101113119.png]]
Con *SortScan* si ottiene la collezione **ordinata** dei record di $R$, che vengono poi filtrati da *Filter*, proiettati dal *Project* e si eliminano i duplicati con *Distinct*.
Se $A$ fosse una chiave, il *Distinct* e l'ordinamento di $R$ sarebbero inutili.
> La ragione è che le chiavi primarie sono garantite per contenere valori univoci, quindi non ci sarebbero duplicati da eliminare.

## **SELECT-FROM-WHERE** con **indice**

```sql
SELECT DISTINCT A
FROM R
WHERE A BETWEEN 50 AND 100
```

con l'ipotesi che esista un indice su $A$.

![[Pasted image 20231101113651.png]]

L'indice su $A$ consente di usare l'**IndexFilter**. Quest'ultima rende inutile il **Sort**. 
> L'IndexFilter restituisce la collezione dei record di $R$ che soddisfano la condizione, in più sono **ordinati su $A$**.

> Se avessi avuto $\texttt{SELECT *}$, dal punto di vista fisico avrei potuto **omettere** la **Project** (e la distinct in questo caso) perché non causa problemi.

## **SELECT-FROM-WHERE** con **[[ORDER BY]]**
```sql
SELECT *
FROM R
WHERE A BETWEEN 50 AND 100
ORDER BY A
```
con l'ipotesi che esista un indice su $A$.

![[Pasted image 20231101114030.png]]
Il **Sort** è usato per trattare l'**ORDER BY**, tuttavia abbiamo già detto in precedenza che è inutile se è presente un **IndexFilter**. Quindi possiamo potare l'albero logico, alleggerendolo:

![[Pasted image 20231101114153.png]]

## e1
```sql
SELECT R.A, S.C
FROM R, S
WHERE   R.B = S.D 
	AND R.A > 100
	AND S.C > 200
```

![[Pasted image 20231101114525.png]]

Visto che l'interrogazione è abbastanza semplice, l'albero logico e quello con operatori fisici sono $1:1$. 

Supponiamo di avere due indici su $R.A$ e su $S.D$.
Per ottimizzare l'interrogazione possiamo usare l'**IndexNestedLoop** per la giunzione, l'albero fisico diventa:

![[Pasted image 20231101114712.png]]
## **SELECT-FROM-WHERE** con [[GROUPBY]]
```sql
select
	A, count(*)
from
	R
where
	A between 50 and 100
group by
	A
having
	count(*) > 1
```

L'albero logico di questa interrogazione è

![[Pasted image 20231104104844.png]]

con $\psi = \texttt{A BETWEEN 50 AND 100}$. Questo è il piano d'accesso:

![[Pasted image 20231104105214.png]]

I dati vengono inizialmente filtrati in base a un intervallo specifico sull'attributo $A$. Successivamente, vengono **raggruppati** per l'attributo $A$, **conteggiati** e **ordinati**. Infine, vengono mantenuti solo i gruppi con più di un record.

1. `TableScan("R")`: Questa è l'operazione iniziale, che rappresenta la scansione della tabella "R". In genere, una scansione della tabella implica l'accesso a tutti i dati contenuti in quella tabella.
    
2. `Filter (O, "A BETWEEN 50 AND 100")`: Dopo aver eseguito la scansione della tabella "R", viene applicato un filtro ai dati estratti. In particolare, il filtro esegue un'operazione di selezione in base alla condizione "A BETWEEN 50 AND 100", il che significa che verranno estratti solo i record in cui il valore del campo "A" si trova nell'intervallo compreso tra 50 e 100.
    
3. `Sort(O, {"A"})`: Dopo il filtraggio, i dati vengono ordinati in base al campo "A".
    
4. `GroupBy(O, {"A"}, {count(*)})`: Dopo il sort, viene eseguita un'operazione di raggruppamento dei dati. In questo caso i dati vengono raggruppati in base all'attributo $A$. Inoltre viene calcolato il conteggio dei record all'interno di ciascun gruppo.
    
5. `Filter (O, count(*)>1)`: Infine, dopo l'ordinamento, viene applicato un ulteriore filtro che estrae solo i gruppi in cui il conteggio dei record è maggiore di 1. Ciò significa che verranno esclusi i gruppi con un solo record.

## **SELECT-FROM-WHERE** con [[JOIN SQL|giunzione]]
```sql
SELECT R.A, S.C
FROM R, S
WHERE R.B = S.D
	AND R.A > 100
	AND S.C > 200
```

![[Pasted image 20231104110128.png]]

il **[[8. DBMS (indice e ripassini)#Nested loops|NestedLoop]]** può essere ottimizzato se ==abbiamo **due indici**: su $R.A$ e su $S.D$==. Per fare ciò usiamo **[[Nested loop a indice|IndexNestedLoop]]**
> Visto che esiste un indice nella **relazione interna $S$**, l'algoritmo da usare è IndexNestedLoop$(R,S,\texttt{"R.B=S.D"})$

![[Pasted image 20231104110706.png]]

> Ricordiamo che l'operatore interno $O_I$ della **IndexNestedLoop**  deve essere 
> - o una **IndexFilter**
> - o una **Filter** il cui operando è il risultato restituito da una **IndexFilter**
> v. [[3. Piani d'accesso#Operatori di giunzione $( bowtie)$|operatori di giunzione]] per chiarezza

### Esecuzione fac-simile
![[Pasted image 20231104111911.png]]

![[Pasted image 20231104112005.png]]
> - Nella **IndexFilter** l'insieme di tuple **R.A** è visibile grazie al padre.
> - Difatti **$R.A=S.B$** è il **valore a run-time** della $\psi$.

==Non possiamo invertire i due sottoalberi, perché l'operatore interno (sottoalbero destro) della **IndexNestedLoop** deve essere: o una **IndexFilter** o una **Filter** che opera sul valore restituito da una **IndexFilter**==