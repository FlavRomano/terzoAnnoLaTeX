Può succedere comunque che nelle **tabelle coinvolte** ci siano **più attributi con lo stesso nome**
- col [[JOIN SQL#Clausola EQUI-JOIN e NATURAL-JOIN|NATURAL JOIN]] abbiamo problemi di ambiguità tra queste coppie di attributi.

Se invece vogliamo eseguire una join in cui **la condizione riguarda solo una o alcune di queste coppie di attributi**, si usa la clausola `USING` seguita dall'elenco degli attributi coinvolti nella condizione

```sql
SELECT listaAttributi
FROM Tab1
JOIN Tab2 USING (attr1, attr2, ...)
```

## Esempio
Il nome del modello del veicolo di targa ZX2345
- Modelli(<u>Cod_mod</u>, Nome_mod, Cod_Fab, Num_vers, Cil_media)
- Veicoli(<u>Targa</u>, Cod_mod\*, Cod_cat, Cilindrata, Cod_comb, cav.Fisc, Velocità, Posti, Imm)

```sql
SELECT Nome_mod
FROM Modelli
JOIN Veicoli USING (Cod_mod)
WHERE Targa = "ZX2345"
```