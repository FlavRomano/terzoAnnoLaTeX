## Albero binario di ricerca
Albero binario etichettato in cui per ogni nodo:
- il sottoalbero sinistro contiene etichette con **valori minori** di quella del nodo
- il sottoalbero destro contiene etichette maggiori

Il tempo di ricerca e inserimento è **logaritmico** al caso medio.

## Albero di ricerca di ordine P
È un albero di ricerca in cui ogni nodo 
- ha **$P$ figli**
- e le etichette fino a $P-1$ sono **ordinate**

 un albero i cui nodi contengono al più $P-1$ **valori chiave** (*search value*) e $P$ puntatori nel seguente ordine 
 $$\langle P_{1},K_{1},P_{2},K_{2},\ldots,P_{q-1},K_{q-1},P_{q}\rangle\quad\text{con }q\le p$$

- ogni $P_{i}$ è un **puntatore ad un nodo figlio** (o puntatore nullo)
- ogni $K_{i}$ è un valore chiave appartenente ad un insieme totalmente ordinato

![[Pasted image 20230830171456.png]]

### Vincoli fondamentali
Ogni albero di ricerca deve soddisfare due vincoli:
1. In ogni nodo deve valere $$K_{1}<K_{2}<\ldots<K_{q-1}$$
2. Per tutti i valori di $X$ presenti nel sottoalbero puntato da $P_{i}$ vale 
	1. $X< K_{i}$ per $i=1$
	2. $K_{i-1}< X< K_{i}$ per $1<i<q$
	3. $K_{i-1}< X$ per $i=q$

dove $q$ è l'ultima chiave.

### A che serve?
Un albero di ricerca
- può essere utilizzato **per cercare record** memorizzati su disco. 
- ogni **ricerca/modifica** comporta la visita di un **cammino radice-foglia**

I valori di ricerca 
- possono essere i valori di **uno dei campi del file**
- ad ogn'uno di essi è associato un **puntatore**:
	- al record avente quel valore nel file di dati
	- oppure al blocco contenente quel record

L'albero stesso può essere memorizzato su disco, assegnando ad ogni nodo dell'albero una **pagina**.

L'albero di ricerca deve **essere aggiornato** all'inserimento di un **nuovo record**:
- includendo il valore del campo di ricerca del nuovo record
- col relativo puntatore al record (o alla pagina che lo contiene) nell'albero di ricerca.

Per **inserire** valori di ricerca nell'albero
- sono necessari specifici algoritmi che garantiscano i due [[B-albero#Vincoli fondamentali|vincoli]]
- in generale però non garantiscono che un albero di ricerca sia **bilanciato**
	- cioè che abbia tutti i nodi foglia **allo stesso livello**

## B-Alberi
Un **B-albero di ordine $p$** se usato come struttura di accesso su un campo chiave
- deve soddisfare le seguenti condizioni:
	1. Ogni **nodo interno** del B-albero ha la forma  $$\langle P_{1},K_{1},P_{2},K_{2},\ldots,P_{q-1},K_{q-1},P_{q}\rangle\quad\text{con }q\le p$$ proprio come visto in precedenza, in particolare
		- $P_{i}$ è un **tree pointer**, cioè puntatore ad un altro nodo del B-Albero
		- $K_{i}$ è la **chiave di ricerca**
		- $Pr_{i}$ è un **data pointer**, cioè un puntatore ad un record il cui campo chiave di ricerca **è uguale a** $K_{i}$ (o alla pagina che contiene tale record)
	2. Per ogni nodo si ha che $$K_{1}<K_{2}<\ldots<K_{q-1}$$
	3. Ogni nodo ha al più $p$ tree pointer

![[Pasted image 20230830184742.png]]

Ancora, per tutti i valori $X$ della chiave di ricerca appartenenti al sottoalbero puntato da $P_{i}$ si ha che
1. $X< K_{i}$ per $i=1$
2. $K_{i-1}< X< K_{i}$ per $1<i<q$
3. $K_{i-1}< X$ per $i=q$

- la **radice**
	- ha almeno 2 *tree pointer* (uno per ogni lato della chiave)
	- a meno che non sia l'unico nodo dell'albero
- ogni **nodo**, esclusa la radice
	- ha almeno $\lceil \frac{p}{2}\rceil$ *tree pointer*
	- in generale ogni nodo con $q$ *tree pointer*, dove $q\le p$, 
		- ha $q-1$ campi **chiave di ricerca** 
		- e $q-1$ *data pointer*
- tutti i **nodi foglia** sono posti allo stesso livello 
	- hanno la stessa struttura dei nodi interni
	- però tutti i loro *tree pointer* $P_{i}$ sono nulli, ovviamente

Quindi vengono soddisfatti i [[B-albero#Vincoli fondamentali|vincoli fondamentali]].
Non solo, essendo tutti i nodi foglia allo stesso livello il B-albero è **bilanciato**, quindi **ricerca** e **inserimento** hanno una **complessità logaritmica** rispetto al numero di chiavi nell'albero.

## $B^{+}$-Alberi
Un $B^{+}$-Albero è un [[B-albero#B-Alberi|B-albero]] in cui 
- i *data pointer* sono **memorizzati solo nei nodi foglia dell'albero**
- la struttura dei nodi foglia **differisce** quella del B-albero

In particolare:
- se il **campo di ricerca** è **un campo chiave**
	- i nodi foglia hanno per ogni valore del campo di ricerca una **entry** e un **puntatore** ad un record.
- altrimenti
	- i puntatori **indirizzano un blocco** che contiene i **puntatori ai record** del file di dati
	- è necessario un passo aggiuntivo per l'accesso ai dati

In generale i **nodi foglia** sono messi fra loro in **relazione**
- essi corrispondono al primo livello di un indice
- i **nodi interni** corrispondo agli altri livelli di un indice
- questo viene sfruttato nel caso di *range query*

Alcuni valori del **campo di ricerca** presenti nei **nodi foglia** sono **ripetuti** nei **nodi interni** per **guidare la ricerca**.

### Struttura nodi interni
La struttura dei **nodi interni** (di ordine $p$) di un $B^{+}$-albero è la seguente:
1. Ogni nodo interno del $B^{+}$-albero ha la forma $$\langle P_{1},K_{1},P_{2},K_{2},\ldots,P_{q-1},K_{q-1},P_{q}\rangle\quad\text{con }q\le p$$ ogni $P_{i}$ è un *tree pointer*
2. Per ogni nodo interno si ha che $$K_{1}<K_{2}<\ldots<K_{q-1}$$
3. Ogni nodo interno ha al più $p$ *tree pointer*
4. Per tutti i valori $X$ della chiave di ricerca del sottoalbero puntato da $P_{i}$ si ha che 
	1. $K_{i-1}X \le K_{i}$ per $1<i<q$ (non più minore stretto)
	2. $X\le K_{i}$ per $i=1$ (non più minore stretto)
	3. $K_{i-1}< X$ per $i=q$
5. Ogni nodo interno, **esclusa la radice** ha almeno $$\left\lceil \frac{p}{2} \right\rceil\quad\text{tree pointer}$$ la **radice** ha almeno 2 *tree pointer* se è un nodo interno
6. Un nodo interno con $q$ *tree pointer*, con $q\le p$, ha $q-1$ **campi di ricerca**

![[Pasted image 20230830190447.png]]

### Struttura nodi foglia
La struttura dei **nodi foglia** di un $B^{+}$-albero è la seguente:
1. Ogni nodo foglia è della forma $$\left\langle \langle K_{1},Pr_{1}\rangle, \langle K_{2},Pr_{2}\rangle, \ldots, \langle K_{q},Pr_{q}\rangle P_{next} \right\rangle$$ dove $q\le p_{leaf}$ e per ogni nodo si ha che $$K_{1}< K_{2}<\ldots< K_{q}$$
	- $P_{next}$ è un *tree pointer* che punta alla **successiva foglia** dell'albero
	- ogni $Pr_{i}$ è un *data pointer* che punta al record con valore del campo di ricerca uguale a $K_{i}$  (oppure a un blocco di puntatori ai record con valore del campo di ricerca uguale a $K_{i}$, se il campo di ricerca non è una chiave)
2. Ogni nodo foglia ha almeno $$\left\lceil \frac{p_{leaf}}{2}\right\rceil \quad\text{valori}$$
3. Tutti i nodi foglia **sono dello stesso livello**

![[Pasted image 20230830191401.png]]