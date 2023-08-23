Quando abbiamo discusso dell'utilità del [[Prodotto cartesiano#Ha senso il prodotto cartesiano|prodotto cartesiano]], abbiamo concluso dicendo che ha senso se seguito da una [[Selezione|selezione]].

Cioè quando $$\sigma_{condizione}(R_{1}\bowtie R_{2})$$ questa operazione può essere sintatticamente sostituita da una **theta-join**, indicata con $$R_{1}\bowtie_{Condizione}R_{2}$$

- La condizione è spesso una congiunzione di condizioni atomiche $A_{1}\;\theta \;A_{2}$ con $\theta = \{=,<,>,\leq,\geq,\neq\}$
	- quando l'operatore è sempre l'uguaglianza ($=$) allora si parla di **equi-join**.

![[Pasted image 20230821105519.png]]

## Esempio
> Impiegati

| Impiegato | Reparto |
| --------- | ------- |
| Alice     | A       |
| Bob       | B       |
| Carol     | B       |

> Reparti

| Reparto | Capo  |
| ------- | ----- |
| A       | Rossi |
| B       | Neri  |


> $Impiegati \bowtie Reparti$

| Impiegato | Reparto | Capo  |
| --------- | ------- | ----- |
| Alice     | A       | Rossi |
| Bob       | B       | Neri  |
| Carol     | B       | Neri  | 

