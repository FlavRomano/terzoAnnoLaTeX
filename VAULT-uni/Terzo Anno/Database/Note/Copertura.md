Data una relazione $R\langle T, F\rangle$, due insiemi di [[dipendenza funzionale|dipendenze funzionali]] $F$ e $G$ sono **equivalenti** su $R$ 
- se le dipendenze funzionali in $F$ mi permettono di **derivare** le dipendenze di $G$, cioè $$F\equiv G\iff F^{+}=G^{+}$$ dove $F^{+}$ è la [[7. La Normalizzazione#Chiusura di un insieme|chiusura]] di $F$ (analogo per $G^{+})$.

Di conseguenza diremo che $F$ **è una copertura di** $G$ e viceversa.
## Esempio 
Dato lo schema $$studenti(matricola, CF, Cognome, Nome, Anno)$$ 
- $F= \{ Matricola\to CF,Cognome,Nome,Anno\;;CF\to Matricola, Cognome, Nome, Anno \}$
- $G=\{ Matricola \to CF,Cognome,Nome,Anno\;; CF\to Matricola\}$

$F$ è una **copertura** di $G$, infatti

$$F^{+}= Matricola, CF, Cognome, Nome, Anno$$ $$G^{+}= CF, Matricola, Cognome, Nome, Anno$$$$F^{+}= G^{+}\implies F \equiv G$$