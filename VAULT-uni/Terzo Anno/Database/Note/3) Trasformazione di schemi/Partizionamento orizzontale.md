> Accorpamento del genitore della generalizzazione nelle figlie.

La classe genitore $A_{0}$ viene eliminata
- le classi figlie $A_{1}$ e $A_{2}$ ereditano le proprietà della classe genitore
	- attributi, identificatore, relazioni
- le relazioni della classe genitore vengono sdoppiate, coinvolgendo ciascuna delle figlie.
```ad-warning
- divide gli elementi della superclasse in più relazioni diverse, per cui **non è possibile mantenere un [[vincolo di integrità referenziale|vincolo referenziale]] verso la superclasse stessa**
- questo tipo di sostituzione non si usa se nello schema relazionale grafico **c'è una freccia che entra nella superclasse**
	- e.g come la $\leftarrow^S$ che entra nella superclasse $A$
```
![[Pasted image 20230818154632.png]]

```ad-warning
- Che succede se esiste una chiave esterna verso la tabella $A$?
	- **non posso accorpare** il padre ai figli se ho una chiave esterna **verso** il padre
- È utile se le operazioni riguardano solo le sottoclassi $B$ e $C$

```

## Esempio
La classe `Corsi` ha attributi
- `codice` ([[chiave]])
- `nome`

e ha due [[sottoclasse|sottoclassi]] di tipo [[1. Costruzione di una base di dati#Ereditarietà|partizione]].
- `CorsiInterni`
	- con attributo `crediti`
- `CorsiEsterni`
	- con attributi `corsoDiLaurea` e `annoAccademico`

![[Pasted image 20230818154750.png]]

Le tabelle che ho creato hanno la **stessa** [[chiave#Chiave Primaria|chiave primaria]] del padre.

```ad-faq
- Che succede se la gerarchia non è una copertura?
	- **non posso usare questo partizionamento**, se non ho copertura della gerarchia perdo dei corsi.
- Che succede se la gerarchia non è disgiunzione?
	- **ho duplicazione dei dati**
```