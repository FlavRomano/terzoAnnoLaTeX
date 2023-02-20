## Ricerca BF
### Su grafo
Sia `r` il nodo di partenza
1. `Frontiera = {r}`, coda FIFO.
2. `Esplorati = set()`
3. `Frontiera.èVuota()`$\implies$`return fallimento`
4. `nodo = Frontiera.pop()`
5. `Esplorati.push(nodo)`
6. `nodo.figlio`$\notin$`Esplorati and nodo.figlio`$\notin$`Frontiera`
	1. `if Test(nodo.figlio): return soluzione(nodo.figlio)`
	2. `Frontiera.push(nodo.figlio)`
7. GOTO 6 `until nodo.nonHaPiùFigli()`
8. GOTO 3

### Su albero
Sia `r` il nodo di partenza
1. `Frontiera = {r}`, coda FIFO.
2. `Frontiera.èVuota()`$\implies$`return fallimento`
3. `nodo = Frontiera.pop()`
4. `nodo.figlio`$\notin$`Esplorati and nodo.figlio`$\notin$`Frontiera`
	1. `if Test(nodo.figlio): return soluzione(nodo.figlio)`
	2. `Frontiera.push(nodo.figlio)`
5. GOTO 4 `until nodo.nonHaPiùFigli()`
6. GOTO 2

Complessità in tempo: $O(b^d)$
Complessità in spazio: $O(b^d)$
Completo, Ottimale $\iff \forall u \in N.\; g(u) = k \cdot depth(u)$

## Ricerca DF
### Su grafo
Sia `r` il nodo di partenza
1. `Frontiera = {r}`, coda LIFO.
2. `Esplorati = set()`
3. `Frontiera.èVuota()`$\implies$`return fallimento`
4. `nodo = Frontiera.pop()` s
5. `Esplorati.push(nodo)`
6. `nodo.figlio`$\notin$`Esplorati and nodo.figlio`$\notin$`Frontiera`
	1. `if Test(nodo.figlio): return soluzione(nodo.figlio)`
	2. `Frontiera.push(nodo.figlio)`
7. GOTO 6 `until nodo.nonHaPiùFigli()`
8. GOTO 3

### Su albero
Sia `r` il nodo di partenza
1. `Frontiera = {r}`, coda FIFO.
2. `Frontiera.èVuota()`$\implies$`return fallimento`
3. `nodo = Frontiera.pop()`
4. `nodo.figlio`$\notin$`Esplorati and nodo.figlio`$\notin$`Frontiera`
	1. `if Test(nodo.figlio): return soluzione(nodo.figlio)`
	2. `Frontiera.push(nodo.figlio)`
5. GOTO 4 `until nodo.nonHaPiùFigli()`
6. GOTO 2