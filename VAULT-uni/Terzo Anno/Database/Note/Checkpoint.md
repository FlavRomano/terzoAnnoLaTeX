È un *punto di allineamento*. Si scrive la marca **CKP** sul log per indicare che tutte le operazioni **che la precedono** sono state ==effettivamente **effettuate** sul DB==.

Un modo *semplice* di fare CKP:
1. si sospende l'attivazione di nuove transazioni
2. si completano le precedenti e si allinea il DB (si scrivono su disco le *pagine sporche* del buffer)
3. si scrive la marca CKP nel log
4. si riprende l'esecuzione

Per non interrompere le transazioni, si può inserire un **BeginCkp** e un **EndCkp**
- si scrive nel log una marca di inizio checkpoint $$(\textbf{BeginCkp}, \;\{ T_1,\ldots,T_n \})$$ che riporta l'elenco delle transazioni attive $\{T_{1},\ldots,T_n\}$
- in parallelo alle normali operazioni delle transazioni, il gestore del buffer **riporta sul disco tutte le pagine modificate**
- si scrive nel log una marca di fine checkpoint $$\textbf{EndCkp}$$ che certifica che tutte le scritture avvenute prima del **BeginCkp** ora sono sul disco
- le transazioni loggate nel mezzo possono essere o non essere state copiate su disco. ==**abbiamo certezza fino al Begin CKP, quello che succede fino all'END non si sa**==.