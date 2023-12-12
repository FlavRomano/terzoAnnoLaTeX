È un'operazione che produce una ==copia completa del DB==
- viene fatta in **mutua esclusione** con tutte le altre transazioni ==quando il sistema non è operativo==
- viene memorizzata in memoria stabile (backup)
- una volta terminato, nel log viene inserito un record di dump che segnala ==l'avvenuta esecuzione dell'operazione in un dato istante==. Quindi il sistema riprende il funzionamento.