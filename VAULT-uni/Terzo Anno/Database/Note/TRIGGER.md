Un trigger viene eseguito, **indipendentemente dal numero di righe** che vengono modificate, quando si verifica un **determinato evento** nel database.

```sql
CREATE TRIGGER nomeTrigger
BEFORE|AFTER
INSERT|UPDATE|DELETE
ON nomeTabella
[FOR EACH ROW]
DECLARE  --...
BEGIN    --...
	-- ...
END      --...
```

Se viene specificato `FOR EACH ROW`, allora parliamo di **trigger a livello di riga**:
> vengono **eseguiti una volta per ciascuna riga modificata** in una transazione.

la possiamo vedere come la **granularità** del comando.
## Before e After
All'interno del trigger è possibile fare riferimento ai vecchi e nuovi valori coinvolti nella transazione.
- se usiamo **BEFORE** `UPDATE`
	- per **valori vecchi** intendiamo i **valori che sono in tabella** e che vogliamo modificare
	- per **valori nuovi** intendiamo i **valori che vogliamo inserire** al posto dei vecchi.
- se usiamo **AFTER** `UPDATE`
	- per **valori vecchi** intendiamo quelli che **c'erano prima** dell'[[UPDATE SQL|UPDATE]]
	- per **valori nuovi** intendiamo quelli presenti nella **tabella alla fine della modifica**

## Instead Of
Con il comando **INSTEAD OF** si specifica che cosa fare **invece di eseguire le azioni che hanno attivato il trigger**.
e.g:
> È possibile utilizzare un trigger `INSTEAD OF`: 
> - per reindirizzare le `INSERT` in una tabella `T1` a una tabella `T2` 
> - oppure per aggiornare con `UPDATE` più tabelle che siano parte di una vista

Devono essere **a livello di riga**.

## Esempi
### Esempio 1
```sql
CREATE TRIGGER ControlloStipendio
BEFORE 
INSERT ON Impiegati 
DECLARE
	StipendioMedio FLOAT 
BEGIN
	SELECT avg(Stipendio) INTO StipendioMedio 
	FROM Impiegati  
	WHERE Dipartimento = :new.Dipartimento;
	IF :new.Stipendio > 2 * StipendioMedio  
		THEN RAISE_APPL._ERR.(-2061, ‘Stipendio alto’) 
	END IF;
END;
```

