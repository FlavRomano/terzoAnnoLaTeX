Una relazione $r$ è in **forma normale di Boyce Codd**
- se per ogni [[dipendenza funzionale]] non banale $X\to Y$ definita su $r$, $X$ contiene almeno una [[chiave]] $K$ di $r$ (è una [[chiave#Superchiave|superchiave]])

> Occorre domandarsi se le parti sinistre nelle dipendenze funzionali siano ancora chiave

È ovvio che la forma normale richieda che **i concetti in una relazione siano omogenei**
- solo proprietà direttamente associate alla chiave

## In poche parole
Se esiste in $R$ una dipendenza $X\to A$ non banale ed $X$ **non è chiave**
- allora $X$ modella l'identità di un'entità **diversa** da quelle modellate dalla relazione $R$

### Esempio
- StudentiEdEsami(<u>Matricola</u>, Nome, Provincia, AnnoNascita, <u>Materia</u>, Voto)
- $Matricola\to Nome$

non è in forma normale di Boyce Codd perché $Matricola$ non è [[chiave#Superchiave|superchiave]]
- il $Nome$ dipende dalla $Matricola$, che non è chiave

## Definizione
Dato lo schema $R\langle T, F \rangle$, è in **forma normale di BC**
- se per ogni $X\to A\in F^{+}$, con $A$ non banale, vale che $X$ è [[chiave#Superchiave|superchiave]]

> Lo schema è in forma normale di BC se ogni parte sinistra delle dipendenze funzionali in $F^+$  è *superchiave*

## Teorema
Dato lo schema $R\langle T, F \rangle$, è in **forma normale di BC**
- se per ogni $X\to A\in F$, con $A$ non banale, vale che $X$ è [[chiave#Superchiave|superchiave]]

### Esempi
- Docenti(<u>CodiceFiscale</u>, Nome, Dipartimento, Indirizzo)
	- $F=\{ CF\to ND\;; D\to I \}$
	- Non è in forma normale di BC, $CF$ è chiave primaria. Un docente potrebbe avere più dipartimenti.
- Impiegati(<u>Codice</u>, Qualifica, <u>NomeFiglio</u>)
	- $F = \{C\to Q\}$
	- Non è in forma normale di BC, $codice$ da solo non è superchiave.
- Librerie(<u>CodiceLibro</u>, <u>NomeNegozio</u>, IndNegozio, Titolo, Quantità)
	- Non è in forma normale di BC: un libro potrebbe avere più titoli e un negozio più sedi.

## Algoritmo di analisi
La relazione $R\langle T, F\rangle$ è decomposta in 
- $R_{1}(X,Y)$
- $R_{2}(X,Z)$

su di esse si ripete questo procedimento esponenziale

```py
def algoritmo_di_analisi(T,F):
	assert('F copertura canonica')
	rho = set(R<T,F>)
	while esiste in rho una Ri<Ti,Fi> non in FNBC per la DF X->A:
		# X diventa chiave per R
		Ta = chiusura(X)
		Fa = proiezione(di=Fi, su=Ta)

		# elimino dagli attributi tutti gli elementi della chiusura di X
		Tb = Ti - chiusura(X) + X 
		Fb = proiezione(di=Fi, su=Tb) 
		# Ra ed Rb sono nomi nuovi
		rho = rho - Ri + set(Ra<Ta, Fa>, Rb<Tb, Fb>)
	return rho
```

### Proprietà
Questo algoritmo **preserva i dati**, ma non necessariamente le dipendenze.

### Esempio
Lo schema 
- Docenti(<u>CodiceFiscale</u>, Nome, Dipartimento, Indirizzo)
- $F=\{CF\to Nome,Dipart\;;Dipart\to Indirizzo\}$

1. I membri sinistri in $F$ sono superchiavi?
	- $CF$ si, perché $$(CF)^{+} = CF,Nome,Dip,Ind$$
	- $Dip$ no, perché $$(Dip)^{+} = Dip, Ind$$
		- la dipendenza funzionale $Dipart\to Indirizzo$ non rende la relazione in BCNF
2. Occorre decomporlo in due relazioni separate considerando che $Dipart\to Indirizzo$ è quella problematica
	- $R_{1}(Dip, Ind)$ con $F_{1}=\{Dip \to Ind\}$
	- $R_{2}(CF, Nome, Dipartimento)$ con $F_{2} = \{CF\to Nome, Dipartimento\}$
3. Sia i membri sinistri di $F_{1}$ che di $F_{2}$ sono chiavi primarie per le rispettive relazioni, quindi abbiamo finito.

Preserva dati e dipendenze.

### Esempio
Si consideri il seguente schema relazionale $$R\langle T, F\rangle$$ con $$T = ABCDE \quad F=\{ CE\to A, D\to E, CB\to E, CE\to B \}$$
1. Le parti sinistre di $F$ sono tutte [[chiave#Superchiave|superchiavi]]? No
	- Basta fare la chiusura della prima $$(CE)^{+}= CEAB\quad\text{manca D}$$
2. Allora decomponiamo in
	- Lo schema $R_1$ $$T_{1}= (CE)^{+}= CEAB$$ con $$F_{1}= \pi_{CEAB}(F) = \{ CE\to A, CB\to E, CE\to B \}$$
		- In questa relazione non ci sono problemi, ogni membro sinistro in $F_{1}$ è superchiave
	- Lo schema $R_2$ $$T_{2}= T - (CE)^{+} + CE = ABCDE - CEAB + CE = DCE$$ con $$F_{2}=\pi_{DCE}(F)=\{ D\to E \}$$
		- In questa relazione CI SONO problemi, l'unico membro sinistro in $F_{2}$ NON è superchiave $$(D)^{+}= DE\quad\text{manca C}$$
3. Decomponiamo, stavolta $R_{2}\langle DCE, F_{2}=\{ D \to E \}\rangle$ in
	- Lo schema $$R_{3} = (D)^{+} = DE$$ con $$F_{3}= \{ D\to E \}$$
		- in questa relazione non ci sono problemi
	- Lo schema $$R_{4} = T_{2} - (D)^{+} + D = DCE - DE + D = CD$$ con $$F_{4}= \{\}$$
		- in questa relazione non ci sono problemi

Abbiamo ottenuto quindi la decomposizione $$\rho = \{R_{1}\langle (ABCE), CE\to A, CB\to E, CE\to B\rangle,  R_{3}\langle(DE),  D\to E \rangle, R_{4}\langle(CD), \emptyset\rangle\}$$