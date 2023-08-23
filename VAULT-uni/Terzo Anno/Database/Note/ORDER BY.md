E‘ possibile dare un ordinamento del risultato di una select. L‘ordinamento si può effettuare in base a un attributo, e può essere crescente o decrescente.

```sql
SELECT lista_attributi  
FROM nome_tabella  
WHERE condizioni  
ORDER BY Attributo ASC --DESC
```

- Le righe vengono ordinate in base al campo `Attributo` in maniera crescente o decrescente secondo se è data la specifica `ASC` o `DESC`. 
	- ASC è il default. 
- Secondo il tipo dell’attributo, l’ordinamento è quello più naturale su quel dominio.

## Doppio ordinamento
Si può 
- voler ordinare i dati in base a una certa chiave (attributo) 
- e poi ordinare i dati che coincidono su quella chiave in base a un’altra chiave (attributo)

### Esempio 
Ordinare gli studenti 
- in base al loro cognome, 
	- in modo tale che due persone con lo stesso cognome siano ordinate in base al nome, 
	- e persone con lo stesso nome e cognome siano ordinate in base all’ordine inverso della data di nascita

```sql
Select *  
From Studenti  
Order by cognome asc, nome asc , nascita desc
```

## Esempio
Nome e reddito delle persone con meno di trenta anni in ordine alfabetico

```sql
SELECT nome, reddito 
FROM persone 
WHERE reddito < 30 
ORDER BY nome
```

![[Pasted image 20230822170441.png]]