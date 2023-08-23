La proiezione $\pi_\text{ListaAttributi}(\text{Operando})$
- è un **operatore monadico**
- produce un risultato che
	- ha **parte degli attributi dell'operando**
	- e **contiene ennuple** dell'operando **ristrette agli attributi della lista**
- si indica con $\pi_{A_{1},\ldots,A_{n}}(R)$

## Esempio
Voglio `Matricola` e `Cognome` di tutti gli impiegati

![[Pasted image 20230820110124.png]]

## Cardinalità delle proiezioni
Il risultato di una proiezione
- contiene **al più** tante ennuple quante quelle dell'operando (ma può contenerne meno)
- se $X$ è [[chiave#Superchiave|superchiave]] di $R$, allora $$\pi_{X}(R)$$ contiene esattamente tante ennuple quante in $R$
- se $X$ NON è superchiave, potrebbero esistere **valori ripetuti** su quegli attributi
	- questi vengono rappresentati solo una volta (la prima occorrenza e basta)

![[Pasted image 20230820110004.png]]

È importante osservare gli effetti di [[Proiezioni e join|join e proiezioni insieme]].

## Non distributività rispetto alla differenza
In generale vale $$\pi_{A}(R_{1}\setminus R_{2}) \ne \pi_{A}(R_{1}) \setminus \pi_{A}(R_{2})$$
Se $R_{1}$ e $R_{2}$ sono definite su $AB$, e contengono tuple uguali su $A$ e diverse da su $B$

Verifichiamo quanto detto, 

![[Pasted image 20230821114839.png]]

Dipede a che colonna si riferisce $A$

> Imp1

| Impiegato | Capo  |
| --------- | ----- |
| Neri      | Mori  |
| Bianchi   | Bruni |
| Verdi     | Bini  |

> Imp2

| Impiegato | Capo     |
| --------- | -------- |
| Neri      | Rossi    |
| Bianchi   | Bordeaux |
| Verdi     | Blu      |

$$Imp1 \setminus Imp2 = Imp1$$ 

### Proiezione su Impiegato

- La proiezione su $Impiegato$ della relazione $Imp1 \setminus Imp2$ sarà proprio uguale alla colonna $Impiegato$ di $Imp1$
- Per il secondo membro invece:
	- $\pi_{Impiegato}(Imp1)$  è uguale a $\pi_{Impiegato}(Imp2)$ 
	- quindi la differenza restituirà l'**insieme vuoto**

$$Imp1.Impiegato \ne \emptyset$$

### Proiezione su Capo

- La proiezione su $Capo$ della relazione $Imp1\setminus Imp2$ sarà proprio uguale alla colonna $Capo$ di $Imp1$
- Per il secondo membro invece:
	- $\pi_{Capo}(Imp1)$ non ha ennuple in comune con $\pi_{Capo}(Imp2)$, sono disgiunti
	- quindi la differenza restituirà la colonna $Capo$ di $Imp1$

$$Imp1.Capo \equiv Imp1.Capo$$