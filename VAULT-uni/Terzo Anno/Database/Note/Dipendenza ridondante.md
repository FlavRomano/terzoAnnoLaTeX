Dato un insieme di [[dipendenza funzionale|dipendenze funzionali]] $F$, $X\to Y\in F$ è una **dipendenza ridondante**
- se **eliminandola da** $F$ riesco comunque a [[Derivazione|derivarla]], cioè $$F\setminus \{ X\to Y \} \vdash X \to Y$$

## Stabilire se una dipendenza è ridondante
Per stabilire se una dipendenza funzionale $X\to A$ è ridondante:
1. la eliminiamo da $F$ $$F = F \setminus \{X \to A\}$$
2. calcoliamo la [[problema dell'implicazione#Algoritmo per calcolare la chiusura di $X$ rispetto a $F$|chiusura]] $X^{+}_F$
	- verifichiamo se $X^{+}_F$ include $A$, ovvero se con le dipendenze funzionali che restano riusciamo ancora a dimostrare che $X$ determina $A$

### Esempio
Dato lo schema
- Orari(CodAula, NomeAula, Piano, Posti, Materia, CDL, Docente, Giorno, Ora)

$$F = \{ Docente,Giorno,Ora\to CodAula\;; CodAula\to NomeAula\;;Docente,Giorno,Ora\to NomeAula\}$$

Visto che abbiamo 
- $Docente,Giorno,Ora\to CodAula$ e $CodAula\to NomeAula$
- $Docente,Giorno,Ora\to NomeAula$ è ridondante
