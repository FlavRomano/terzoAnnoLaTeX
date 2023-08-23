La selezione $\sigma_\text{cond}(Operando)$
- è un'**operatore monadico**
- produce un risultato che
	- ha **lo stesso schema dell'operando**
	- **contiene un sottoinsieme delle ennuple dell'operando**
		- quelle che soddisfano una condizione espressa combinando condizioni atomiche $$A\texttt\;\theta\;b$$ dove $A$ è un attributo, $c$ una costante e $\theta$ un operatore di confronto.

Per riferirsi ai valori nulli si usano le condizioni $\texttt{IS NULL}$ e $\texttt{IS NOT NULL}$.

## Importante
1. Le **selezioni** vengono **valutate in sequenza** (non in parallelo) e **separatamente** quindi $$\sigma_{\text{età}>30}(\text{Persone})\cup\sigma_{\text{età}\le30}(\text{Persone}) \ne \text{Persone}$$
2. Le **condizioni atomiche** all'interno di una selezione vanno **valutate in sequenza** quindi $$\sigma_{\text{età}>30\lor\text{età}\le30}(\text{Persone})\ne\text{Persone}$$

In entrambi i casi non possiamo escludere la creazione di [[Valore nullo|valori nulli]], perciò l'esempio corretto sarebbe
$$
\begin{aligned}
\sigma_{età>30}(\text{Persone})\cup\sigma_{età\le30}(\text{Persone})\cup\sigma_{Età\texttt{ IS NULL}}(\text{Persone}) &=\\
\sigma_{età>30 \; \lor \;età\le30 \; \lor\; età \texttt{ IS NULL}}(\text{Persone}) &= \text{Persone}
\end{aligned} 
$$

## Esempio
`Impiegati` che guadagnano più di 50 

![[Pasted image 20230820112659.png]]

`Impiegati` che guadagnano più di 50 e lavorano a Milano

![[Pasted image 20230820112733.png]]

## Esempio con valori nulli
![[Pasted image 20230820113013.png]]

La $n$-upla viene buttata perché
- la condizione atomica **è vera solo per valori non nulli**

Con l'ausilio di $\texttt{IS NULL}$ risolviamo la cosa

![[Pasted image 20230820115003.png]]