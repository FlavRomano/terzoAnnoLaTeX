Quando bisogna effettuare una ricerca su *range di valori* e *ricerche su chiave*, si opta per il **metodo tabellare**:
- si usa un **indice**: un **insieme ordinato** di coppie $$(k, r(k))$$  $$k = \text{valore della chiave} \quad r(k) = \text{riferimento al record con chiave }k$$
- contiene informazioni sulla posizione di **memorizzazione delle tuple** sulla base del valore del campo $k$
- la realizzazione di indici avviene tipicamente attraverso **strutture ad albero multi livello**.

### Indice
L'[[indice]] è gestito con una struttura ad albero detta [[B-albero#$B {+}$-Alberi|B+ albero]].
A partire da un valore $v$
- permette di trovare con **pochi accessi** i record della relazione $R$ in cui il valore di un attributo $A$ è in una relazione specificata con $v$

Abbiamo due tipologie di indici ad albero:
1. **Indici STATICI**, dove la struttura ad albero viene creata sulla base dei dati presenti nel DB e **non viene più modificata** (o viene modificata di rado)
2. **Indici DINAMICI**, dove la struttura ad albero **viene aggiornata ad ogni operazione** sulla base di dati di inserimento, o di cancellazione, preservando le prestazioni senza riorganizzazioni.

In base a cosa coincide la chiave di ordinamento, distinguiamo
- [[indice#Indice primario]]
- [[indice#Indice secondario]]