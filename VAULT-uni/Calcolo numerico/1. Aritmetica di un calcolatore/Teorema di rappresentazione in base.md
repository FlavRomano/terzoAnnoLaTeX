Indichiamo con $B$ la base, con $B\in\mathbb N$ e $B\geq 2$.

Sia $x \in \mathbb R$ non zero $(x\neq0)$, allora esistono e sono unicamente determinati:
1. **Cifre della rappresentazione** $\left\{ d_i \right\}_{i>1}$ con $d_1\neq0$, $0\leq d_i \leq B-1$ e $d_i$ non definitivamente uguali a $B-1$.
2. **Esponente della rappresentazione** $p\in\mathbb Z$ .

tali per cui si ha $$ x = \text{sign}(x) \cdot B^p\cdot \underbrace{\sum_{i=1}^{\infty} d_i\cdot B^{-i}}_{\text{mantissa}} $$

questa è la **rappresentazione normalizzata in virgola mobile** di $x$.

e.g: $B = 10$, $\text{numero} = 0.01$
$$ \begin{aligned} 
x = + 10^p\cdot (&d_1 \cdot 10^{-1} + d_2 \cdot 10^{-2} + \ldots) \\
+&d_1 \cdot 10^{p-1} + d_2 \cdot 10^{p-2}\\
& \text{il primo numero deve essere non zero, quindi vado a}\\ 
& \text{vedere la prima cifra non zero, cioè quella in posizione }10^{-2}\\
\end{aligned} $$

$$\implies p-1 = -2 \iff p = -1 \quad\therefore x = +10^{-1}\cdot(d_1 \cdot 10^{-2})$$

Dal teorema di rappresentazione in base, segue che la rappresentazione di un numero reale nel calcolatore può avvenire assegnando delle posizioni di memoria per: segno, esponente e cifre della rappresentazione.

e.g: con $p = 8$ e $23$ cifre per la rappresentazione
$$ x = \pm 2^p sum_{i=1}^{23}d_i\cdot 2^{-i} \quad d_i\neq0,p\in[-2^{p-1}-1,+2^{p-1}-1] = [-127, 127]$$