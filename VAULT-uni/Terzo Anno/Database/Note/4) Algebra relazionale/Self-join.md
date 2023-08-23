Supponiamo di considerare la seguente relazione

![[Pasted image 20230821110715.png]]

e di volere ottenere una relazione Nonno-Nipote.

> È ovvio che in questo caso abbiamo bisogno di utilizzare due volte la stessa tabella

Tuttavia $Genitori \bowtie Genitori = Genitori$ perché tutti gli **attributi coincidono**.
Occorre effettuare una [[Ridenominazione|ridenominazione]] $Nonno,Genitore \leftarrow Genitore, Figlio$
$$\rho_{Nonno,Genitore \leftarrow Genitore, Figlio}(Genitori)$$

![[Pasted image 20230821111031.png]]

A questo punto effettuiamo un [[Join#Join naturale|join naturale]] con la tabella $Genitori$
$$\rho_{Nonno,Genitore \leftarrow Genitore, Figlio}(Genitori) \bowtie \rho_{Nipote\leftarrow Figlio}(Genitori)$$ 

![[Pasted image 20230821111321.png]]