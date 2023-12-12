Sono una classe di protocolli che **tendono a ritardare** l'esecuzione di **transazioni prone a generare conflitti**, quindi *anomalie* rispetto alle transazioni concorrenti.

Cercano di **prevenire i conflitti**, sono quelli più usati nella pratica.

Si dividono in due classi principali: 
1. Metodi **basati su lock**
2. Metodi **basati su timestamp**

## Metodi basati su lock
I DBMS commerciali implementano il controllo della concorrenza **usando il meccanismo delle lock**:
> Per poter effettuare una qualsiasi operazione di lettura/scrittura su una risorsa, è necessario aver **precedentemente acquisito la lock sulla risorsa stessa** (*il controllo sulla risorsa*).

Abbiamo due tipologie di lock:
1. **Lock in lettura** per ==**l'accesso condiviso**==
2. **Lock in scrittura** per la ==**mutua esclusione**==

In soldoni, la lock è un meccanismo che ==**impedisce** ad altre transazioni di **accedere ai dati** a cui una **transazione sta accedendo**==.

Ci possono essere 
- lock a **livello di riga/tabella/pagina** (quindi a **multi-granularità**)
- lock in **operazioni di scrittura (mutua esclusione) o lettura (accesso condiviso)** (quindi **multimodale**)

### Quando una risorsa è bloccata
Le transazioni che ne richiedono l'accesso **vengono messe in coda**. Quindi devono aspettare che la lock venga **rilasciata**.

### Operazioni sulla lock
Su ogni lock sono definite **due operazioni**:
1. **Richiesta** della lock in ==lettura/scrittura==
2. **Rilascio** della lock ==acquisita in precedenza== (`unlock`)

```c
// codice utente
Transazione T0:
	read(x)
	write(y)

// codice con lock
Transazione T0:
	lock_read(x)
	read(x)
	unlock(x)
	lock_write(y)
	write(y)
	unlock(y)
```

Il gestore della concorrenza (serializzatore) del DBMS ha il compito di **stabilire l'ordine d'esecuzione delle singole operazioni**, ==per rendere **serializzabile** l'esecuzione di un insieme di transazioni==.

### Prestazioni
Questo meccanismo è molto efficace per prevenire conflitti, ma influisce sulle prestazioni.

### 2PL: Strict Two Phase Locking (blocco a due fasi stretto)
È un protocollo definito dalle seguenti regole:
1. Ogni transazione **richiede la lock** per il blocco corrispondente **prima di effettuare un'operazione**
2. **Transazioni diverse** ==non ottengono **blocchi in conflitto**==
3. Le **lock si rilasciano al termine della transazione** (cioè al **commit** o all'**abort**)

Questo ultimo punto può fare generare situazioni di **deadlock**
e.g:

Transazioni:
```c
Transazione T1:                                      Transazione T2:
	read(x)                                              read(y)
	write(y)                                             write(x)
	commit                                               commit
```

Schedule:

| T1              | T2              | Azione     |
| --------------- | --------------- | ---------- |
| `lock_read(x)`  |                 |            |
|                 | `lock_read(y)`  |            |
| `read(x)`       |                 |            |
|                 | `read(y)`       |            |
| `lock_write(y)` |                 |            |
|                 | `lock_write(x)` |            |
| ///             | ///             | **STALLO** |

### Gestire un deadlock
Per gestire deadlock provocate dal gestore della concorrenza, si possono usare **tre tecniche**:
#### 1. Usare dei timeout
**Ogni operazione** di una transazione **ha un timeout**
- la transazione **deve essere completata prima del timeout**
- altrimenti viene **annullata** la transazione stessa (**abort**)

e.g:

```c
Transazione1:
	lock_read(x, 4000);
	read(x)
	lock_write(y, 2000)
	write(y)
	COMMIT
	unlock(x)
	unlock(y)
```

#### 2. Deadlock avoidance
Prevenire le **configurazioni** che potrebbero portare a deadlock. Ci sono vari modi per farlo:
1. Lock/Unlock **di tutte le risorse** allo stesso tempo
2. Utilizzo di **timestamp** o **classi di priorità** tra transazioni
	- può verificarsi starvation e.g una transazione non ottiene mai la lock.

#### 3. Deadlock detection
Si utilizzano algoritmi per **identificare eventuali situazioni di deadlock** e prevedere meccanismi di **recupero dal deadlock**.
- viene utilizzato il **grafo di Holt** (anche noto come *grafo delle attese*) per ==identificare la **presenza di cicli**== (corrispondenti a deadlock)
	- in caso di ciclo si fa **abort** delle transazioni che lo compone, ==eliminando così la **mutua dipendenza**==

## Metodi basati su timestamp
Per la gestione della concorrenza in un DBMS vi sono alternative al 2PL, ad esempio utilizzare **time-stamp delle transazioni**.
- A ogni transazione **si associa un timestamp**
	- il timestamp rappresenta il ==momento **d'inizio** della transazione==
- Una transazione ==non può **leggere** o **scrivere** un dato== che è stato **scritto da una transazione con un timestamp maggiore**.
- Una transazione ==non può **scrivere** su un dato== già **letto da una transazione con timestamp maggiore**.

### Livelli di isolamento/consistenza per ogni transazione
- **SERIALIZABLE** assicura che
	1. la transazione $T$ ==**legga** solo cambiamenti fatti da **transazioni concluse**== (che hanno fatto commit)
	2. nessun valore **letto/scritto da** $T$ ==venga **cambiato da altre transazioni** finché **$T$** non è **conclusa**==
	3. se $T$ legge un **insieme di valori** acceduti secondo qualche condizione di ricerca, allora ==l'insieme di valori **non viene modificato** da altre transazioni **finché $T$ non è conclusa**==
- **REPEATABLE READ** assicura che
	1. la transazione $T$ ==**legga** solo cambiamenti fatti da **transazioni concluse**== (che hanno fatto commit)
	2. nessun valore letto/scritto da $T$ ==venga **cambiato da altre transazioni** finché **$T$** non è **conclusa**==
- **READ COMMITTED** assicura che
	1. la transazione $T$ ==**legga** solo cambiamenti fatti da **transazioni concluse**== (che hanno fatto commit)
	2. La transazione $T$ ==non rileva alcuna modifica effettuata da **transazioni** **concorrenti** **non** **concluse**== tra i **valori che ha letto all'inizio di T**.
- **READ UNCOMMITTED**
	- A questo livello di isolamento una transazione $T$ può ==**leggere le modifiche** fatte ad un oggetto da una **transazione in esecuzione**==
	- L'oggetto può **essere cambiato mentre $T$ è in esecuzione** 
		- quindi $T$ è soggetta a ==**effetti fantasma**==.