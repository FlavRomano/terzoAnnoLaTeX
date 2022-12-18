I segnali sono interruzioni software che: possiamo mandare ad un processo o ad un thread oppure che si possono scambiare tra loro processi e threads. Essi comunicano al processo il verificarsi di un evento, ad ognuno dei quali corrisponde un segnale numerato.

Un processo all’arrivo di un segnale di un certo tipo può decidere di:
- Ignorarlo.
- Lasciarlo gestire al kernel con l’azione di default definita per quel segnale.
- Specificare una funzione (detta **signal handler**) che viene mandata in esecuzione appena il segnale viene rilevato.

I segnali sono inviati:
- Da un processo/thread ad un altro usando `kill` oppure `pthread_kill`.
- Dall’utente con particolari combinazioni di tasti (al processo in foreground), ad esempio ^C corrisponde a SIGINT e ^Z corrisponde a SIGTSTP.
- Dall’utente con l’utility `kill` della shell.
- Dal SO per comunicare al processo il verificarsi di particolari eventi (ad esempio SIGFPE: errore floating-point e SIGSEGV: segmentation fault).
- Da un processo/thread a se stesso usando `raise`.

## Strutture dati relativi del kernel relative ai segnali
Le strutture dati che il kernel utilizza per la gestione dei segnali sono:
- **Signal handler array** che descrive cosa fare quando arriva un segnale di un certo tipo, le possibilità sono ignorare o trattare con un handler (e in questo caso nell’array c’è un puntatore al codice dell’handler). Ogni processo ha un signal handler array, quindi è **condiviso** tra i suoi threads.
- **pending signal bitmap** che è **privata** per thread e contiene un bit per ogni tipo di segnale, se il bit X è a 1 allora il thread ha un segnale pendente (lanciato ma non ancora gestito) di tipo X.
- **signal mask** che è **privata** per thread e contiene un bit per ogni tipo di segnale, se il bit X è a 1 allora il thread non riceve i segnali di tipo X.

## Gestione di un segnale
### Processo single-thread
Quando arriva un segnale X ad un processo single-thread:
- Il segnale viene inserito nella *pending signal bitmap* e se la signal mask non blocca X il processo viene interrotto. I segnali generalmente non interrompono le system call ma c'è un'eccezione, se la system call è bloccata per qualche motivo (e.g lettura da una pipe vuota) e arriva un segnale esso potrebbe essere gestito e la system call verrebbe interrotta.
- Il kernel stabilisce quale comportamento adottare controllando il contenuto del signal handler array.

### Processo multi-thread
Se il segnale non è destinato ad un thread particolare allora solo uno di essi (scelto in maniera random) gestisce il segnale; mentre se il segnale è destinato ad un thread particolare T nel processo:
- Se la signal mask di T non blocca il segnale X il thread viene interrotto.
- Il kernel stabilisce quale comportamento adottare controllando il contenuto del signal handler array (globale, del processo).

Tipicamente: 
- Se il segnale è dovuto ad un errore viene inviato al thread che ha fatto l’errore e quindi è lui a gestirlo.
-  Se i segnali sono generati sinteticamente vanno al thread o al processo (dipende dalla sys call usata).
- Tutti gli altri segnali vanno al processo.

## Signal safety
In questo ambito esiste il concetto di Asynchronous safe function: 
> Una funzione è sicura per i segnali asincroni se può essere richiamata in modo sicuro e senza 
> effetti collaterali, senza interferire con altre operazioni, da un contesto di gestione dei segnali. 
> In altre parole, deve poter essere interrotta in qualsiasi punto per essere eseguita linearmente 
> fuori sequenza senza causare uno stato inconsistente. Deve funzionare correttamente anche 
> quando i dati globali possono trovarsi in uno stato inconsistente.

Immaginiamo di star eseguendo una funzione `f` all’interno di un thread, all’arrivo di un segnale parte il gestore (che interrompe l’esecuzione di `f`). Se anche il gestore chiama `f` la sua esecuzione deve avvenire in modo corretto (oss. `f` può essere anche lo stesso handler). Questo concetto di safety quindi riguarda delle funzioni che sono eseguite concorrentemente ma dallo stesso thread.
```c
// variabili globali

int tot_segnali = 0;  
volatile bool continua = true;

// funzione che viene invocata quando viene ricevuto un segnale USR1 USR2 o INT (Control-C)
void handler(int s) {
	tot_segnali++;
	printf("Segnale %d ricevuto dal processo %d\n", s, getpid());
	if(s==SIGUSR2) { 
		continua = false;
		kill(getpid(),SIGUSR1);
	}
}
```
In questo caso infatti quando SIGUSR2 viene gestito con l’handler esso invia SIGUSR1 che viene sempre gestito dallo stesso handler. Quindi le funzioni usate come handler devono essere asynchronous safe.

Un esempio di funzione non async-signal-safe è la seguente:
```c
int somma2(int *y, int x) {
	// lock?
	*y += x;
	// unlock?
	return 0; 
}
```
Immaginiamo che un thread arrivi all’istruzione *y += x.
- Dopo aver caricato in un registro il valore a cui punta y arriva un segnale che nell’handler riesegue `somma2` che incrementa tale valore. 
- Quando la prima esecuzione di `somma2` riparte il valore a cui si va a sommare x è quello vecchio e non quello incrementato dell’handler del segnale; questo succede perchè c’è un puntatore alla variabile y che viene modificata da entrambe le invocazioni della funzione. 
- In questo caso se uso un mutex rischio il deadlock perché la seconda invocazione rimane in attesa che la prima sblocchi il mutex, cosa che non succederà in quanto la prima attende la terminazione della seconda. 

Quindi è più difficile soddisfare il requisito di async-signal-safe che quello di MT-saf.e