In questo tipo di organizzazione i record di una collezione sono memorizzati **consecutivamente sul valore di uno o un insieme di attributi $A_{1},\ldots,A_{n}$**.

Questo tipo di organizzazione facilita le operazioni di **ordinamento/raggruppamento/ricerca** sugli attributi su cui è definito l'ordinamento. È più complessa di quella seriale perché in questo caso l'ordinamento deve essere **garantito ad ogni inserzione**.

Spesso è la soluzione più usata per **collezioni statiche**, che non richiedono future inserzioni.

La ricerca è **binaria** su file di dati ordinati, richiede $\lg \# blocchi$ **accessi** a pagina
- se il file contiene $b_{i}$ blocchi (pagine), la localizzazione di un record richiede:
	- **ricerca binaria nel file**
	- accesso al blocco che contiene il record
	- quindi richiede $\lg b_{i}+ 1$ accessi a pagina.

n.b: *blocchi* e *pagine* sono sinonimi.

## Esempio
Un file ordinato con $r = 30'000$ record, con dimensione dei blocchi/pagine su disco $B=1024$ byte, i record hanno dimensione fissa $R = 100$ byte e sono indivisibili.
- il **fattore di blocco/pagine** è $$bfr = \left\lfloor \frac{B}{R} \right\rfloor = \left\lfloor\frac{1024}{100} \right\rfloor = 10\text{ record per blocco}$$
- il **numero di blocchi necessari** è $$b = \left\lceil \frac{r}{bfr} \right\rceil = \left\lceil\frac{30'000}{10}\right\rceil = 3000\text{ blocchi}$$
- una **ricerca binaria su file dati richiede** $$\lg b = \lg 3000 \sim 12 \text{ accessi ai blocchi/pagine}$$
