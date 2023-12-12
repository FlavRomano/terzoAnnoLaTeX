Si rappresenta 
- aggiungendo allo schema una nuova [[relazione]] 
	- contenente **due** [[chiave#Chiave esterna|chiavi esterne]]
	- che riferiscono le due relazioni coinvolte

la [[chiave#Chiave Primaria|chiave primaria]] di questa relazione è costituita dall'insieme di tutti i suoi attributi.

![[Pasted image 20230817191013.png]]

## Esempio 1
![[Pasted image 20230817191412.png]]

Nella prima figura
- non posso mettere una [[chiave#Chiave esterna|chiave esterna]] su uno dei due lati, perché potrei escludere dei casi

Nella seconda figura
- devo aggiungere una [[tabella]] che prende almeno due attributi
	- uno che punti alla [[chiave#Chiave Primaria|chiave primaria]] della prima classe
	- uno che punti alla [[chiave#Chiave Primaria|chiave primaria]] della seconda classe

## Esempio 2
![[Pasted image 20230817191516.png]]
