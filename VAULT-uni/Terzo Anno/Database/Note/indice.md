L'indice è una **struttura che contiene informazioni**
- sulla **posizione** di memorizzazione delle tuple in base al valore del **campo chiave**

Velocizza l'accesso casuale via chiave di ricerca.

Possiamo distinguere:
1. **Indice PRIMARIO**, la chiave di ordinamento del file sequenziale **coincide** con la *chiave di ricerca dell'indice*
2. **Indice SECONDARIO**, la chiave di ordinamento e la chiave di ricerca sono **diverse**

Un indice può essere definito su un insieme di attributi $A_{1},\ldots,A_{n}$
- l'indice **contiene un record per ogni combinazione** di valori assunti dagli attributi nella relazione
- può essere utilizzato per rispondere in modo efficiente ad **interrogazioni che specificano un valore** per **ciascuno** di questi **attributi**
- più indici comportano maggiore costo per il mantenimento

## Indice primario
Un indice **primario** 
- è un **file ordinato**
- i cui record sono di **lunghezza fissa** e costituiti da due campi:
	1. Il primo campo è dello stesso tipo del **campo chiave di ordinamento** ([[chiave#Chiave Primaria|chiave primaria]])
	2. Il secondo campo è un **puntatore a un blocco del disco**

Esiste **un record nel file dell'indice** per ogni blocco nel file di dati 

![[Pasted image 20230830193322.png]]

## Indice secondario
Un indice **secondario**
- può essere definito su un **campo non chiave** 
	- che è una [[Chiave candidata|chiave candidata]] e **ha valori unici**
	- oppure su un campo non chiave con **valori duplicati**

I record presentano due campi:
1. il primo campo è dello stesso tipo del campo che **non viene usato per ordinare il file**, è chiamato **campo di indicizzazione**
2. il secondo campo è un **puntatore al blocco** oppure un **puntatore al record** (*RID*)

![[Pasted image 20230830193958.png]]

e.g:
![[Pasted image 20230830194109.png]]

$Matr$ è un attributo chiave primaria mentre $An$ non lo è (può avere duplicati).