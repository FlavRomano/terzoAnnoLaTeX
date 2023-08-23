> Sostituzione della generalizzazione con relazioni

La gerarchia (generalizzazione)  si trasforma
- in due associazioni uno a uno $(1:1)$ che legano rispettivamente
	- la classe genitore con le classi figlie
- **non c'è un trasferimento di attributi o associazioni**
- le classi figlie $A_{1}$ e $A_{2}$ sono identificate esternamente dalla classe genitore $A_{0}$

Nello schema ottenuto vanno aggiunti dei vincoli
- ogni occorrenza di $A_{0}$ non può partecipare contemporaneamente alle due [[associazione|associazioni]]
- e se la gerarchia è **[[generalizzazione totale|totale]]**, deve partecipare ad almeno una delle due 

![[Pasted image 20230819174038.png]]

```ad-warning
- Utilizzarla se **ci sono chiavi esterne VERSO il padre**
- Ha alcuni problemi
	- per ottenere informazioni devo accedere a più tabelle sequenzialmente, invece se avessi accorpato tutto al padre sarebbe stato accesso diretto.
	- non efficiente durante le interrogazioni della base di dati.
```

## Esempio
La classe `Corsi` ha attributi
- `codice` ([[chiave]])
- `nome`

e ha due [[sottoclasse|sottoclassi]] di tipo partizione [[1. Costruzione di una base di dati#Ereditarietà|partizione]].
- `CorsiInterni`
	- con attributo `crediti`
- `CorsiEsterni`
	- con attributi `corsoDiLaurea` e `annoAccademico`

![[Pasted image 20230819174156.png]]