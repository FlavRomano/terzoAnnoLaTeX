Dato lo schema $R\langle T, F\rangle$ diremo che un insieme di attributi $W\subseteq T$ è una **chiave candidata** di $R$ se 
- $W\to F$ appartiene alla chiusura di $F^{+}$, quindi $W$ è [[chiave#Superchiave|superchiave]] $$W\to T\in F^{+}$$
- Per ogni sottoinsieme di attributi $V$ non definitivamente uguale a $W$, $V\to T$ non appartiene alla chiusura di $F^{+}$, $V$ **non è superchiave** $$\forall \; V\subset W.\; V\to T \notin F^{+}$$

oss: nell'esempio sopra, $AD$ è chiaramente una chiave candidata
