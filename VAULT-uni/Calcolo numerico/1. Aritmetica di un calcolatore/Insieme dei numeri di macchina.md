Date $t$ cifre, una base $B$ e range $(-m,M)$, chiamiamo **insieme dei numeri di macchina** l'insieme dei numeri reali 
$$ F(B,t,m,M) = \{0\}\cup\left\{ x\in\mathbb R \mid x = sign(x)\cdot B^p \cdot \sum_{i=1}^t d_i\cdot B^{-i},\;0\leq d_i\leq B-1,\;d_1\neq0,-m\leq p\leq M \right\}$$

oss. Unisco lo 0 perché non ha rappresentazione normalizzata.

- L'insieme dei numeri di macchina ha cardinalità $$\# F(B,t,m,M) = N = 2\cdot B^{t-1}\cdot (B-1) \cdot (M+m+1) + 1$$
- Sia $x \in F(B,t,m,M)$ e $x\neq0$, allora 
	- vale che $\omega \leq \lvert x \rvert \leq \Omega$ valore normalizzato
	- $\omega = \text{più piccolo valore positivo di macchina} = B^{-m-1}$
	- $\Omega = \text{più grande valore positivo di macchina} = B^M\cdot(1 - B^{-t})$
- Se $p = M$ si introducono rappresentazioni speciali per: $\pm \infty$ e $NaN$
- $F(B,t,m,M)$ è simmetrico rispetto all'origine.
- Posto $x = (-1)^s\cdot B^p\cdot\alpha\in F(B,t,m,M)$ allora il successivo numero di macchina sarà $$ y = (-1)^s \cdot B^p \cdot (\alpha + B^{-t})$$ 
	- per cui, in un sistema a virgola mobile, la distanza tra due valori è $$\lvert y - x\rvert = B^{p-t}$$ se aumenta $p$, ottengo "spazi" più grandi.
