Questo tipo di organizzazione prevede l'esistenza di un algoritmo di **trasformazione della chiave**:
> dato il valore di una chiave, l'algoritmo restituisce l'indirizzo della pagina in cui memorizzare (e successivamente cercare) il record

## Hash file
In un file hash i record
- vengono allocati in una **pagina** il cui indirizzo dipende dal **valore di chiave** del record.

 $$key \to Hash(key) \to page\;address$$

Una comune funzione hash è il **resto della divisione intera**: $$Hash(k) = H(k) =  k \text{ mod } NP$$
- ricordiamo che $NP$ è il numero di pagine di un file

Si può applicare anche a chiavi alfanumeriche, dopo averle convertite.

![[Pasted image 20231030164126.png]]

## Metodo procedurale statico
### Che vuol dire statico?
Un'organizzazione è detta **statica** se una volta dimensionata per una certa quantità di dati, **non si riconfigura automaticamente** a seguito di un aumento dei dati memorizzati. Quindi comporta un **degrado delle prestazioni** che si elimina solo a seguito di una **riorganizzazione**.

![[Pasted image 20230830162643.png]]

Un progetto ha per parametri:
- la funzione per la **trasformazione della chiave** (per l'hashing)
- il **fattore di caricamento** $$d = \frac{N}{M\cdot c}$$
	- $d$ rappresenta la **frazione dello spazio fisico disponibile** utilizzato in media
	- se $N = \#\text{tuple previsto per il file}$
		- $M = \text{fattore di pagine}$
		- $c = \text{fattore di caricamento}$
	- allora il file può prevedere un numero di pagine $B$ pari all'intero immediatamente superiore a $d$, cioè $$B = \lceil d \rceil$$
- la **capacità** $c$ delle pagine
- il metodo per la gestione dei trabocchi

## Strutture hash
Le collisioni sono gestite con linked list
- è l'organizzazione più efficiente per l'accesso diretto basato su valori della chiave con condizioni di uguaglianza (**accesso puntuale**).
- non è efficiente per **ricerche basate su intervalli**
- funzionano solo con file la cui dimensione non varia molto nel tempo (procedurale statico)
	- viene utilizzata maggiormente per tabelle che **non vengono aggiornate spesso**

### Trade-off
Purtroppo nelle tabelle hash 
- se il **$\# blocchi$** è più **piccolo** rispetto al database si hanno **frequenti collisioni**
- se il **$\# blocchi$** è **troppo grande** rispetto al database si ha un **fattore di riempimento dei blocchi molto basso**

Una struttura Hash non è efficiente per le query su un **range di valori**:
```sql
SELECT *
FROM STUDENTI
WHERE (MATRICOLA > 10) AND (MATRICOLA < 20)
```

e non è efficiente per operazioni che **coinvolgono attributi non chiave**.

> Quindi possiamo affermare che il metodo procedurale (hash) è utile per ricerche per chiave ma non per intervallo.

Per entrambi i tipi di ricerche v. [[Metodo tabellare]]