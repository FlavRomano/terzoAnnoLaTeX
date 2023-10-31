Consideriamo il caso base, cioè
- a $Z=2$ vie
- e supponiamo di avere solo $NB=3$ buffer in memoria centrale

![[Pasted image 20230831171537.png]]

## Fusione z-way
Nel caso base **Z = 2** si fondono **due run alla volta**
- con $NB=3$, si associa un buffer a ognuna delle run
	- il terzo buffer **serve per produrre l'output** una pagina alla volta

In sostanza
- si legge la **prima pagina da ciascuna run**
	- quindi si può determinare la prima pagina dell'output
- quando **tutti i record di una pagina di run** sono stati **consumati**
- si legge **un'altra pagina della run**

![[Pasted image 20230831172442.png]]

## Complessità
Sempre osservando il caso base, consideriamo solo il **numero di operazioni di I/O**

Con $Z=2$ e $NB=3$ si osserva che:
- nella **fase di sort interno** si leggono e si riscrivono $NP$ pagine
- ad ogni **passo di merge** si leggono e si riscrivono $NP$ pagine
- quindi $2\cdot NP$
	- il numero di **passi di fusione** è $$\# fusione = \lceil \lg NP\rceil$$ in quanto ad ogni passo il numero di run **si dimezza**
- il costo complessivo è $$2 \cdot NP \cdot \left( \lceil \lg NP\rceil + 1 \right)$$

### Esempio
Per ordinare $NP = 8000$ pagine sono necessarie
$$2 \cdot 8000 \cdot (\lceil \lg 8000\rceil + 1) \sim 224'000\text{ operazioni di I/O}$$
se ogni operazione richiede $20$ ms, il sort impiegherebbe $1.15$ ore

oss: se $NP$ non è una potenza di 2, il numero di operazioni è minore di quello calcolato. Perché in uno o più passi di fusione può capitare che una run non venga fusa con un'altra.