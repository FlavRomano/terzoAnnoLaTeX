L'algoritmo per trovare tutte le chiavi si basa su due proprietà:
1. se un attributo $A$ di $T$ **non appare a destra di alcuna [[dipendenza funzionale]]** di $F$ allora $A$ **appartiene** ad ogni chiave di $R$
2. se un attributo $A$ di $T$ **appare a destra di almeno una [[dipendenza funzionale]]** di $F$ ma **non appare a sinistra di alcuna dipendenza non banale** allora $A$ **non appartiene** ad alcuna [[problema dell'implicazione#Chiave candidata|chiave]]

Quindi per **trovare tutte le chiavi**
- si può partire dall'insieme $X$ degli **attributi che non appaiono a destra di alcuna dipendenza** di $F$ 
- e applicare le due proprietà

## Algoritmo 
Per trovare tutte le chiavi di $R\langle T, F \rangle$ si parte da un insieme di candidati pari all'insieme $T$ **meno tutte le parti destre delle dipendenze funzionali in $F$**
$$X = T \setminus \texttt{partiDestre}(F)$$ 
- Dalla proprietà (1), segue che $$X^{+}= T \implies \texttt{Base}\text{ è chiave}$$
- Se invece $X^{+}\neq T$ allora occorre **aggiungere attributi a $X$**.
	- occorre aggiungere quegli attributi $W\subseteq T$ che appaiono a destra di qualche dipendenza e a sinistra di qualche altra, uno alla volta.
	- ad ogni passo occorre **evitare di aggiungere** attributi che siano **già nella chiusura di $X$**, perché 
		- o sono attributi ridondanti
		- oppure producono un insieme $X'$ che contiene una chiave già trovata in precedenza.
	- si calcola la chiusura di ogni $X'$ fino a quando questa non coincide con $T$, il che garantisce che $X'$ **sia chiave**

### Esempio
Sia $R\langle T, F\rangle$ con $T= \{ A, B, C, D, E ,G \}$ ed $F = \{AB\to C, BC\to AD, D\to E, CG\to B\}$

1. $X = T \setminus \texttt{partiDestre}(F) = \{G\}$, calcoliamo la chiusura di $X$: $$X^{+}= G^{+}= G$$
2. Aggiungiamo un attributo di $W = \{A,B,C,D\}$ a $G$: $$\begin{align} GA^{+} &=GA\neq T\\
 GB^{+}&=GB\neq T \\ GC^{+}&=GCBADE=T\implies GC\text{ è una chiave di }R\\
GD^{+}&=GDE\neq T\end{align}$$
1. Proviamo ad aggiungere a $GA$, $GB$ e $GD$ (i *candidati*) un altro attributo di $W = \{ B,D \}$, considerando stavolta solo insiemi di attributi che non contengono la chiave $GC$: $$\begin{align} GAB^{+}&=GABCDE = T\implies GAB \text{ è una chiave di }R \\ GAD^{+}&= GADE\neq T\\ GBD^{+}&=GBDE\neq T\end{align}$$
2. Proviamo ad aggiungere a $GAD$ e $GBD$ un altro attributo di $W = W\setminus \{GC\} \setminus \{GAB\}$, togliendo le chiavi $GC$ e $GAB$ otteniamo un $W = \emptyset$ e quindi si conclude che non esistono altre chiavi. Termina perché **non abbiamo più candidati**.
 
Ogni candidato è un **sottoinsieme di $T$** rappresentato come $$X\text{::}{Y}\subseteq T$$ che denota
- dato $Y=\{A_{1},\ldots,A_{n}\}$ **tutti gli insiemi** formati da **$X$ unito** a **qualsiasi insieme di attributi $A_{i}$**
	- e.g $AB\;\text {::}\;CD$ rappresenta $\{AB, ABC, ABD, ABCD\}$.

Sapendo tutto questo
- se $\texttt{noDes}$ sono gli attributi **che non appaiono a destra** di nessuna dipendenza 
	- questi attributi **devono apparire in ogni chiave**
- per cui inizialmente i candidati saranno $$Candidati = \texttt{noDes}\text{:: } \texttt{sinDes} $$
- ogni insieme in $X\text{::} Y$  è analizzato partendo da $X$
	- se **è già chiave** allora **tutti gli altri insiemi** $X\text{::} Y$ sono scartati
	- altrimenti si mettono in $Candidati$ $$XA_{1}{::}(Y\setminus A_{1}),\ldots, XA_{n}{::}(Y\setminus A_{n})$$
- se $X$ non contiene le chiavi già trovate in precedenza e $X^{+}=T$  allora **$X$ è chiave**

Oss: le chiavi trovate dopo saranno per forza più lunghe, non potranno essere contenute in una chiave già trovata.
- questo si assicura aggiungendo i **nuovi candidati in coda alla lista** (`append`)
	- mantenendo quindi la lista dei **candidati ordinata per lunghezza**.


