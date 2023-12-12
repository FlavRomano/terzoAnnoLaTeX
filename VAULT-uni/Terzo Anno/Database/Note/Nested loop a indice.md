Data una tupla della relazione esterna **$R$**, la **scansione completa** della relazione interna **$S$** può essere sostituita da una **scansione basata su un indice** costruito sugli **attributi di join di $S$**

```py
i = 0
for record_r in R:
	for record_s in get_through_index("I,S.i = r.i", i)
		"""aggiungi <r,s> al risultato"""
	i += 1
```

![[Pasted image 20231031114632.png]]

Quello che accade nella figura sopra è:
1. **Input: 22**, l'output saranno tutte le ennuple di **$S$** che hanno 22 su $A$
2. **Input: 32**, l'output saranno tutte le ennuple di **$S$** che hanno 32 su $A$
3. ...

Bisogna premurarsi di avere ==**la tabella con meno ennuple** come operando sinistro della join e di avere la condizione sulla **chiave primaria**==, altrimenti si va incontro a problemi di complessità.

oss: Il costo di accesso ad $S$ dipende dall'**implementazione dell'indice**.

L'accesso alla relazione interna mediante indice riduce di molto i costi d'esecuzione del nested loop join.

