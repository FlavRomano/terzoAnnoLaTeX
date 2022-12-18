Un thread è un flusso sequenziale di attività all'interno di un processo. In C sono implementati nella libreria `pthread.h`.

## Ciclo di vita dei thread
### pthread_create
```c
int pthread_create(pthread_t *thread, pthread_attr_t *attr, void *(*start_routine) ( void *), void *arg);
```

La funzione `pthread_create` crea un nuovo thread nel processo chiamante. L'argomento `thread` è il puntatore che identifica il thread creato e viene utilizzato anche in `pthread_join` in attesa che il processo termini. Il nuovo thread inizia l'esecuzione invocando `start_routine` con unico parametro `arg`  (per questo motivo se necessitiamo di più argomenti viene passata una struct). In caso di successo restituisce 0, altrimenti un numero di errore e il contenuto di `*thread` rimane indefinito.
#### Quando termina un thread?
Il nuovo thread termina in uno dei seguenti modi:
- Chiama `pthread_exit`, specificando un valore di stato di uscita che è disponibile per un altro thread nello stesso processo che chiama `pthread_join`.
- Finisce la `start_routine`, cioò equivale a chiamare `pthread_exit`.
- Viene cancellato con `pthread_cancel`.
- Uno qualsiasi dei thread del processo chiama `exit` oppure il processo principale finisce il `main`. Questo provoca la terminazione di tutti i thread del processo.

### pthread_join
```c
int pthread_join(pthread_t thread, void **retval);
```

La funzione `pthread_join` attende che il thread specificato termini. Per recuperare il valore di uscita del thread, memorizzo una variabile `retval` e la passo come secondo argomento. 
Se il thread `thread` è già terminato allora `pthread_join` restituisce immediatamente 0. In caso di successo restituisce 0, altrimenti un numero di errore.

#### Alternative a join
Un altro modo per far capire al thread principale che i thread ausiliari hanno finito un certo compito, senza aspettare che essi terminino, e quello di usare un semaforo (senza nome). Il semaforo viene inizializzato a zero e se abbiamo `n` threads basta fare un ciclo for con una `sem_wait` sul semaforo all’interno, ognuna delle quali si bloccherà essendo il semaforo a zero. Quando i thread hanno terminato il loro compito fanno la `sem_post`, in questo modo siamo sicuri che solo dopo `n` chiamate di `sem_post` il thread principale può uscire dal for e quindi essere certo che tutti i thread hanno terminato quel particolare compito.

## Mutex
Per inizializzare un mutex già esistente:
```c
int pthread_mutex_init(pthread_mutex_t *mutex, pthread_mutexattr_t *attr);
```

Per creare un mutex con attributi di default e distruggerlo:
```c
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER; 
int pthread_mutex_destroy(pthread_mutex_t *mutex);
```

Operazioni su un mutex:
```c
int pthread_mutex_lock(pthread_mutex_t *mutex); 
int pthread_mutex_trylock(pthread_mutex_t *mutex);
int pthread_mutex_unlock(pthread_mutex_t *mutex);
```

Potremmo usare allo stesso modo un semaforo inizializzato a 0 (e i cui valori sono solo 0 e 1), in questo modo la `sem_wait` corrisponde alla `mutex_lock` e la `sem_post` corrisponde alla `sem_unlock` ma ci sono delle differenze. 

In questo caso `mutex_unlock` può essere chiamata solo dal thread che ha fatto la `mutex_lock`, mentre la `sem_post` può essere chiamata da uno qualsiasi dei thread. Per realizzare la mutua esclusione quindi si preferisce l’uso dei mutex.

## Condition variables
Supponiamo che un thread debba aspettare che una certa variabile diventi > 10. Potremmo scrivere:
```c
while (true)
	if (v > 10) break;
```
ma in questo modo facciamo busy waiting che è estremamente inefficiente. 
Nel caso dei semafori possiamo aspettare solo fino a quando la variabile associata non diventa > 0, nel caso più generale si usano le condition variables. Le condition variables permettono di rendere più efficiente l’attesa facendo eseguire il test solamente quando ha senso farlo, cioé quando la variabile da testare viene modificata; segue un esempio di attesa/risveglio:
```c
// ATTESA
pthread_mutex_lock(&m);       //lock del mutex m
while (v <= 0)  
	pthread_cond_wait(&c,&m); //processo messo in waiting list di c e rilascio del mutex m
pthread_mutex_unlock(&m);     //rilascio del mutex m
```

```c
// RISVEGLIO
v+=x;                     //aggiornamento di v
pthread_mutex_lock(&m);   //lock del mutex m
pthread_cond_signal(&c);  //risveglio un thread della waiting list di c
pthread_mutex_unlock(&m); //rilascio del mutex m
```

oss. Le operazioni sulle condition variables devono essere fatte sempre all’interno di una sezione protetta da un mutex.

## Thread safety
Nelle pagine man delle funzioni di libreria è indicato l’attributo Thread Safety che puo essere MT-Safe (Multi Thread Safe), oppure MT-Unsafe, in questo caso è indicata una ragione per cui `e unsafe.
Una definizione di Multi Thread Safe è: 

>Una funzione i cui effetti collaterali, se richiamati da due o più thread, sono garantiti come se i 
>thread eseguissero la funzione uno dopo l'altro in un ordine indefinito, anche se l'esecuzione 
>effettiva è interleaved. 

quindi si fa riferimento ad una singola funzione invocata da due o più thread concorrenti.
Esempio di funzione MT-Unsafe:
```c
int somma(int x) { 
	static c=0;
	c += x; 
	return c;
}
```

La variabile statica c è condivisa tra tutti i threads che invocano la funzione (a differenza di x) e dato che l’incremento non è atomico non avviene in modo corretto. 
Immaginiamo ad esempio di chiamare la funzione con due threads, prima con x=2 e poi con x=1; 
- Il primo thread ottiene come risultato 2 e il secondo thread ottiene come risultato 3, quindi alla fine la variabile c è stata incrementata di 3.
1. Però potrebbe succedere che il primo thread legge il valore di c che è 0, si sospende e comincia l’esecuzione del secondo thread che legge il valore di c (sempre 0), ci somma 1 e lo aggiorna. 
2. A questo punto riprende l’esecuzione del primo thread che somma 2 al valore di c che aveva letto e lo aggiorna, quindi in questo caso la variabile viene incrementata di 2 e non di 3 come doveva succedere. 

Questo significa che la funzione non è MT-Safe in quanto l’esecuzione interleaved può differire da quella sequenziale.

A volte possiamo rendere una funzione MT-safe rendendo atomica la modifica di una variabile condivisa (che sia globale/statica o acceduta mediante un puntatore) con un mutex.

## Re-entrancy
La funzione deve essere safe (ad esempio deve modificare correttamente le variabili ecc..) al ”rientro” dall’esecuzione della stessa funzione da un altro thread o anche dal thread stesso. I consigli pratici sono di evitare di usare variabili globali e statiche nei programmi multi-thread e se ci sono proteggerle con i mutex; scrivere signal handler brevi e semplici che chiamano solo funzioni di libreria async-signal-safe e cercare di evitare che lo stesso handler sia chiamato simultaneamente.

### Funzioni re-entrancy e non
La libreria del C contiene diverse funzioni con il sufisso "r" che in qualche modo sono legate alla re-entrancy, ma non è sempre così. 

Ne sono esempi le funzioni: strtok, strtok r, qsort, qsort r.  
- Quando usiamo `strtok` alla prima invocazione passiamo come argomenti la stringa da tokenizzare e il delimitatore, nelle invocazioni successive invece passiamo come argomenti NULL e il delimitatore in quanto la funzione salva in una variabile statica il punto in cui è arrivata a leggere la stringa e quindi nelle iterazioni successive comincia a scorrerla da tale punto. Proprio per questo strtok non è una funzione MT-safe. Una versione MT-save è appunto `strtok_r` che prende come terzo argomento un puntatore ad una stringa che serve internamente alla funzione per sapere da quale carattere iniziare a scorrere la stringa nelle varie iterazioni.
- La funzione `qsort_r` che prende un parametro in più rispetto alla `qsort` (un puntatore a void) e lo passa alla funzione di confronto. Quindi anche se la `qsort_r` risolve un possibile problema di safety evitando di usare la `qsort` con una variabile globale non dobbiamo dimenticare che non è un modo per rendere MT-safe il qsort, ma è proprio una funzione diversa che potrebbe avere problemi di rientranza.