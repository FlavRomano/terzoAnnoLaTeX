Combinando [[Selezione|selezione]] e [[Proiezione|proiezione]] possiamo estrarre informazioni da una relazione, ma abbiamo un problema:
- ma **non possiamo** correlare informazioni presenti in relazioni diverse

Per questo nasce il **join**, che consente di correlare i dati in relazioni diverse.

## Join naturale
Il Join naturale è
- un **operatore binario generalizzabile**
- che **correla dati** in relazioni diverse sulla base di **valori uguali** in **attributi con lo stesso nome**
- produce un risultato 
	- sull'unione degli attributi degli operandi
	- con ennuple ottenute combinando
		- le ennuple degli operandi con valori uguali sugli attributi in comune

Date le relazioni $R_{1}(X_{1})$, $R_{2}(X_{2})$, allora $R_{1}\bowtie R_{2}$ è una relazione su $X_{1}\cup X_{2}$
$$R_{1}\bowtie R_{2} = \{ t \text{ su } X_{1}\cup X_{2} \mid \exists \; t_{1}\in R_{1}\text{ e } t_{2}\in R_{2}\text{ con }t[X_{1}]=t_{1}\text{ e }t[X_2]=t_{2}\}$$

### Esempi
- $R_{1}\bowtie R_{2}$ 
![[Pasted image 20230820180525.png]]
![[Pasted image 20230820181129.png]]
ogni  ennupla contribuisce al risultato (**join completo**)
- Questo invece è un **join non completo**
![[Pasted image 20230820180634.png]]
abbiamo perdita di informazioni, le ennuple che non contribuiscono al risultato vengono tagliate fuori.
- Questo è un **join vuoto**
![[Pasted image 20230820180948.png]]
le due relazioni non hanno attributi correlati

## Cardinalità 
Dati $R_{1}(A,B)$ e $R_{2}(B,C)$ il join contiene un numero di ennuple **compreso** fra 0 e $\lvert R_{1}\rvert \cdot\lvert R_{2} \rvert$   
$$0\leq \lvert R_{1}\bowtie R_{2}\rvert \leq \lvert R_{1}\rvert \cdot \lvert R_{2}\rvert$$
- Se il **join è completo** allora contiene un numero di ennuple almeno uguale a $$\lvert R_{1}\bowtie R_{2}\rvert=\max\{ \lvert R_{1} \rvert, \lvert R_{2} \rvert \}$$
- Se il **join coinvolge una chiave** $B$ di $R_{2}$ allora $$0 \le \lvert R_{1}\bowtie R_{2} \rvert \leq \lvert R_{1} \rvert$$
- Se il **join coinvolge una chiave** $B$ di $R_{2}$ e un **[[vincolo di integrità referenziale]]** tra attributi di $R_{1}$ e la chiave di $R_{2}$ allora $$\lvert R_{1}\bowtie R_{2} \rvert =\lvert R_{1} \rvert$$

È importante osservare gli effetti di [[Proiezioni e join|join e proiezioni insieme]]

## Join esterno
Il **join esterno**
- estende con [[Valore nullo|valori nulli]] le ennuple che verrebbero **tagliate fuori** da un join interno.

Ne esistono tre versioni:
1. **Join sinistro**, mantiene tutte le ennuple del primo operando, estendendo tramite l'aggiunta di `NULL` se neccessario $$R \overset{←}{\bowtie}S$$
2. **Join destro**, idem ma del secondo operando $$R \overset{→}{\bowtie}S$$
3. **Completo**, idem ma di entrambi gli operandi $$R \overset{⟷}{\bowtie}S$$

### Esempi
1. Join sinistro ![[Pasted image 20230820182421.png]]
2. Join destro ![[Pasted image 20230820182516.png]]
3. Join completo ![[Pasted image 20230820182556.png]]