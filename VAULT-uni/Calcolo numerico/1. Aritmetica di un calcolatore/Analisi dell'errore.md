L'aritmetica di macchina genera due tipi di errore:
1. Per $x\in\mathbb R$, la rappresentazione approssimata in macchina $\tilde x = F(B,t,m,M)$ è $$ \tilde x = x \cdot (1+ \varepsilon_x)\quad \lvert \varepsilon_x \rvert \leq u $$ dove $u$ è detta **precisione di macchina**
2. Per $\tilde x,\tilde y\in F(B,t,m,M)$ le operazioni $$\tilde x + \tilde y\quad \tilde x - \tilde y \quad \tilde x * \tilde y \quad \tilde x / \tilde y$$ potrebbero non essere definite in $F$

Per la motivazione del punto (2) definiamo le operazioni di macchina: $$ \tilde x \oplus \tilde y = f\left(\tilde x + \tilde y \right) \quad f(x) = \text{trn}(x)$$

potremmo scrivere $$\tilde x \oplus \tilde y = \text{trn}(\tilde x+ \tilde y) = (\tilde x + \tilde y)\cdot (1+\varepsilon)\quad\lvert \varepsilon \rvert \leq u$$ si viene a creare un **errore locale**.

e.g: $f(x) = x^2 - 1$

È una differenza di quadrati, quindi possiamo riscriverlo nella forma $f(x) = (x-1)(x+1)$. 
Quale forma, o algoritmo, è più conveniente dal punto di vista dell'aritmetica di macchina?

- $f(x) = x^2 - 1\quad$ abbiamo $$\begin{aligned} x&\to x^2\\ x^2&\to x^2 - 1 \end{aligned}$$ 
	1. $x \mapsto \tilde x = x(1+\varepsilon_x) \quad \lvert \varepsilon_x\rvert\leq u$
	2. $\tilde x \otimes \tilde x = \tilde x ^2 \cdot (1 + \varepsilon_1) \quad \lvert \varepsilon_1\rvert\leq u$
	3. $(\tilde x \otimes \tilde x) \ominus1 = \left( \tilde x^2 (1+\varepsilon_1) - 1 \right)(1 + \varepsilon_2) \quad \lvert \varepsilon_1\rvert\leq u$

Quindi questo algoritmo è $$ g_1(\tilde x) = (\tilde x \otimes \tilde x) \ominus 1 $$ che tipo di errore facciamo?
$$ \begin{aligned}
g_1(\tilde x) &= (\tilde x \otimes \tilde x) \ominus 1 \\
&= \left( \tilde x ^2 (1+\varepsilon_1) - 1 \right)(1+\varepsilon_2) \\
[\tilde x = x(1+\varepsilon_x)] &= \left( x^2 \cdot (1+\varepsilon_x)^2(1+\varepsilon_1)-1 \right)(1+\varepsilon_2) 
\end{aligned} $$

Per semplificare l'espressione facciamo **analisi di 1° ordine dell'errore**, cioè non consideriamo errori di grado più alto del primo.

$$\begin{aligned}
&\doteq \left( x^2 \cdot (1+\varepsilon_x)^2(1+\varepsilon_1)-1 \right)(1+\varepsilon_2) \\
&\doteq \left( x^2 \cdot (1 + \cancel{\varepsilon_x^2} + 2 \varepsilon_x)(1+\varepsilon_1) - 1 \right) (1+\varepsilon_2)\\
&\doteq \left( x^2 \cdot (1 +  2\varepsilon_x + \varepsilon_1 + \varepsilon_2) \right) - 1(1+\varepsilon_2) \\
&\doteq \underbrace{(x^2 - 1)}_{f(x)} + 2x^2\varepsilon_x + x^2 \varepsilon_1 + \varepsilon_2 (x^2 - 1) \\
g_1(\tilde x) &= f(x) + 2x^2\varepsilon_x + x^2\varepsilon_1 + \varepsilon_2(x^2 - 1) \\
g_1(\tilde x) - f(x) &= 2x^2\varepsilon_x + x^2\varepsilon_1 + \varepsilon_2(x^2 - 1)\\
\frac{g_1(\tilde x)- f(x)}{f(x)} &= \frac{2x^2}{f(x)}\cdot \varepsilon_x + \frac{x^2}{f(x)}\cdot \varepsilon_1 + \frac{\varepsilon_2}{f(x)}\cdot(x^2-1)\\
\frac{g_1(\tilde x)- f(x)}{f(x)} &= \frac{2x^2}{(x^2-1)}\cdot \varepsilon_x + \frac{x^2}{(x^2-1)}\cdot \varepsilon_1 + \frac{\varepsilon_2}{\cancel{(x^2-1)}}\cdot\cancel{(x^2-1)}
\end{aligned}$$
Abbiamo ottenuto così l'errore totale
$$ \varepsilon_1 = \frac{g_1(\tilde x)- f(x)}{f(x)} = \underbrace{\frac{2x^2}{x^2 - 1}\cdot\varepsilon_x}_{\text{input error}} + \underbrace{\frac{x^2}{x^2-1} \cdot\varepsilon_1 + \varepsilon_2}_{\text{local error}} $$
Quello che abbiamo chiamato *input error* è la propagazione dell'errore iniziale, invece *local error* è la propagazione dovuta agli errori locali.

Osserviamo invece il secondo algoritmo
$$f(x) = x^2 -1 = (x-1)(x+1) \to g_2(\tilde x) = (\tilde x \ominus 1) \otimes (\tilde x \oplus 1)$$
$$\begin{aligned}
g_2(\tilde x) &= \left( x(1+\varepsilon_x) - 1 \right)(1 + \varepsilon_1)\cdot \left( x(1+\varepsilon_x) - 1 \right)(1+\varepsilon_2)(1+\varepsilon_3)\\
&= \left( x^2 (1+\varepsilon_x)^2 - 1 \right) ( 1+\varepsilon_1)(1+\varepsilon_2)(1+\varepsilon_3)\\
&\doteq (x^2(1 + \cancel{\varepsilon_x^2} + 2\varepsilon_x) - 1)( 1+\varepsilon_1)(1+\varepsilon_2)(1+\varepsilon_3)\\
&\doteq x^2 ( 1+ 2\varepsilon_x + \varepsilon_1 + \varepsilon_2 + \varepsilon_3) - 1 (1 + \varepsilon_1 + \varepsilon_2 + \varepsilon_3) \\
&= x^2 + 2\varepsilon_x x^2 + \varepsilon_1 x^2 + \varepsilon_2 x ^2 + \varepsilon_3 x^2 - 1 - \varepsilon_1  - \varepsilon_2 - \varepsilon_3
\end{aligned}$$