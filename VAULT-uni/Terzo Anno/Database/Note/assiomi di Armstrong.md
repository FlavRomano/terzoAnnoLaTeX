Sono delle regole di inferenza.
Data una relazione $R(T)$ con $X,Y\subseteq T$
## Riflessività R
Se $Y\subseteq X$ allora $X$ **determina funzionalmente** $Y$
$$Y\subseteq X\vDash X\to Y$$

## Arricchimento A
Se $X \to Y$ e $Z\subseteq T$ allora $XZ$ **determina funzionalmente** $YZ$
$$X\to Y,\; Z\subseteq T \vDash XZ \to YZ$$

### Esempio
- Persona(Nome, Cognome, CF, Regione, Città)

$$CF \to Città \vDash CF, Regione \to Città, Regione$$

## Transitività T
Se $X\to Y$ e $Y\to Z$ allora $X$ **determina funzionalmente** $Z$ 
$$X\to Y,Y\to Z\vDash X\to Z$$

## Correttezza e completessa
```ad-theo
title: Teorema
Gli assiomi di Armstrong sono **corretti e completi**, posso applicarli tante volte tanto prima o poi mi fermo.
```

Attraverso gli assiomi di Armstrong, si può mostrare l'**equivalenza** tra:
- **implicazione logica** $\vDash$ e **derivazione** $\vdash$
	- se una dipendenza è **derivabile con gli assiomi di Armstrong** allora è anche **implicata logicamente** (*correttezza* degli assiomi) $$\forall f.\; F\vdash f \implies F \vDash f$$
	- se una dipendenza è **implicata logicamente** allora è anche **derivabile con gli assiomi di Armstrong** (*completezza* degli assiomi) $$\forall f.\; F\vDash f \implies F \vdash f$$

n.b: $f$ è una dipendenza