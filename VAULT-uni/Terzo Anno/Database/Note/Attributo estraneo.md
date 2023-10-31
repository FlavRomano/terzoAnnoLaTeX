Sia $F$ un insieme di [[dipendenza funzionale|dipendenze funzionali]]
Data una dipendenza $X\to Y\in F$, si dice che $X$ **contiene un attributo estraneo** $A_{i}$ 
- se posso **toglierlo dalla parte sinistra lasciando invariata la parte destra**, cioè $$(X\setminus\{ A_{i} \}) \to Y \in F^{+}$$ quindi in pratica sto dicendo che da $F$ continuo a derivare la dipendenza funzionale nonostante abbia rimosso l'attributo dalla parte sinistra $$F\vdash (X\setminus \{A_{i}\})\to Y$$

Per verificare se in una dipendenza $AX\to B$ l'attributo $A$ è estraneo:
- Calcoliamo $X^{+}$ e verifichiamo che includa $B$
	- con questo verifichiamo che basti $X$ per determinare $B$

## Esempio
Dato lo schema 
- Orari(CodAula, NomeAula, Piano, Posti, Materia, CDL, Docente, Giorno, Ora)

Abbiamo un insieme di dipendenze funzionali $$F = \{ Docente,Giorno,Ora\to CodAula\;; Docente,Giorno\to Ora\}$$

Prendiamo come $X$ la dipendenza $Docente,Giorno$ e verifichiamo se $Ora$ è un attributo esterno: 
$$\{Docente, Giorno\}^{+}=Docente,Giorno,Ora,CodAula$$ quindi abbiamo ottenuto che $Docente,Giorno$ è chiave e $Ora$ nella prima dipendenza logica è un **attributo estraneo**. Quindi l'insieme di dipendenze logiche può essere riscritto come $$F' = \{ Docente,Giorno\to CodAula\;;Docente,Giorno\to Ora \}$$