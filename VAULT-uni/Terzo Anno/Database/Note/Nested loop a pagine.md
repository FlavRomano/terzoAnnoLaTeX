Molti DBMS utilizzano questa variante dove
- rinunciamo all'ordine imposto dalla relazione esterna
- per eseguire il **join** di **tutte le tuple in memoria** 
	- prima di **richiedere nuove pagine** della **relazione interna**

```py
for page_r in R:
	for page_s in S:
		"""esegui il join di tutte le tuple in page_r e page_s"""
```

![[Pasted image 20231031113101.png]]

Come è facile notare, prima $NP(R)$ era il numero di record $Nrec$ della relazione esterna $R$.
Questa strategia è molto versatile, infatti può essere usata anche nel caso in cui a $R$ siano **assegnati più buffer**.