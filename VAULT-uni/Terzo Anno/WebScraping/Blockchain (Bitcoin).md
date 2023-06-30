
Table of contents

1. [[#Struttura|Struttura]]
	1. [[#Struttura#Cosa contiene un blocco|Cosa contiene un blocco]]
1. [[#Bitcoin|Bitcoin]]
	1. [[#Bitcoin#Perché?|Perché?]]
		1. [[#Perché?#UTXO|UTXO]]
	1. [[#Bitcoin#Struttura|Struttura]]
	1. [[#Bitcoin#Modello UTXO|Modello UTXO]]
		1. [[#Modello UTXO#Transazioni Coinbase|Transazioni Coinbase]]
	1. [[#Bitcoin#Transazione bitcoin for dummies|Transazione bitcoin for dummies]]
	1. [[#Bitcoin#Esempio più serio|Esempio più serio]]
	1. [[#Bitcoin#Transaction Fee (commissioni)|Transaction Fee (commissioni)]]
	1. [[#Bitcoin#Dove sono realmente queste pile di bitcoin?|Dove sono realmente queste pile di bitcoin?]]
	1. [[#Bitcoin#In teoria UTXO|In teoria UTXO]]
		1. [[#In teoria UTXO#Tipi di Transazioni|Tipi di Transazioni]]
	1. [[#Bitcoin#Locking my BTC|Locking my BTC]]


La blockchain è
- la "killer application P2P", 
- un libro mastro distribuito che viene replicato tra i nodi di una rete peer-to-peer
- come un "notaio" 
- caratterizzato dalla proprietà "tamper freeness" (anti-manomissione).

## Struttura
### Cosa contiene un blocco
Dati, hash del blocco corrente, hash del blocco precedente.
- Che tipo di dati possiamo trovare in un blocco?
	- Transazioni (per quanto riguarda le cryptocurrencies)
	- Supply chain
	- Asset certification (NFT)
- Tutti i dati sono pubblici

L'hash del blocco precedente permette a tutti gli effetti di creare una catena. 

## Bitcoin
### Perché?
È la killer application per la blockchain
- Criptovaluta inventata da Satoshi Nakamoto nel 2009
- Moneta digitale decentralizzata
- Senza nessuna autorità centrale 
	- nessuna banca centrale o stato emette o garantisce la valuta
- Protocollo ancora in evoluzione

Perché analizzeremo dati provenienti dalla blockchain di bitcoin?
- ha di gran lunga la più alta capitalizzazione
- molte criptovalute si basano su concetti simili
- fa parte della classe di blockchain basate su **UTXO**

#### UTXO
**Unspent Transaction Output**
- Modello adottato da molte criptovalute (litecoin, solana, cardano)

### Struttura
Una transazione bancaria è del tipo $$FROM\text{ [account1] }TO\text{ [account2] }AMOUNT\text{ [10]}$$
cioè un trasferimento monetario da un account all'altro. L'indirizzo di bitcoin è concettualmente un numero di conto bancario?
- in un certo senso è simile a un numero di conto bancario
- ma c'è un'importante differenza
	- tiene separati i bitcoin **ricevuti da diverse transazioni**
	- è come avere un salvadanaio diverso per ogni lotto di bitcoin ricevuto dall'indirizzo da diverse transazioni
	- un singolo indirizzo, con diversi lotti di bitcoin
	- indirizzi multipli, per diversi lotti di bitcoin.

```
ADDRESS: mm6toLuezPeXx4ZsXvQKdBBVWAHoTQJUPV

			   Pila 1      Pila 2
Transaction 1: === (3)     ====== (6)
Transaction 2: === (3)     === (3)
Transaction 3: ====== (6)  =========== (11)

come avere pile di monete diverse per i Bitcoin ricevuti da diverse transazioni
```

### Modello UTXO
Un utente riceve bitcoin in lotti
- Quando l'utente desidera pagare qualcosa, crea una **transazione**
	- utilizza un insieme di stack di Bitcoin per creare **un nuovo stack** da inviare al destinatario.
	- prende l'intero importo da uno o più stack e lo invia all'indirizzo del destinatario.
	- il destinatario mantiene la pila di Bitcoin ricevuta separata dalle altre all'interno del proprio indirizzo 

```
Sender ADDRESS                  Receiver ADDRESS
Pila 1   Pila 2  transaction    Pila 1  Pila 2   Pila 3  Pila 4
===      ======  ----------->   ===     ======   ===     ===
					/
===      ===     --/            === === } sono state inviate da altre transazioni   
                                          all'indirizzo del Receiver
====     =======

Quando l'utente (Sender) vuole pagare qualcosa
- Crea una transazione prendendo 2 lotti (pile) di Bitcoin e li invia al destinatario (Receiver).
- I bitcoin ricevuti sono una pila separata nell'indirizzo del Receiver
```

#### Transazioni Coinbase
Sono transazioni senza input, sono **nuovi bitcoin** inviati ai miner come ricompensa per aver risolto la **proof of work** .
- "Nuovi" perché sono generati *freschi* dal sistema.

### Transazione bitcoin for dummies

![[utxo1.png]]

![[utxo2.png]]

Nel modello UTXO, si deve spendere l'intero lotto di 6.25 BTC in un transaction output
- creando un nuovo lotto si può lasciare un resto nello stesso indirizzo
	- una nuova transaction output.
- il lotto di bitcoin viene diviso e spedito a due destinazioni
	1. Il negozio di gelati (il pagamento **ROSA**)
	2. Al nostro indirizzo (il resto **BLU**)
- il lotto originario di 6.25 BTC è stato **esaurito**, non esiste più.

Questo è ciò che il sistema di transazioni BTC è progettato per fare:
- prendere un output esistente (lotto di bitcoin)
- creare nuovi output (lotti di bitcoin) a partire da esso
- inviare questi output a indirizzi diversi.

Generalmente il totale dei lotti può raggiungere un importo **superiore** a quello che l'utente desidera spendere.
- in questo caso basta aggiungere un altro output alla transazione
	- rimandare la differenza a questo output (come un resto)
- generare un nuovo salvadanaio per il resto

![[utxo3.png]]

### Esempio più serio
Il negozio decide di acquistare una nuova macchina per gelati per 3.10 BTC

![[utxo4.png]]

- la gelateria non ha nessun lotto al suo indirizzo in grado di coprire il costo della macchina
- raccoglie una manciata di output per avere una somma > 3.10
- l'output raccolto dai lotti precedenti (output della transazione) è l'input per questa transazione



![[utxo5.png]]

Gli output non spesi (riquadri **VIOLA**) sono ancora buoni per essere spesi.
- sono chiamati **Unspent Transaction Outputs** (UTXO)
- il numero totale di bitcoin di un indirizzo è la somma degli UTXO dell'indirizzo stesso

### Transaction Fee (commissioni)
In questa transazione il totale degli output è inferiore al totale degli input
- 3.25 BTC come input
- 3.2499 BTC come output

![[utxo6.png]]

Ci sono alcuni bitcoin rimanenti che non vengono utilizzati. Questo "residuo" è la fee (commissione) di transazione.
- sono raccolte dai miner quando estraggono un blocco
- l'aggiunta di una commissione di transazione è fondamentalmente un incentivo per i miner a includere la vostra transazione nel blocco. 
	- Dà priorità alla transazione.
- senza una commissione, le transazioni impiegheranno probabilmente un po' di tempo per essere incluse in un blocco.

Le transazioni sono memorizzate sulla blockchain
- Gli output della transazione sono come slot sulla blockchain che memorizzano "pile di bitcoin" da spendere.

### Dove sono realmente queste pile di bitcoin?
Sono memorizzati nell'output delle transazioni, che sono registrate sui blocchi della blockchain.

Per inviare bitcoin a qualcuno, 
- bisogna prenderli dall'output delle transazioni precedenti (un lotto di bitcoin ricevuti in precedenza) che li ha inviati.
- l'input di una transazione prende una pila di btc dagli output precedenti.

![[catena_btc.png]]

una catena di proprietà viene registrata sulla blockchain 
- il valore viene spostato da un indirizzo all'altro....

### In teoria UTXO
È un modello che
- mappa input address a output address
- ogni input spende un output di transazioni precedenti
- tipicamente una transazione consiste di 1 input, 2 output
- solo gli unspent output sono significativi (sono quelli mantenuti nel modello)

#### Tipi di Transazioni
1. Common UTXO
![[common_utxo.png]]
la forma più comune di transazione 
- un semplice pagamento da un indirizzo ad un altro
- spesso include un resto, restituito all'indirizzo originale
2. Aggregating funds
![[aggregating_utxo.png]]
transazione che aggrega diversi input in un unico output
- l'equivalente di scambiare un mucchio di monete con un'unica banconota più grande
- può essere generato per ripulire molti importi più piccoli che sono stati ricevuti come resto per i pagamenti (generati da applicazioni wallet)
- è un merge di fondi appartenenti allo stesso utente nell'output della transazione, 
	- viene sfruttata anche per i pagamenti congiunti (transazioni a più firme)
3. Distributing funds
![[distributing_utxo.png]]
transazioni che distribuiscono un input a più output che rappresentano più destinatari
- utilizzato per distribuire i fondi
- e.g pagare buste paga a più dipendenti

### Locking my BTC
Effettuare una transazione significa semplicemente inviare una serie di dati alla rete Bitcoin.
- perché qualcuno non può costruire una transazione che prenda il mio indirizzo e lo usi per inviare bitcoin al suo indirizzo?
- Cosa impedisce a qualcun altro di spendere i miei bitcoin? Potrebbe essere molto semplice, perché il mio indirizzo è pubblico.

Bitcoin utilizza un meccanismo di chiusura basato sulla crittografia asimmetrica
- bloccare ogni pila di bitcoin (output della transazione)
- utilizza tecniche di crittografia asimmetrica (chiavi pubbliche/private)

> un indirizzo A1 invia alcuni bitcoin ad un altro indirizzo A2
- A1 sblocca alcuni dei propri bitcoin
	- A1 mette un piccolo pezzo di codice (script) sopra i bitcoin inviati per bloccare i bitcoin trasferiti
- quando A2 vuole spendere i suoi bitcoin deve sbloccarli 
	- fornire un altro piccolo pezzo di codice per sbloccarli
- eseguire il codice di blocco insieme al codice di sblocco consente ad A2 di spendere i bitcoin ricevuti

- un importo di bitcoin viene generalmente inviato a un indirizzo bitcoin, che è l'hash della chiave pubblica
- uno scenario più semplice
	- inviare bitcoin a una chiave pubblica

![[transaction.png]]

B notifica la sua chiave pubblica ad A
- A invia i suoi bitcoin alla chiave pubblica di B
	- A sblocca alcuni dei suoi bitcoin, che erano stati bloccati da una precedente transazione
	- crea un blocco sui bitcoin inviati a B (alla chiave pubblica di B)
	- solo B sarà in grado di sbloccare il gruppo di bitcoin ricevuti, con la sua chiave privata

![[unlockingBTC.png]]