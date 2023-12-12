Questa classe di protocolli permette 
- **l'esecuzione sovrapposta** e **non sincronizzata** di transazioni
- ed effettuano un **controllo su possibili conflitti** generati **solo dopo il commit**

Ogni transazione ==effettua **liberamente** le proprie operazioni== sugli oggetti del DB ==secondo **l'ordine temporale** con cui le operazioni sono generate== (in pratica in maniera sequenziale).

## Al commit
Viene **effettuato un controllo** mirato a stabilire se sono sorti conflitti.

In caso di conflitti:
1. ==viene effettuato **rollback delle azioni**== delle transazioni
2. le transazioni vengono rieseguite
 
## Fasi del protocollo
Le fasi di un protocollo ottimistico sono **tre**:
1. **Fase di lettura**
	- ogni transazione ==legge i valori **degli oggetti** del DB su cui deve operare==
	- questi valori vengono ==memorizzati in **variabili locali** (copie)== su cui verranno effettuati ==eventuali **aggiornamenti**==
2. **Fase di validazione**
	- vengono effettuati ==controlli sulla **[[Serializzabilità transazioni|serializzabilità]]**== nel caso in cui gli ==aggiornamenti locali dovessero essere **propagati sul DB**==
3. **Fase di scrittura**
	- gli aggiornamenti delle transazioni che hanno ==superato la **fase di validazione**== vengono ==propagati **definitivamente**== sugli oggetti del DB.