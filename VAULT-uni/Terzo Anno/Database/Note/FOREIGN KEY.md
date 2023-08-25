I comandi **REFERENCES** e **FOREIGN KEY** permettono di definire [[vincolo di integrità referenziale|vincoli di integrità referenziale]].
La sintassi è simile a quella vista in precedenza, abbiamo
- per singoli attributi, come **vincolo di colonna**
- su una lista di attributi, come **vincolo di tabella**

Come vincolo di colonna:
```sql
CREATE nomeTabella(
	attributo_1 --...
	attributo_2 --...
	-- ...
	attributo_k REFERENCES tabella_riferita(colonna_riferita)
	-- ... 
)
```

Come vincolo di tabella:
```sql
CREATE nomeTabella(
	attributo_1 -- ...
	attributo_2 -- ...
	-- ...
	attributo_n -- ...
	FOREIGN KEY(colonne_referenti) REFERENCES tabella_riferita(colonne_riferite)
	-- ... 
)
```

![[Pasted image 20230824174230.png]]
## Tabella referente e tabella riferita
Distinguiamo 
- [[Tabella referente]]
	- quella in cui viene **definita la chiave esterna** (foreign key). 
		- questa chiave esterna **fa riferimento** a una colonna o insieme di colonne nella **tabella riferita**
	- è la tabella che contiene **il riferimento ai dati nella tabella riferita**.
- [[Tabella riferita]]
	- quella a cui **si fa riferimento tramite la chiave esterna nella tabella referente**. 
		- la **chiave esterna nella tabella referente** si riferisce a una **chiave primaria** o a una colonna univoca **nella tabella riferita**. 
	- Questa tabella **fornisce** i **dati di riferimento** a cui la chiave esterna fa riferimento.


## Violazione di una foreign key
Quando si crea un vincolo di **foreign key** si può specificare l'azione da intraprendere quando delle righe della **tabella riferita** vengono cancellate o modificate. Vengono dichiarate al momento della definizione mediante i comandi
- **ON DELETE**
- **ON UPDATE**

Come vincolo di colonna
```sql
CREATE TABLE nomeTabella (
	nomeColonna1 --...
	--...
	nomeColonnaK tipoColonnaK REFERENCES tabellaRiferita(colonnaRiferita)
			ON DELETE[/ON UPDATE] reazione
	--...
)
```

Come vincolo di tabella
```sql
CREATE TABLE nomeTabella (
	nomeColonna1 --...
	--...
	nomeColonnaN --... 
	FOREIGN KEY tabellaReferente(colonnaReferente) 
		REFERENCES tabellaRiferita(colonnaRiferita) ON DELETE[/ON UPDATE] reazione
)
```

### Reazione alla `ON DELETE`

Come `reazione` possiamo mettere:
- **NO ACTION** per impedire il delete delle righe della tabella riferita 
	- questa opzione è di default
- **CASCADE** per generare un delete a cascata
	- cancella tutte le righe dipendenti dalla tabella quando la corrispondente riga è cancellata dalla tabella riferita.
- **SET NULL** nome omen
	- assegna [[NULL]] ai valori della colonna che ha il vincolo **foreign key** nella tabella, quando la riga corrispondente viene cancellata dalla tabella riferita.
- **SET DEFAULT** per assegnare un valore di default
	- assegna il valore di default ai valori della colonna che ha il vincolo foreign key nella tabella quando la riga corrispondente viene cancellata dalla tabella riferita.

### Reazione alla `ON UPDATE`
Come `reazione` possiamo mettere:
- **CASCADE** dove le righe della tabella referente vengono **impostate** ai valori della tabella riferita
- **SET NULL** dove i valori della tabella referente vengono impostati a [[NULL]]
- **SET DEFAULT** dove i valori della tabella referente vengono impostati al valore di default
- **NO ACTION** come sopra, si rifiutano gli aggiornamenti che violino l'integrità referenziale.