## Dinamiche del gioco
Il gioco si svolge in round, ad ogni round un giocatore può:
- **Combattere col mostro.** il combattimento si conclude *decrementando* la salute del mostro e del giocatore. Sia $SG$ il livello di salute attuale del giocatore, tale livello viene decrementato di un valore random $X$ con $0\leq X<SG$. Stessa cosa per la salute del mostro $SM$, si genera un valore casuale $K$ con $0\leq K <SM$ che viene sottratto alla salute.
- **Bere una parte della pozione.** la salute del giocatore viene incrementata di un valore proporzionale alla quantità di pozione bevuta $P$ generato randomicamente.
- **Uscire dal gioco.** in questo caso la partita viene considerata persa per il giocatore.

1. Il combattimento si conclude quando il giocatore o il mostro (anche entrambi) muoiono (hanno un valore di salute pari a 0).
2. Se il giocatore ha **vinto** o **pareggiato** $\implies$ può chiedere di giocare nuovamente.
3. Se il giocatore ha **perso** $\implies$ deve uscire dal gioco.

## Consegna
Sviluppare un'applicazione client-server che implementi il gioco descritto sopra.
```ad-info
title: Server
- Il server riceve richieste di gioco da parte dei client.
- Gestisce ogni connessione in un diverso thread.
```

```ad-info
title: Server's threads
Ogni thread riceve comandi dal client e li esegue.
- Nel caso del comando `combattere` simula il comportamento del mostro assegnato al client.
- Dopo aver eseguito ogni comando ne comunica al client l'esito.
- Comunica al client l'eventuale terminazione del gioco, insieme all'esito.
```

```ad-info
title: Clients
Il client si connette al server.
- Chiede iterativamente all'utente il comando da eseguire e lo invia al server. 
I comandi sono:
	1. combatti
	2. bevi pozione
	3. esci dal gioco
- Attende un messaggio che segnala l'esito del comando.
- In caso di vittoria, chiede all'utente se intende continuare a giocare e lo comunica al server.
```
## Idee
- La salute del giocatore non viene ripristinata una volta battuto un mostro, viene aggiornata quella iniziale prima di permettere al giocatore di combattere di nuovo.
- La pozione per la salute è una sola e contiene al massimo 75 HP.
- Ogni mostro abbattuto ricarica di un terzo la pozione della salute (25 HP), ma non può andare oltre 75 HP. e.g $pozione=50 \underbrace{\implies}_{vittoria} pozione=75$.