> Accorpamento delle figlie della generalizzazione nel genitore

Se $A_{0}$ è la classe genitore di $A_{1}$ e $A_{2}$ allora $A_{1}$ e $A_{2}$ vengono eliminate e accorpate ad $A_{0}$
- ad $A_{0}$ viene aggiunto un attributo **discriminatore**
	- che indica da quale delle classi figlie deriva una certa istanza
- gli attributi di $A_{1}$ e $A_{2}$ **vengono assorbiti** da $A_{0}$
	- assumendo [[Valore nullo|valore nullo]] sulle istanze provenienti dall'altra classe.

![[Pasted image 20230818150333.png]]

## Esempio
La classe `Corsi` ha attributi
- `codice` ([[chiave]])
- `nome`

e ha due [[sottoclasse|sottoclassi]] di tipo partizione [[1. Costruzione di una base di dati#Ereditarietà|partizione]].
- `CorsiInterni`
	- con attributo `crediti`
- `CorsiEsterni`
	- con attributi `corsoDiLaurea` e `annoAccademico`

![[Pasted image 20230818151020.png]]

Abbiamo accorpato le due classi figlie al genitore
- aggiungendo i loro attributi
	- `crediti` dovuto a `CorsiInterni`
	- `corsoDiLaurea` e `AnnoAccademico` dovuto a `CorsiEsterni`
- e aggiungendo l'attributo **discriminatore** `internoOEsterno`
	- con valori `Interno` o `Esterno`

```ad-warning
Traduzione utilse solo se le operazioni che faccio **non coinvolgono il discriminatore**
```