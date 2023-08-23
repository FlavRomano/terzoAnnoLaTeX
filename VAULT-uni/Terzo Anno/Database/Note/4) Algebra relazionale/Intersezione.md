L'intersezione è un **operatore derivato**
- lo deriviamo usando gli operatori
	- [[Prodotto cartesiano]]
	- [[Ridenominazione]]
	- [[Selezione]]
	- [[Proiezione]]

![[Pasted image 20230820123437.png]]

n.b: la ridenominazione $\rho_{S}(S)$ è importante perché 
- rinomina gli attributi di $S$ **anteponendo** il prefisso $S$ e.g $A \to S.A$

$$R(A,B)\cap S(A,B) = \{ x \mid x \in R.\; \exists\; y\in S,\; x = y \}$$

1. Effettuiamo una selezione con condizione $A=S.A \;\land \;B = S.B$ sul prodotto cartesiano tra $R$ e $S$ ridenominata. Così da tenere le ennuple i cui valori sono uguali su entrambe le relazioni. $$\sigma_{A=S.A \;\land \;B = S.B}(R\times \rho_{S}(S))$$
![[Pasted image 20230820123625.png]]
1. Infine una proiezione così da poter selezionare solo le colonne del primo operando $$\pi_{A,B}(\sigma_{A=S.A \;\land \;B = S.B}(R\times \rho_{S}(S)))$$
![[Pasted image 20230820124118.png]]