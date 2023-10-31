Dato un insieme $F$ di dipendenze funzionali, la chiusura di $F$ è $$F^{+}= \{ X \to Y \mid F \vdash X \to Y \}$$  Per verificare che una dipendenza funzionale $V\to W$ fa parte della chiusura $F^{+}$, cioè $V\to W \in F^{+}$, esiste un algoritmo efficiente
- che verifica l'appartenenza
- senza calcolare la chiusura di $F$

per farlo si basa sul **teorema** che 
- a partire da $F$ riesco a **derivare** $X \to Y$ solo se $Y$ è sottoinsieme della [[7. La Normalizzazione#Chiusura di un insieme|chiusura]] di $X$ rispetto a $F$ $$F \vdash X \to Y \iff Y \subseteq X_{F}^{+}$$

## Algoritmo per calcolare la chiusura di $X$ rispetto a $F$
Sia $X$ un insieme di attributi e $F$ un insieme di dipendenze funzionali, vogliamo calcolare $X_{F}^{+}$

> Ricordiamo che a monte l'algoritmo si basa sul teorema secondo cui $$F \vdash X \to Y \iff Y \subseteq X_{F}^{+}$$

1. Inizializziamo $X^{+}$ con l'insieme $X$
2. Se fra le dipendenze di $F$ c'è una dipendenza $Y\to A$ con $Y \subseteq X^{+}$ allora inseriamo $A$ $$X^{+}= X^{+}\cup \{A\}$$
3. GOTO 2. UNTIL finiscono gli attributi da aggiungere a $X^{+}$
4. RETURN $X_{F}^{+}= X^{+}$

### Terminazione dell'algoritmo
L'algoritmo termina perché ad ogni passo **viene aggiunto un nuovo attributo di $X^{+}$**
- essendo gli attributi in numero finito, a una certa **mi fermo per forza**

### Esempio
Abbiamo un insieme di dipendenze funzionali $F=\{ DB\to E, B\to C, A \to B \}$
- Trovare $\{AD\}^{+}$

1. Poniamo $$X^{+}= X = AD$$
2. In $F$ abbiamo la dipendenza $A\to B$ con $A\subseteq AD$, quindi inseriamo $B$ $$\{AD\}^{+}= ADB$$
3. In $F$ abbiamo la dipendenza $DB\to E$ con $DB \subseteq ADB$, quindi inseriamo $E$ $$\{AD\}^{+}= ADBE$$
4. In $F$ abbiamo la dipendenza $B\to C$ con $B \subseteq ADBE$ quindi inseriamo $C$ $$\{AD\}^{+}= ADBEC$$

oss: $AD$ è [[chiave#Superchiave|superchiave]] perché da $AD$ riesco a determinare ogni altro attributo della relazione.

- $A$ è superchiave? $$\{A\}^{+} = A\quad \{A\}^{+}= AB \quad \{A\}^{+}=ABC$$ manca $D$, quindi $A$ non è chiave
- $ABD$ è superchiave?
	- Si, perché $AD \subseteq ABD$, contiene la superchiave dunque per arricchimento lo è
- $ABC$ è superchiave? 
	- No, analogo di $A$
