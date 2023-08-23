Il self join è un **caso molto particolare** di [[JOIN SQL|JOIN]]
- mette in relazione **una tabella con se stessa**

Questo lo si può ottenere
- **ridenominando due volte** la tabella **con due nomi diversi**
- **trattando** le due copie come se si trattasse **di due tabelle diverse**

In generale

```sql
SELECT X.A1, Y.A4
FROM Tab1 X, Tab2 Y, Tab1 Z
WHERE X.A2 = Y.A3 AND X.A2 = Z.A1
```

## Esempi
### Esempio 1
Trovare tutte le coppie di veicoli che sono dello stesso modello.
- Veicoli(<u>Targa</u>, Cod_mod\*, Cod_cat, Cilindrata, Cod_comb, cav.Fisc, Velocità, Posti, Imm)

```sql
SELECT V1.Targa, V2.Targa
FROM Veicoli V1, Veicoli V2
WHERE V1.Cod_mod = V2.Cod_mod 
	and V1.Targa <> V2.Targa -- per evitare ripetizioni
```

### Esempio 2
Trovare i colleghi di Mario Rossi (cioè quelli che lavorano nello stesso reparto)
- Impiegati(Nome, Cognome, Matricola, Reparto)

```sql
SELECT Imp1.*, Imp2.*
FROM Impiegati as Imp1 
JOIN Impiegati as Imp2 on (Imp1.Reparto = Imp2.Reparto)
WHERE Imp2.Nome = "Mario" and Imp2.Cognome = "Rossi"
```

1. **SELECT**: In questa parte della query, abbiamo l'elenco degli attributi che si desidera selezionare dalla tabella: tutte le colonne di `Imp1` e `Imp2`
    
2. **FROM**: La clausola FROM specifica le tabelle coinvolte nella query. Nella query fornita, ci sono due occorrenze della tabella "Impiegati" con gli alias "Imp1" e "Imp2".
    
3. **JOIN**: La clausola JOIN unisce due o più tabelle in base a una condizione specificata. Nella query, viene eseguito un join tra le tabelle "Impiegati" con gli alias "Imp1" e "Imp2" utilizzando la condizione `Imp1.Reparto = Imp2.Reparto`. Questo significa che verranno abbinate le righe di "Impiegati" che hanno lo stesso valore nel campo "Reparto".
    
4. **WHERE**: La clausola WHERE filtra i risultati del join in base a determinate condizioni. Nella query, viene applicata una condizione che richiede che l'impiegato "Mario" con cognome "Rossi" sia presente nella tabella "Impiegati" con l'alias "Imp2".