Si rappresentano 
- aggiungendo la [[chiave#Chiave esterna|chiave esterna]] 
	- a una qualunque delle due [[relazione|relazioni]] che riferisce l'altra relazione

![[Pasted image 20230817184407.png]]

nel caso di [[Vincolo di totalità|vincolo di totalità]]:
- la chiave esterna viene aggiunta alla relazione rispetto cui **associazione è totale**

## Esempio
Consideriamo l'associazione `Dirige(Professori, Dipartimenti)` con cardinalità $1:1$
- Un professore può o non può dirigere un solo dipartimento
- Un dipartimento deve avere un solo professore come dirigente

![[Pasted image 20230817185555.png]]

### Schema
- Professori(Nome: string, Cognome: string, Email: string, <u>Codice</u>: int)
- Dipartimenti(<u>Disciplina</u>: string, Polo: string, <u>Direttore*</u>: int)

> Professori

| Nome      | Cognome     | Email                  | <u>Codice</u> |
| --------- | ----------- | ---------------------- | ------ |
| Tizio     | De Tizis    | t.detizis@unipi.it     | 4023   |
| Caio      | Caiolis     | c.caiolis@unipi.it     | 4024   |
| Sempronio | Semprònioli | s.sempronioli@unipi.it | 4025   |

> Dipartimenti

| <u>Disciplina</u>        | Polo        | <u>Direttore*</u> |
| ----------------- | ----------- | ----------------- |
| Matematica        | Fibonacci   | 4023              |
| Ing. Aerospaziale | Porta Nuova | 4024              |
| Informatica       | Fibonacci   | 4025              |
