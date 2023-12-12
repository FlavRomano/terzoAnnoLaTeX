Nonostante riesca a decomporre correttamente [[Conservazione dei dati|senza perdite]] uno schema, ancora niente mi garantisce che non vengano violate delle dipendenze 

### Esempio
Abbiamo una decomposizione sì fatta:
- T1(Impiegato, Sede)
- T2(Impiegato, Progetto)

con dipendenze funzionali $Impiegato \to Sede$ e $Progetto \to Sede$.

> Che cosa accade se aggiungo l'impiegato "Neri", che lavora a Milano, al progetto "Marte"?
> - oss: stiamo aggiungendo (impiegato, progetto), le dipendenze funzionali che dicono?

![[Pasted image 20230827164702.png]]



Viene violata la dipendenza funzionale funzionale $Progetto\to Sede$, perché 
- Neri è impiegato a Milano
- Marte è un progetto che sta a Roma

Facendo il join naturale, si viene a creare una [[tuple spurie|tupla spuria]] $Neri, Marte, Milano$

![[Pasted image 20230827165133.png]]

oss: tupla spuria perché non ha senso che a un progetto che sta a Roma ci lavori uno con sede a Milano
> No smart-working 😭

## Proiezione delle dipendenze
Dato uno schema $R\langle T, F\rangle$ e un insieme di attributi $T_{1}\subseteq T$, la **proiezione di $F$ su $T_{1}$** è 
- l'insieme di tutte le dipendenze funzionali $X\to Y$ nella chiusura $F^{+}$ tali che $XY$ sia in $T_{1}$ $$\pi_{T_{1}}(F) = \{ X \to Y \in F^{+} \mid XY \subseteq T_{1} \}$$

### Esempio
Sia $R(A, B, C)$ e $F=\{A\to B\;; B\to C\;; C\to A\}$ 
1. Calcoliamo la proiezione di $F$ su $AB$:
$$\pi_{AB} (F) = \{ A\to B\;; B \to A \}$$ oss: $B\to A$ perché: $$\begin{align} A&\to B\\ B&\to C \\ C &\to A \\ B&\to A \ni F^{+}\end{align}$$
2. Calcoliamo la proiezione di $F$ su $AC$:
$$\pi_{AC}(F) = \{ A\to C, C\to A \}$$
oss: $A\to C$ perché: $$\begin{align}C&\to A\\ A&\to B\\ B&\to C\\A&\to C\ni F^{+}\end{align}$$

## Conservazione delle dipendenze
Dato lo schema $R \langle T, F \rangle$ la decomposizione $\rho = \{ R_{1},\ldots, R_{n} \}$ **preserva le dipendenze** 
- se **l'unione delle dipendenze** in $\pi_{T_{i}}(F)$ è una [[Copertura|copertura]] di $F$
	- cioé che le dipendenze nell'unione mi permettano di derivare le stesse dipendenze di $F$.

Il problema di stabilire se una decomposizione $\rho$ preserva le dipendenze ha complessità polinomiale.

## Teorema ponte tra conservazione delle dipendenze e dati
Sia $\rho = \{ R_{i}\langle T_{i}, F_{i}\rangle \}$ una decomposizione di $R\langle T, F \rangle$ che **preserva le dipendenze**, 

**preserva anche i dati** 
- se esiste una $T_{j}$ [[chiave#Superchiave|superchiave]] per $R\langle T,F\rangle$ 

### Esempio
Data la relazione
- Telefoni(Prefisso, Numero, Località, Abbonato, Via)

$$F = \{ Pref, Num \to Loc, Abb, Via\;; Loc\to Pref \}$$

Si consideri la decomposizione:
- Tel(Numero, Località, Abbonato, Via) con $F_{1}=\{ Loc,Num\to Abb,Via \}$
- Pref(Località, Prefisso) con $F_{2}= \{ Loc \to Pref \}$

Preserva dati **ma non le dipendenze**
- $F_{1}\cup F_{2}$ non è una copertura di $F$, basti pensare che $F\vDash PN\to L$ non è deducibile da entrambe.