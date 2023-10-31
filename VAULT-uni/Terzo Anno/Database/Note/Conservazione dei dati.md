Una decomposizione $\rho = \{ R_{1}(T_{1}),\ldots, R_{k}(T_{k}) \}$ di uno schema $R(T)$ **preserva i dati**
- se per ogni [[istanza valida]] $r\in R$ si ha $$r= (\pi_{T_{1}})\bowtie\ldots\bowtie(\pi_{T_{k}})$$ se faccio tutte le [[Join|join]] devo **ottenere la relazione di partenza**, non devo ottenere [[tuple spurie]]
### Decomposizione pessima con perdita di informazione
Consideriamo il seguente schema:
- StudentiEdEsami(Matricola, Nome, Provincia, AnnoNascita, Materia, Voto)

Può, ma è sconsigliato, essere decomposto in 
- Studenti(Matricola, Nome, Provincia,AnnoNascita)
- Esami(Materia, Voto)

In questo caso **si perde informazione sullo studente**, non c'è modo di risalire all'esame dato dallo studente. Un modo per verificarlo è facendo un join naturale tra i due schemi decomposti, infatti otterremo delle [[tuple spurie]].

![[Pasted image 20230827160126.png]]

![[Pasted image 20230827160136.png]]

![[Pasted image 20230827160147.png]]

Mario non ha preso un esame di Storia e Laura non ha preso un esame di Matematica nella tabella originale.
### Decomposizione corretta
Consideriamo il seguente schema:
- StudentiEdEsami(Matricola, Nome, Provincia, AnnoNascita, Materia, Voto)

Può essere correttamente decomposto in 
- Studenti(Matricola, Nome, Provincia, AnnoNascita)
- Esami(Matricola, Materia, Voto)

Non perdo informazioni perché **matricola è [[chiave]]**. Usare la chiave è l'unico modo per avere decomposizione senza perdita d'informazione.
## Teorema
Dalla definizione di [[Join#Join naturale|join naturale]]
- se $\rho=\{ R_{1}(T_{1}),\ldots, R_{k}(T_{k}) \}$ **è una decomposizione su $R(T)$**, allora per ogni istanza $r\in R$ si ha $$r\subseteq \{ R_{1}(T_{1}),\ldots, R_{k}(T_{k}) \}$$

Uno schema $R(T)$ si **decompone senza perdita** in due schemi $R_{1}(T_{1})$ e $R_{2}(T_{2})$ se 
- per ogni possibile istanza $r\in R(T)$ il **join naturale delle proiezioni di** $r$ su $T_{1}$ e $T_{2}$ **produce la tabella di partenza** (cioé senza [[tuple spurie]]).

### Condizione sufficiente ma non necessaria
La decomposizione senza perdita **è garantita**
- se **l'insieme di attributi comuni** alle due relazioni, $T_{1}\cap T_{2}$ , è [[chiave]] per almeno una delle **due relazioni decomposte**.

### Dimostrazione
Supponiamo $r$ essere una relazione sugli attributi $ABC$, consideriamo le sue proiezioni:
- $r_{1}$ su $AB$ 
- $r_{2}$ su $AC$

Supponiamo $r$ soddisfi la dipendenza funzionale $A\to C$, allora $A$ **è chiave** per $r_{1}$ su $AC$
- quindi non ci sono, in tale proiezione ($r_{1}$) , due tuple diverse sugli stessi valori di $A$

Il join costruisce tuple **a partire dalle tuple delle due proiezioni**
- sia $t=(a,b,c)$ una tupla del join $r_{1}\bowtie r_{2}$
- per definizione di proiezione:
	- esistono due tuple in $r$, $t_{1}'=(a,b,*)$ e $t_{2}'=(a,*,c)$ dove "$*$" sta per *un valore non noto*
- poiché $A\to C$, allora esiste un solo valore in $C$ associato al valore $a$
	- dato che $(a,c)$ compare nella proiezione, questo valore è **proprio $c$**
- allora nella tupla $t_{1}'$ il valore incognito deve essere proprio $c$
- quindi  $(a,b,c)\in r$ 

### Perché "non necessaria"?
Perché dipende dalla relazione, ad esempio
![[Pasted image 20230827161722.png]]

con dipendenze funzionali $Impiegato\to Progetto$ e $Impiegato\to Sede$, può essere decomposto in
- T1(Impiegato, Sede)
- T2(Progetto, Sede)

senza dare luogo a nessuna anomalia.

## Decomposizione binaria
Sia $R\langle T,F \rangle$ uno schema di relazione, la decomposizione $$\rho=\{R_{1}(T_{1}), R_{2}(T_{2})\}$$ **preserva i dati** se $T_{1}\cap T_{2}\to T_{1}\in F^{+}$ oppure $T_{1}\cap T_{2}\to T_{2}\in F^{+}$.

Infatti nell'esempio sopra dalle decomposizioni
- $T_{1}= (Imp,Sede)$
- $T_{2}= (Prog, Sede)$

possiamo dire che preservano i dati perché: $$(Imp,Sede)\cap(Prog, Sede)\to (Imp,Sede) = (Imp,Prog) \to (Imp,Sede) \in F^{+}$$ basti osservare che dalle regole di inferenza otteniamo
1. $Imp \to Prog$
2. $Imp \to Sede$
3. $Imp,Prog \to Imp$
4. $Imp, Prog \to Imp, Sede$
