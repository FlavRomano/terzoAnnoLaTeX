L’operatore **UNION** realizza l’operazione di unione definita nell’algebra relazionale. 

Utilizza 
- come operandi le due tabelle risultanti da comandi `SELECT` 
- restituisce una **terza tabella** che contiene **tutte le righe della prima** e della **seconda tabella**. 

Nel caso in cui dall’unione e dalla proiezione risultassero delle righe duplicate
- l’operatore UNION **ne mantiene una sola copia**
	- a meno che non sia specificata l’opzione `ALL` che indica la volontà di mantenere i duplicati

## Esempio
![[Pasted image 20230823184118.png]]

```sql
SELECT Padre, Figlio -- se avessimo scritto "Padre as Genitore" e "Madre as Genitore"
FROM Paternità
UNION
SELECT Madre, Figlio -- l'ordine degli attributi sarebbe importato eccome
FROM Maternità
```

![[Pasted image 20230823184136.png]]

L'attributo `Padre` della prima `SELECT` viene messo nella stessa colonna con l'attributo `Madre` della `SELECT` seguente.