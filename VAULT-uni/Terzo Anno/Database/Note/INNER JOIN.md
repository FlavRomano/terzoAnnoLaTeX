L’**INNER JOIN** è un’operazione di join in cui la condizione **non** sia necessariamente una **condizione di uguaglianza**.
## Esempi
### Esempio 1
Tutti i Veicoli la cui la cilindrata è minore della cilindrata media del loro modello.
- Modelli(<u>Cod_mod</u>, Nome_mod, Cod_Fab, Num_vers, Cil_media)
- Veicoli(<u>Targa</u>, Cod_mod\*, Cod_cat, Cilindrata, Cod_comb, cav.Fisc, Velocità, Posti, Imm)

```sql
SELECT Veicoli.*
FROM Modelli, Veicoli
WHERE Veicoli.Cod_mod = Modelli.Cod_mod and Modelli.Cil_media > Veicoli.Cilindrata
```

### Esempio 2
Supponendo di avere una tabella impiegati e una reparti, trovare il nome dei reparti in cui non lavora Mario Rossi.
- Impiegati(Nome, Cognome, IdReparto)
- Reparti(IdReparto, Nome)

```sql
SELECT Reparti.Nome
FROM Impiegati as imp 
JOIN Reparti as rep on imp.IdReparto <> rep.IdReparto
WHERE imp.Cognome = "Rossi" and imp.nome = "Mario"
```

1. **SELECT**: Questa parte della query specifica quale attributo o colonne verranno estratte dai risultati della query. In questo caso, l'attributo selezionato è `Reparti.Nome`, che indica che stiamo cercando di ottenere il nome dei reparti.
    
2. **FROM**: La clausola FROM indica da quali tabelle verranno prelevati i dati. Nella query, c'è un alias `imp` per la tabella "Impiegati". Ciò significa che la query preleverà dati dalla tabella "Impiegati" utilizzando l'alias `imp`.
    
3. **JOIN**: Questa clausola specifica il tipo di join che verrà eseguito. Nel caso presente, c'è un join tra la tabella "Impiegati" (alias `imp`) e la tabella "Reparti" (alias `rep`). La condizione di join è `imp.IdReparto <> rep.IdReparto`, che significa che verranno abbinate le righe in cui l'ID del reparto dell'impiegato è diverso dall'ID del reparto nel record del reparto. Questo tipo di join è detto "non corrispondente" o "join esterno".
    
4. **WHERE**: La clausola WHERE filtra i dati in base a determinate condizioni. Nella query, vengono applicate due condizioni: `imp.Cognome = "Rossi"` e `imp.Nome = "Mario"`. Questo significa che stiamo cercando gli impiegati con il cognome "Rossi" e il nome "Mario".