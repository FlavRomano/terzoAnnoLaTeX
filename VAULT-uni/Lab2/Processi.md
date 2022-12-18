## System calls
In C le system call sono funzioni di libreria che hanno una corrispondenza 1:1 con le kernel system call.
### open
```c
int open(const char *pathname, int flags, mode_t mode);
```
La `open` è una system call che apre il file specificato dal `pathname`. Se quest'ultimo non esiste e il flag `O CREAT` è attivo allora viene creato. La funzione restituisce un file descriptor, un intero non negativo che può essere utilizzato da altre system call (`read`, `write`, ...) e si riferisce al file aperto. Se l'apertura del file fallisce allora restituisce -1.

### fopen
```c
FILE *fopen(char *pathname, char *mode);
```
La `fopen` è una funzione di libreria che apre il file con nome `pathname` e associa uno stream ad esso. La funzione restituisce un puntatore al file appena aperto, altrimenti `NULL` se fallisce. L'argomento `mode` specifica il modo in cui il file viene aperto, spesso vengono usati:
- `"r"`. Apre il file in lettura. Lo stream è posizionato all'inizio del file.
- `"r+"`. Apre il file in lettura e in scrittura. Lo stream è posizionato all'inizio del file.
- `"w"`. Apre il file rimuovendo il contenuto senza eliminarlo per la scrittura. Lo stream è posizionato all'inizio del file. Il file viene creato se non esiste.
- `"w+"`. Apre il file in lettura e in scrittura. Fa le stesse cose di `"w"`.
- `"a"`. Apre il file per _appending mode_, cioé scrive alla fine del file non sovrascrivendo il contenuto già presente. Infatto lo stream è posizionato alla fine del file. Il file viene creato se non esiste.
- `"a+"`. Apre il file per lettura e _appending mode_. Per quanto riguarda la scrittura avviene sempre alla fine del file (appending), per le letture la posizione può variare (in alcuni sistemi legge all'inizio del file, in altri alla fine del file).
oss. Aprire un file in _appending mode_ fa si che le successive operazioni di scrittura avvengano alla fine del file.

### read
```c
ssize_t read(int fd, void *buf, size_t count);
```
La system call `read` prova a leggere `count` bytes dal file identificato dal file descriptor `fd` scrivendo ciò che legge dentro al buffer `buf`. Se la lettura avviene con successo allora restituisce il numero di byte letti. Non è un errore se legge meno byte di quelli richiesti in `count`, questo perché alcuni byte potrebbero non essere disponibili al momento di quella lettura oppure perché la `read` è stata interrotta da un segnale. Se si verifica un errore allora restituisce -1.

### fread
```c
size_t fread(void *ptr, size_t size, size_t nmembFILE, FILE *stream);
```
La funzione di libreria `fread` legge `nmemb` elementi, tutti grandi `size` byte, dallo stream puntata dal parametro `stream`; il risultato della lettura viene memorizzato nella locazione puntata da `ptr`. La funzione restituisce il numero di elementi letti, se viene raggiunto l'EOF o avviene un errore allora restituisce 0.

### write
```c
ssize_t write(int fd, const void *buf, size_t count);
```
La system call `write` scrive al più `count` bytes provenienti dal buffer puntato dal parametro `buf` nel file identificato dal file descriptor `fd`. Restituisce il numero di byte scritti se la scrittura avviene a buon fine, altrimenti -1 se avviene un errore. Come nel caso della `read`, una `write` avvenuta con successo potrebbe restituire meno byte di quelli specificati da `count`. Queste scritture parziali possono essere causate dallo spazio insufficiente sul disco o se viene interrotto da un segnale. Quando ciò accade il chiamante può finire la scrittura chiamando un'altra `write` per trasferire i restanti bytes. Tuttavia potrebbe avvenire un errore se il disco è pieno.

### fwrite
```c
size_t fwrite(void *ptr, size_t size, size_t nmemb, FILE *stream);
```
La funzione di libreria `fwrite` scrive `nmembs` elementi, tutti grandi `size` byte, letti a partire dalla locazione di memoria puntata da `ptr` nella stream puntata dal parametro `stream`.
Anche in questo caso, la funzione restituisce il numero di elementi scritti se avviene con successo, altrimenti restituisce 0 se raggiunge l'EOF o avviene un errore.

## Cose da sapere riguardo ai processi
Every process has ad identificative code: pid is the process ID in the processes’ table while ppid is the process’ parent ID in the processes’ table. Ogni processo ha un suo identificatore chiamato `pid`, il pid è l'identificativo di un processo mentre il `ppid` è il pid del processo padre.
### fork
```c
pid_t fork();
```
La funzione `fork` crea un nuovo processo duplicando il processo chiamante. Il nuovo processo verrà chiamato "figlio" mentre il processo chiamante della `fork` sarà chiamato "padre". 
- Il processo figlio e il processo padre vengono eseguiti in spazi di memoria separate.
- Al momento della chiamata di `fork()` entrambi gli spazi di memoria hanno lo stesso contenuto e il processo figlio è un'esatta copia del processo padre.

La funzione restituisce il pid del figlio ovviamente al padre (perché ha effettuato lui la chiamata) e 0 al figlio. Se fallisce allora restituisce -1 al padre e il processo figlio non viene creato.

### exit
```c
void _exit(int status);
```
La funzione `exit` termina il processo chiamante immediatamente. Qualsiasi descrittore di file appartenente a quel processo viene chiuso.

### wait
```c
pid_t wait(int *wstatus);
```
La system call `wait` viene usata per aspettare il cambiamento di stato di un figlio del processo chiamante. Uno stato viene considerato "cambiato" quando:
- Il figlio termina.
- Il figlio viene interrotto da un segnale.
- Il figlio viene fatto ripartire da un segnale.

La `wait` sospende l'esecuzione del processo chiamante finché uno dei figli non termina. Se la chiamata avviene con successo allora restituisce il pid del figlio terminato, altrimenti -1.

### waitpid
```c
pid_t waitpid(pid_t pid, int *wstatus, int options);
```

La system call `waitpid` sospende l'esecuzione del processo chiamante finché uno dei suoi figli, identificato dall'argomento `pid`, non cambia stato. Di default `waitpid` aspetta solo la terminazione di un figlio, tuttavia questo comportamento può cambiare in base al parametro `options`. Il valore `pid` può essere:
- $<-1$.  Aspetta qualsiasi processo figlio il cui ID del gruppo di processi è uguale al valore assoluto di pid.
- $-1$. Aspetta qualsiasi processo figlio.
- $0$. Aspetta qualsiasi processo figlio il cui ID del gruppo di processi è uguale a quello del chiamante.
- $>0$. Aspetta qualsiasi figlio il cui pid è uguale al parametro `pid`.

Se `waitpid` avviene con successo allora restituisce il pid del figlio il quale stato è cambiato. 

#### Processo zombie
Un figlio che termina, ma non è stato "atteso" dal padre, diventa uno **zombie**. Il kernel mantiene delle informazioni relative al processo per consentire al padre di eseguire di eseguire una `wait` per ottenere informazioni sul figlio. Fiché uno zombie non viene rimosso dal sistema tramite una `wait`, consumerà una entry nel PCB del kernel e, se questa si riempie, non sarà possibile creare altri processi. Se un processo padre termina, i suoi figli "zombie" vengono **adottati** da `Init` (il padre di tutti i processi) che esegue automaticamente una `wait` per rimuovere gli zombie.

### execl
```c
int execl(const char *pathname, const char *arg, ... /* (char *) NULL */);
```
La famiglia di funzioni `exec` sostituisce l'immagine del processo corrente con una nuova immagine. Il parametro `pathname` è il nome del file da eseguire, mentre i vari `args` sono gli armomenti (`arg0`, `arg1`, ...). Il primo argomento, per convenzione, deve puntare al nome del file associato al file da eseguire. L'elenco degli argomenti deve essere terminato da un puntatore `NULL` castato a `char *`. Le funzioni `execl` restituiscono solo se si è verificato un valore, il valore di ritorno è -1 quando esso accade.

## Scambiare dati tra processi
### Pipe
```c
int pipe2(int pipefd[2], int flags);
```
pipe() creates a pipe, a unidirectional data channel that can be used for interprocess communication. The array pipefd is used to return two file descriptors referring to the ends of the pipe. pipefd[0] refers to the read end of the pipe. pipefd[1] refers to the write end of the pipe. Data written to the write end of the pipe is buffered by the kernel until it is read from the read end of the pipe. If a process attempts to read from an empty pipe, then read(2) will block until data is available. If a process attempts to write to a full pipe, then write(2) blocks until sufficient data has been read from the pipe to allow the write to complete. If all file descriptors referring to the write end of a pipe have been closed, then an attempt to read(2) from the pipe will see end-of-file (read(2) will return 0). If all file descriptors referring to the read end of a pipe have been closed, then a write(2) will cause a SIGPIPE signal to be generated for the calling process. If the calling process is ignoring this signal, then write(2) fails with the error EPIPE. Le scritture sulla pipe sono atomiche entro una certa dimensione che dipende dal sistema (in LINUX `e 4KB).

La system call `pipe` crea una pipe: un canale di dati unidirezionale che può essere utilizzato per la comunicazione tra processi. L'array `pipefd` viene utilizzato per restituire due file descriptor che si riferiscono alle estremità della pipe:
- `pipefd[0]`. Si riferisce all'estremità in **lettura** della pipe.
- `pipefd[1]`. Si riferisce all'estremità in **scrittura** della pipe.

I dati scritti all'estremità di scrittura vengono bufferizzati dal kernel finché non vengono letti dall'estremità in lettura della pipe. 
- Se un processo tenta di leggere da una pipe vuota, la system call `read` si blocca finché i dati non sono disponibili.
- Se un processo tenta di scrivere su una pipe piena, la system call `write` si blocca finché non vengono letti dati sufficienti per completare la scrittura.

- Se tutti i file descriptor che si riferiscono all'estremità di scrittura di una pipe sono chiusi, allora un tentativo di lettura (con `read`) dalla pipe vedrà solo EOF (la `read` restituirà 0).
- Se tutti i file descriptor che si riferiscono all'estremità di lettura di una pipe sono chiusi, allora  un tentativo di scrittura (con `write`) causerà un errore `SEGPIPE` per il processo chiamante.

oss. Le scritture sulla pipe sono atomiche entro una certa dimensione (dipende dal sistema).

### Pipe con nome (FIFO)
```c
int mkfifo(const char *pathname, mode_t mode);
```

La system call `mkfifo` crea un file speciale FIFO con nome `pathname`, il parametro `mode` specifica i permessi di FIFO che vengono modificati dall'`umask` del processo. 

Un file FIFO è simile a una pipe, ma viene creato in modo diverso. Invece di essere un canale di comunicazione anonimo, un file FIFO viene inserito nel filesystem. Una volta creato, qualsiasi processo può aprirlo in lettura o scrittura, allo stesso modo di un file normale. Tuttativa deve essere aperto simultaneamente da entrambe le estremità prima di poter eseguire qualsiasi operazione di I/O. L'apertura di un file FIFO in lettura normalmente si blocca finché un altro processo non apre lo stesso file FIFO in scrittura (e viceversa). In caso di successo `mkfifo` restituisce 0, altrimenti -1.

## Semafori
Un semaforo è un interno non negativo. È possibile eseguire due operazioni sui semafori:
- `sem_post` per incrementare il valore di 1.
- `sem_wait` per decrementare il valore di 1.

Se il valore di un semaforo è 0, allora se viene chiamata `sem_wait` il processo si blocca finché il valore non diventa maggiore di zero. I semafori POSIX sono disponibili con nome senza nome.

### Semafori con nome
Un semaforo con nome è identificato da un nome della forma `"/name"`, cioé una stringa composta da una barra iniziale e seguita da uno o più caratteri. Due processi possono operare sullo stesso semaforo passando lo stesso nome a `sem_open`. La funzione `sem_open` crea un nuovo semaforo con nome o apre un semaforo con nome esistente. Dopo che il semaforo è stato aperto, è possibile utilizzarlo con `sem_post` e `sem_wait`. Quando un processo ha finito di usare il semaforo può usare `sem_close` per chiuderlo. Quando tutti i processi hanno terminato di utilizzare il semaforo, questo può essere rimosso dal sistema utilizzando `sem_unlink`.

### Semafori senza nome
Un semaforo senza nome non ha un nome. Il semaforo è invece collocato in una regione di memoria condivisa tra più processi. Un semaforo condiviso da un processo è collocato in un'area di memoria condivisa tra i processi, ad esempio una variabile globale. Prima di essere utilizzato, un semaforo senza nome deve essere inizializzato con `sem_init`.
```c
int sem_init(sem_t *sem, int pshared, unsigned int value);
```

La funzione `sem_init` inizializza il semaforo senza nome (già esistente) all'indirizzo indicato da `sem`. L'argomento `value` specifica il valore iniziale del semaforo mentre `pshared` indica se il semaforo deve essere condifiso tra i thread di un processo o tra i processi. 
- Se `pshared` è 0 allora il semaforo è condiviso tra i thread.
- Se `pshared` è 1 allora il semaforo è condiviso tra i processi.

Possiamo utilizzare il semaforo per `sem_post` e `sem_wait` e, quando non serve più e prima che la memoria in cui si trova sia deallocata, deve essere distrutto con `sem_destroy`.
