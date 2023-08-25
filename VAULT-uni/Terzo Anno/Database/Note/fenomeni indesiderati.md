Consideriamo la seguente tabella 

![[Pasted image 20230825175304.png]]

qui saltano subito all'occhio:
- **ridondanza**
	- lo stipendio di ciascun impiegato è ripetuto in tutte le ennuple relative a esso.
	- questo perché lo stipendio dipende solo dall'impiegato
	- il costo del bilancio per ogni progetto è ripetuto
- **anomalia di aggiornamento**
	- se lo stipendio di un impiegato varia, occore modificarne il valore in ennuple diverse
- **anomalia di cancellazione**
	- un nuovo impiegato interrompe la partecipazione a tutti i progetti, dobbiamo cancellare tutte le ennuple in cui appare
	- in questo modo l'impiegano scompare dal database (e noi non lo vogliamo)
- **anomalia di inserimento**
	- un nuovo impiegato non può essere inserito finchè non gli viene assegnato un progetto

