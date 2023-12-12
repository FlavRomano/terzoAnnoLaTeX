Una relazione $r$ è in **terza forma normale** 3NF 
- se per ogni [[dipendenza funzionale]] non banale $X\to Y$ definita su $r$, è verificata almeno una delle seguenti condizioni:
	- $X$ contiene una superchiave $K$ di $r$, come nella [[forma normale di Boyce Codd]]
	- **oppure** ogni attributo in $Y$ è contenuto in **almeno** una chiave $K$ di $r$

L'*oppure* indebolisce la normalizzazione, potrebbero esserci delle anomalie ma di sicuro preserviamo dati e dipendenze sempre.

### Esempio
![[Pasted image 20230828115754.png]]

$$Progetto,Sede\to Dirigente$$
$$Dirigente\to Sede$$

- Nella prima dipendenza funzionale va tutto bene, il membro sinistro è [[chiave#Superchiave|superchiave]]
- Nella seconda dipendenza funzionale il membro destro $Sede$ è contenuto nella superchiave $Progetto,Sede$

Quindi la relazione è in 3NF.

## Pro e Contro
Di contro ha che
- è **meno restrittiva** della [[forma normale di Boyce Codd]]
	- tollera alcune ridondanze e anomalie sui dati
	- certifica meno la qualità dello schema ottenuto

Di pro ha che 
- è **sempre ottenibile** qualsiasi sia lo schema di partenza

## Definizione
Data una relazione $R\langle T, F \rangle$, è in 3NF
- se per ogni dipendenza funzionale $X\to A \in F^{+}$ non banale
	- $X$ **è [[chiave#Superchiave|superchiave]]**
	- oppure $A$ è [[Attributo primo|attributo primo]]

È importante osservare che:
- se tutte le parti destre delle dipendenze funzionali sono attributi primi allora siamo a cavallo per la 3NF

## Algoritmo di sintesi
1. Trova una [[7. La Normalizzazione#Copertura canonica|copertura canonica]] $G$ di $F$ e poni $\rho=\{\}$
2. Sostituisci in $G$ ogni insieme $X\to A_{1},\ldots,X\to A_{h}$ di dipendenze con la stessa parte sinistra (nota come *determinante*), con la dipendenza $$X\to A_{1}\cdots A_{h}$$
3. Per ogni dipendenza $X\to Y$ in $G$
	- metti uno schema con attributi $XY$ in $\rho$
4. Unisci ogni schema di $\rho$ contenuto in un altro schema di $\rho$
5. Se la decomposizione **non contiene** alcuno schema i cui attributi costituiscano una [[chiave#Superchiave|superchiave]] per $R$, aggiungi ad essa lo schema con attributi $W$, con $W$ una chiave di $R$

### Esempio
Data la relazione:
- R(MGCRDSPA)
- $F=\{ M\to RSDG, MS\to CD, G\to R, D\to S, S\to D, MPD\to AM \}$

1. [[7. La Normalizzazione#Algoritmo per calcolare la copertura canonica|Costruire una copertura canonica]] $G$ di $F$
	1. Per prima cosa trasformare le dipendenze in dipendenze atomiche $$G = \{ M\to R, M\to S, M\to D, M\to G, MS\to C, MS\to D, G\to R, D\to S, S \to D, MPD\to A, MPD \to M \}$$
	2. Eliminare gli attributi estranei
		- $MS\to D$, $S$ è estraneo perché mi basta la $M$ per derivare la $D$
		- $MS\to C$, $S$ è estraneo perché $$\begin{align} 
		M &\to D\\
		D &\to S\\
		M &\to S\\
		M&\to MS\\
		MS&\to C\\
		M&\to C \end{align}$$
		- $MPD \to A$, $D$ è estraneo perché $D$ è derivabile da $M$ stesso
		- $MPD\to M$ è una dipendenza logica banale
		- Otteniamo $$G = \{M \to R, M\to S, M\to D, M\to G, M\to D, M\to C, G \to R, D\to S, S\to D, MP\to A\}$$
	1. Eliminare le dipendenze ridondanti
		- $M\to D$ è ridondante, compare già
		- $M\to R$ è ridondante per $$M\to G \quad G\to R$$
		- $M\to S$ è ridondante per $$M\to D\quad D\to S$$
		- Otteniamo $$G=\{ M \to D, M\to G, M\to C, G \to R, D\to S, S \to D, MP\to A\}$$
2. Accorpiamo le dipendenze atomiche con stessa parte sinistra $$G = \{ M \to CDG, G \to R, D\to S, S\to D, MP\to A \}$$
3. Per ogni dipendenza creiamo uno schema
	- $R1(\underline M C D G)$
	- $R2(\underline GR)$
	- $R3(\underline DS)$
	- $R4(\underline SD)$
	- $R5(\underline{MP}A)$
4. Fondi gli schemi $R_{i},R_{j}\mid R_{i}\subseteq R_{j}$
	- abbiamo solo $R3(\underline DS)$ ed $R4(\underline SD)$ 
	- quindi $R3 = (\underline{DS})$ con $G3 = \{D\to S, S \to D\}$
5. Se non esiste uno schema che contiene la superchiave $K$, in questo caso $MP$, creane uno con attributi $K$
	- non dobbiamo fare nulla perché abbiamo già lo schema $R5(\underline{MP}A)$

In finale la decomposizione è:
- $R1(\underline M C D G)$ con $G1 = \{ M \to D, M\to G, M\to C \}$
- $R2(\underline GR)$ con $G2 = \{ G \to R \}$
- $R3(\underline {DS})$ con $G3 = \{D\to S, S \to D\}$
- $R5(\underline{MP}A)$ con $G5 = \{ MP\to A  \}$