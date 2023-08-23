## Proiezione sul join
Date due relazioni $R_{1}(X_{1})$ e $R_{2}(X_{2})$ vale che $$\pi_{X_{1}}(R_{1}\bowtie R_{2}) \subseteq R_{1}$$ la proiezione del join su un attributo da luogo a tabelle diverse da quelle da cui il join è stato ottenuto.

![[Pasted image 20230820184723.png]] 

$Impiegati \bowtie Reparti$ produce

![[Pasted image 20230820184753.png]] 

$\pi_{Impiegato, Reparto}(Impiegati \bowtie Reparti)$ produce una tabella diversa rispetto a $Impiegati$  (un sottoinsieme di ennuple) 

![[Pasted image 20230820185555.png]]

allo stesso modo $\pi_{Reparto,Capo}(Reparti \bowtie Impiegati)$ produce una tabella diversa rispetto a $Reparti$  (un sottoinsieme di ennuple) 

![[Pasted image 20230820185704.png]]

## Join di proiezioni
Data una relazione $R(X_{1},X_{2})$ vale che $$R\subseteq(\pi_{X{1}}(R))\bowtie (\pi_{X_{2}}(R))$$ il join delle proiezioni di una tabella, può dare luogo a una tabella più grande

![[Pasted image 20230820190247.png]]

eseguiamo due proiezioni su attributi diversi, ottenendo 

![[Pasted image 20230820190440.png]] 

eseguendo il join di queste due proiezioni, cioé $$(\pi_{Impiegato, Reparto}(newImpiegatiReparti))\bowtie (\pi_{Reparto, Capo}(newImpiegatiReparti))$$ ottengo una tabella più grande rispetto a quella di partenza

![[Pasted image 20230820190630.png]]