Sia $F$ un insieme di dipendenze funzionali, diremo che $$F \vdash X\to Y$$ cioè che da $F$ **possiamo derivare** $X\to Y$
- se $X\to Y$ può essere inferito a partire $F$ usando gli [[assiomi di Armstrong]].

## Definizione
Una **derivazione** di $f$ da $F$ è una sequenza finita $f_{1},\ldots,f_{m}$ di dipendenze
- dove $f_{m}=f$ 
- e ogni $f_{i}$ è un **elemento** di $F$ oppure è ottenuta dalle precedenti dipendenze $$f_{1},\ldots,f_{i-1}$$ della derivazione usando una regola di inferenza.

## Regole 
### Unione U
Sia $F = \{ X\to Y, X\to Z \}$ allora diremo che $$\{ X\to Y, X\to Z \}\vdash X\to YZ$$ $$F\vdash X\to YZ$$ cioé $X\to YZ$ è **derivabile** ($\vdash$) da $F$.

La dimostrazione non è nulla di che, usiamo gli [[assiomi di Armstrong]]
$$\begin{align*}
X \to Y &\quad\text{per ipotesi} \tag 1\\
X \to XY &\quad\text{per arricchimento di 1} \tag 2\\
X \to Z &\quad\text{per ipotesi} \tag 3\\
XY \to YZ &\quad\text{per arricchimento di 3} \tag 4\\
X \to YZ &\quad\text{per transitività di 2 e 4} \tag 5
\end{align*}$$
### Decomposizione D
Sia $F =  \{ X\to Y\}$ e sia $Z \subseteq Y$ allora diremo che $$\{ X\to Y\}\vdash X\to Z$$ $$F\vdash X\to Z$$  cioé $X\to Z$ è **derivabile** ($\vdash$) da $F$.

### Corollario
Da unione e decomposizione si ricava che
- se $Y = A_{1},A_{2},\ldots,A_{n}$ allora $$X\to Y \iff \{ X\to A_{1},X\to A_{2},\ldots, X\to A_{n} \}$$

## Esempi
### Esempio 1
Abbiamo una relazione $R(A,B,C,D)$ e un insieme di dipendenze funzionale $F=\{A\to B, BC\to D\}$
- $AC$ è [[chiave#Superchiave|superchiave?]]?

$AC$ superchiave vuol dire che $AC$ determina funzionalmente $ABCD$, cioé $AC \to ABCD$

![[Pasted image 20230826110501.png]]