Quando vengono correlate mediante una [[JOIN SQL|JOIN]] delle tabelle con colonne contenenti dati in comune, è possibile che **un valore sia presente** in una delle colonne **e non nell'altra**.
- effettuando una [[JOIN SQL#Clausola EQUI-JOIN e NATURAL-JOIN|EQUI JOIN]] la **riga corrispondente a tale valore viene scartata**

In alcuni casi può essere **necessario mantenere questi valori**, per fare ciò si utilizza una `OUTER JOIN`

```sql
SELECT lista_attributi  
FROM Tab1 
LEFT [OUTER] JOIN Tab2 -- [OUTER] si può omettere
--
SELECT lista_attributi  
FROM Tab1 
RIGHT [OUTER] JOIN Tab2 
--
SELECT lista_attributi  
FROM Tab1 
FULL [OUTER] JOIN Tab2 
--
```

## Esempi
### Esempio 1
Ottenere i codici e nomi delle categorie di macchine della tabella veicoli e anche quelli per cui non esiste nessun veicolo.
- Categorie(<u>Cod_cat</u>, Nome_cat)
- Veicoli(<u>Targa</u>, Cod_mod\*, Cod_cat, Cilindrata, Cod_comb, cav.Fisc, Velocità, Posti, Imm)

```sql
SELECT Cod_cat, Nome_cat
FROM Categorie
LEFT JOIN Veicoli on Categorie.Cod_cat = Veicoli.Cod_cat
```

```sql
SELECT Cod_cat, Nome_cat
FROM Veicoli
RIGHT JOIN Categorie on Veicoli.Cod_cat = Categorie.Cod_cat
```

### Esempio 2
Per ogni persona, elencare il padre e, se nota, la madre
- Maternità(Madre, Figlio)
- Paternità(Padre, Figlio)

```sql
SELECT P.Figlio, Padre, Madre
FROM Paternità as P
LEFT JOIN Maternità as M on P.Figlio = M.Figlio
```

