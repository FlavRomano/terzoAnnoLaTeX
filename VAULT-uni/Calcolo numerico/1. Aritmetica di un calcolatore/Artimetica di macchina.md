Rappresentare un numero reale non nullo $x\in\mathbb R$, con $x\neq0$, in macchina significa approssimare $x$ con un numero $$\tilde x \in F(B,t,m,M)$$ commettendo un **errore relativo** di rappresentazione.

```ad-def
title: Errore relativo
Errore che non dipende dalla grandezza del numero, ma dall'aritmetica della macchina.
$$ \varepsilon_x = \dfrac{\tilde x - x}{x} = \frac{\eta_x}{x},\quad x\neq0 $$
con $\eta_x = \tilde x - x$ noto come **errore assoluto** di rappresentazione.
```

Dato $x\in\mathbb R$ con $x\neq0$ distinguiamo due casi:
1. $\lvert x \rvert < \omega$ (underflow) oppure $\lvert x \rvert > \Omega$ (overflow)
2. $\omega\leq\lvert x \rvert\leq\Omega$ abbiamo delle tecniche di approssimazione:
	- Arrotondamento, dove $x$ viene approssimato con il numero rappresentabile più vicino $\tilde x$
	- Troncamento, dove $x$ viene approssimato con il più grande numero rappresentabile $\tilde x$ tale per cui $$ \lvert \tilde x \rvert \leq \lvert x \rvert $$ convenzionalmente si scrive $$\text{trn}(x) = \tilde x$$