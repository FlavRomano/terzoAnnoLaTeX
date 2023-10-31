Data un'[[istanza valida]] $r$ sullo schema $R(T)$
- $R$ nome dello schema
- $T$ insieme degli attributi che definiscono la struttura della relazione

siano $X$ e $Y$ due sottoinsiemi (insiemi disgiunti di attributi) non vuoti di $T$:
- una **dipendenza funzionale** fra gli insiemi di attributi $X$ e $Y$ **è un vincolo** sulle istanze della relazione $R$ espresso nella forma $$X \to Y$$
	- $X$ **determina funzionalmente** $Y$, o $Y$ **è determinato da** $X$ 
	- se per ogni istanza valida $r\in R$  un valore di $X$ **determina univocamente** un valore di $Y$ $$\forall \;r\in R.\forall\;t_{1},t_{2}\in r. \; t_{1}[X] = t_{2}[X] \implies t_{1}[Y] = t_{2}[Y]$$
----
1. Una dipendenza funzionale è detta **banale** se è sempre valida, ad esempio $$A\in X, X\to A$$ altrimenti è detta **non banale** (cioé quando $X\to A$ e $A$ non è contenuta in $X$)
2. Le dipendenze funzionali sono una **proprietà semantica**:
	- **dipendono dai fatti rappresentati**
	- non dipendono dalla rappresentazione degli attributi negli schemi
3. Sono una **generalizzazione del vincolo** di [[chiave]] e [[chiave#Superchiave|superchiave]]

## Notazione
Dato uno schema $R(T)$ e un insieme di dipendenze funzionali $F$ valide per $R$, possiamo utilizzare la seguente notazione $$R\langle T,F\rangle$$

## Dipendenze funzionali atomiche
Ogni dipendenza funzionale si può **scomporre in più dipendenze funzionali** $$X\to A_{1},A_{2},\ldots,A_{n}\quad \text{si può scomporre in}\quad X\to A_{1};X\to A_{2};\ldots;X\to A_{n}$$ 
Dipendenze funzionali del tipo $X\to A$ ($X$ determina funzionalmente $A$) si chiamano **dipendenze funzionali atomiche**.

### Esempio
- DotazioniLibri(CodiceLibro, NomeNegozio, IndNegozio, Titolo, Quantità)

Da $CodiceLibro$ e $NomeNegozio$ riusciamo a determinare funzionalmente $IndNegozio, Titolo, Quantità$  $$CodiceLibro,NomeNegozio\to IndNegozio,Titolo,Quantità$$ da cui ricaviamo tre dipendenze atomiche $$CodiceLibro,NomeNegozio\to IndNegozio\quad CodiceLibro,NomeNegozio\to Titolo$$ $$CodiceLibro,NomeNegozio\to Quantità$$

## Dipendenza funzionale completa
Data una relazione $R(T)$
Una **dipendenza funzionale** $X\to Y$ si dice **completa** quando per ogni $W\subset X$ **non vale** $W\to Y$.
- se $X$ è [[chiave#Superchiave|superchiave]], allora $X$ determina **ogni altro attributo** della relazione $X\to T$
- se $X$ è [[chiave]] (in particolare fissa il termine *superchiave minimale*), allora $X\to T$ è una **dipendenza funzionale completa**
	- oss: $X$ chiave **è** una superchiave minimale, quindi **non posso togliere nessun attributo da** $X$ (in quanto chiave)

## Dipendenze implicate
Data una relazione $R(T)$, sia $F$ un insieme di dipendenze funzionali sullo schema $R$.
Diremo che $$F\vDash X\to Y$$ cioè $F$ **implica logicamente** $X\to Y$ 
- se ogni istanza di $r\in R$ che soddisfa $F$ soddisfa anche $X\to Y$

### Esempio
Sia $r\in R\langle T, F\rangle$, con $F=\{X\to Y, X \to Z\}$ e $X,Y,Z\subseteq T$.
Sia $X' \subseteq X$, le seguenti dipendenze funzionali sono **soddisfatte** da $r$:
- $X\to X'$ (dipendenza funzionale banale perché $X'\subseteq X$)
- $X\to YZ$ per definizione di [[dipendenza funzionale#Dipendenze funzionali atomiche|dipendenza funzionale atomica]]$$\{ X\to Y, X\to Z \}\vDash X\to YZ$$

## Esempi
### Esempio 1
Data la seguente relazione:
- Persone(CodiceFiscale, Cognome, Nome, DataNascita)

Una dipendenza funzionale è formata da $X=\{CodiceFiscale\}$ e $Y=\{Cognome, Nome\}$, quindi $$CodiceFiscale\to Cognome,Nome$$

### Esempio 2
- StudentiEdEsami(Matricola, Nome, Provincia, AnnoNascita, Materia, Voto)

Una dipendenza funzionale è formata da $X=\{Matricola\}$ e $Y=\{Nome, Provincia, AnnoNascita\}$, quindi $$Matricola\to Nome, Provincia, AnnoNascita$$

### Esempio 3
Questa tabella soddisfa la dipendenza funzionale $$Matricola\to Cognome$$

![[Pasted image 20230825184828.png]]

e.g: $1\to Rossi, 2\to Verdi, \ldots$  ma non vale il viceversa della dipendenza funzionale, $$\text{Non vale }Cognome\to Matricola$$ 
perché $Rossi\to Matricola\{1,3\}$

### Esempio 4
DotazioniLibri(CodiceLibro, NomeNegozio, IndNegozio, Titolo, Quantità)

- da $CodiceLibro$ determiniamo funzionalmente il $Titolo$ 
- da $NomeNegozio$ determiniamo funzionalmente $IndNegozio$
- da $CodiceLibro, NomeNegozio$ determiniamo quindi $IndNegozio, Titolo, Quantità$. 

Cioé
$$DF = \{CodiceLibro\to Titolo \; ; \;IndNegozio \to CodiceLibro, NomeNegozio;$$
$$\;CodiceLibro, NomeNegozio\to IndNegozio, Titolo, Quantità \}$$ Qua notiamo subito **un'anomalia**, infatti nell'ultima dipendenza funzionale $$CodiceLibro, NomeNegozio\to IndNegozio, Titolo, Quantità$$ abbiamo $IndNegozio$ e $Titolo$ che sono **già dedotti** dalle dipendenze funzionali precedenti.

Per eliminare questa anomalia semplicemente togliamo gli attributi che provocano **ridondanza**, ottenendo come risultato finale $$CodiceLibro, NomeNegozio\to Quantità$$
