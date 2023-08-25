Spesso in questo caso si utilizzano le [[Subquery scalare|subquery scalare]] perché ci interessa un valore solo da utilizzare come confronto.

e.g: Trovare tutti i veicoli della categoria "Autovettura"
- Categorie(Cod_cat, Nome_Cat)
- Veicoli(<u>Targa</u>, Cod_mod\*, Categoria, Cilindrata, Cod_comb, cav.Fisc, Velocità, Posti, Imm)

Mediante [[JOIN SQL|Join]]:
```sql
SELECT Veicoli.*
FROM Categorie, Veicoli
WHERE Categoria = Cod_cat and Nome_Cat = "Autovettura"
```

Mediante **subquery**:
```sql
SELECT *
FROM Veicoli
WHERE Categoria = (
	SELECT Cod_cat
	FROM Categorie
	WHERE nome_cat = "Autovettura"
) -- questa subquery restituisce un singolo valore
```
