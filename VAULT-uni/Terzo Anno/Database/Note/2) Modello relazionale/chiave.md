## Superchiave
Un insieme $K$ di attributi è **superchiave** per $r$ se 
- $r$ **non contiene** due [[2. Modello relazionale#Vantaggi|ennuple]] distinte $t_1$ e $t_2$ con $$t_{1}[K] = t_{2}[K]$$

$K$ è **chiave** per $r$ se
- $K$ è una **superchiave minimale** per $r$
	- cioè non contiene un'altra superchiave

## Chiave Primaria
Una **chiave primaria** è una chiave su cui **NON** sono ammessi [[Valore nullo|valori nulli]]

n.b: Come notazione si predilige la sottolineatura dell'attributo all'interno dello [[schema di relazione]].

![[Pasted image 20230817162037.png]]

## Chiave esterna
È un [[vincolo di integrità referenziale]] tra due o più [[tabella|tabelle]]. Essa 
- identifica una o più colonne di una tabella, detta tabella **referenziante**
- che referenzia una o più colonne di un'altra tabella, detta **referenziata**.

```sql
CREATE TABLE 
	Fattura (
	  id INTEGER PRIMARY KEY,
	  data_acquisto DATE,
	  id_cliente INTEGER,
	  FOREIGN KEY(id_cliente) 
		  REFERENCES Cliente(id) ON DELETE CASCADE
	)
```

```ad-example
Questo codice andrà a creare una tabella `fattura` in cui si potranno specificare un `id` ([[chiave#Chiave Primaria|chiave primaria]]), una `data_acquisto` e un `id_cliente`. Il vincolo imposto è che `id_cliente` sia contenuto nella colonna `id` della [[tabella]] `cliente`.
```


> Fattura

| <u>id</u>  | data_acquisto | id_cliente* |
| --- | ------------- | ---------- |
| 1   | 20/03/2018    | 111        |
| 2   | 20/03/2018    | 123        |
| 3   | 15/05/2020    | 111        |

> Cliente

| <u>id</u>  | nome                 |
| --- | -------------------- |
| 111 | Wikimedia Foundation |
| 123 | Jimbo S.p.A.         |
| 200 | Cool gadgets Inc.    |
