L'algoritmo per trovare tutte le chiavi si basa su due proprietà:
1. se un attributo $A$ di $T$ **non appare a destra di alcuna [[dipendenza funzionale]]** di $F$ allora $A$ **appartiene** ad ogni chiave di $R$
2. se un attributo $A$ di $T$ **appare a destra di almeno una [[dipendenza funzionale]]** di $F$ ma **non appare a sinistra di alcuna dipendenza non banale** allora $A$ **non appartiene** ad alcuna [[problema dell'implicazione#Chiave candidata|chiave]]

Quindi per **trovare tutte le chiavi**
- si può partire dall'insieme $X$ degli **attributi che non appaiono a destra di alcuna dipendenza** di $F$ 
- e applicare le due proprietà

## Algoritmo 
Per trovare tutte le chiavi di $R\langle T, F \rangle$ si parte da un insieme di candidati pari all'insieme $T$ **meno tutte le parti destre delle dipendenze funzionali in $F$**
$$\texttt{Base} = T \setminus \texttt{partiDestre}(F)$$ Ogni candidato è un **sottoinsieme di $T$** rappresentato come $$X\text{::}{Y}\subseteq T$$ che denota
- dato $Y=\{A_{1},\ldots,A_{n}\}$ **tutti gli insiemi** formati da **$X$** e da **qualsiasi insieme di attributi $A_{i}$**

Sapendo tutto questo
- se $\texttt{Base}$ sono gli attributi **che non appaiono a destra** di nessuna dipendenza 
	- questi attributi **devono apparire in ogni chiave**
- per cui inizialmente i candidati saranno $$Candidati = \texttt{Base}\text{:: }T\setminus \texttt{Base} $$
- ogni insieme in $X\text{::} Y$  è analizzato partendo da $X$
	- se **è già chiave** allora **tutti gli altri insiemi** $X\text{::} Y$ sono scartati
	- altrimenti si mettono in $Candidati$ $$XA_{1}{::}(Y\setminus A_{1}),\ldots, XA_{n}{::}(Y\setminus A_{n})$$
- se $X$ non contiene le chiavi già trovate in precedenza e $X^{+}=T$  allora **$X$ è chiave**

Oss: le chiavi trovate dopo saranno per forza più lunghe, non potranno essere contenute in una chiave già trovata.
- questo si assicura aggiungendo i **nuovi candidati in coda alla lista** (`append`)
	- mantenendo quindi la lista dei **candidati ordinata per lunghezza**.

### Esempio 
Con $T=\{ A,B,C,D,E,F \}$ e $\overline F = \{C\to D, \;CF\to B, \;D\to C,\; F \to E\}$

Si parte col trovare la base $$Base = T \setminus \texttt{partiDestre}(\overline {F}) = \{A,F\}$$ e quindi i candidati $$Candidati = Base\text : \text : T \setminus Base = AF::T\setminus AF = \{AF::BCDE\}$$

Verifichiamo se nell'insieme sono presenti chiavi, qui abbiamo solo $AF$. Calcoliamone la [[problema dell'implicazione#Algoritmo per calcolare la chiusura di $X$ rispetto a $F$|chiusura]]
$$\{AF\}^{+} = AFE \implies \text{non è chiave}$$ Quindi si prosegue ad aggiungere nuovi candidati $$Candidati = \{AF::(BCDE \setminus AFE)\} = \{AF::BCD\} = \{ AFB::CD,AFC::D, AFD \}$$

Verifichiamo se nell'insieme dei candidati sono presenti chiavi:
- $AFB$, calcoliamone la chiusura $$\{AFB\}^{+}=AFBE\implies \text{non è chiave}$$
	- Quindi modifichiamo $$Candidati = \{ AFB::(CD\setminus AFBE),\ldots \} =\{AFB::CD,\ldots\}$$
- $AFC$, calcoliamone la chiusura $$\{AFC\}^{+}=AFCDBE\implies \text{E' CHIAVE}$$
	- Quindi modifichiamo $$Candidati =\{AFB::CD,AFC::D, AFD \}\setminus \{AFC::D\}=\{AFB::CD, AFD\}$$
- $AFD$, calcoliamone la chiusura $$\{AFD\}^{+}=AFDC\supseteq AFC \implies \text{E' CHIAVE}$$
	- Quindi modifichiamo $$Candidati =\{AFB::CD, AFD\}\setminus \{AFD\}=\{AFB::CD\}$$

Quindi $Candidati =\{AFB::CD\}$.


