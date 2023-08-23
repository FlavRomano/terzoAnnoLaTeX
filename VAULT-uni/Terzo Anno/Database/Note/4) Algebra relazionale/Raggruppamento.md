Con l'operatore ${}_{\{A_{i}\}} \gamma_{\{f_{i}\}}(R)$ si effettua il raggruppamento:
- $A_{i}$ sono **attributi di $R$** 
	- in pratica *on $R$ group by $A_{i}$*  
- $f_{i}$ sono **espressioni** che usano **funzioni di aggregazione** ($\min, \max, \text{count}, \text{sum},\ldots$)

Il valore del raggruppamento è una relazione in cui:
1. Si **partizionano** le ennuple di $R$ mettendo nello stesso **gruppo** tutte le ennuple con **valori uguali** degli $A_{i}$
2. Si calcolano le espressioni $f_{i}$ **per ogni gruppo**
3. **Per ogni gruppo** si restituisce **una sola ennupla** con attributi: i valori degli $A_{i}$ e delle espressioni $f_{i}$

![[Pasted image 20230821160334.png]]

## Groupby su [[chiave#Chiave Primaria|chiave primaria]]
  Quando viene eseguita una groupby su una [[chiave#Chiave Primaria|chiave primaria]] di una tabella, 
  - otterrai un risultato in cui ogni gruppo conterrà una sola ennupla. 
	  - perché le chiavi primarie sono uniche per ogni riga nella tabella

In pratica, otterrai lo stesso risultato della tabella originale.

## Esempi
### Esempio semplice
Data la relazione:
- Esami(Materia: str, Candidato: int, Voto: int, Docente: int)

![[Pasted image 20230821162723.png]]

Per ogni candidato voglio:
- numero di esami
- voto minimo
- voto massimo
- voto medio

1. Eseguo una `groupby` sull'attributo $Candidato$ e passo come funzioni di aggregazione $count(*),\min(Voto),\max(Voto),\text{avg}(Voto)$ $${}_{\{Candidato\}} \gamma_{\{count(*),\min(Voto),\max(Voto),\text{avg}(Voto)\}}(Esami)$$
![[Pasted image 20230821162837.png]]
1. Viene restituita una nuova nelazione che contiene tante ennuple quanti *$Candidati$* (attributo su cui abbiamo fatto la `groupby`) *distinti* stanno sulla relazione.
![[Pasted image 20230821163025.png]]

```ad-faq

| Materia | Candidato | Voto | Docente |
|---------|-----------|------|---------|
| MAT101  | 101       | 85   | 201     |
| FIS102  | 102       | 75   | 202     |
| CHM103  | 103       | 90   | 203     |
| BIO104  | 104       | 75   | 204     |


- Posso raggruppare per voto?
	- Si
- Cosa succede se calcolo $\min(Voto)$
	- In pratica mi restituisce una tabella con $Voto, \min(Voto)$ con valori identici per riga. 
	- Ha senso perché sto già raggruppando per voto, formo delle partizioni in base al voto e poi prendo il minimo di ognuna.

| Voto | min(voto) |
|------|-----------|
| 75   | 75        |
| 85   | 85        |
| 90   | 90        |

```

### Esempio su due attributi
Abbiamo una relazione
- Clienti(IdCliente: int, Cognome: str, Nome: str)

| IdCliente | Cognome  | Nome  |
|-----------|----------|-------|
| 872       | Rossi    | Maria |
| 541       | Bianchi  | Luigi |
| 236       | Verdi    | Maria |
| 953       | Rossi    | Luigi |
| 187       | Bianchi  | Maria |
| 682       | Verdi    | Luigi |
| 765       | Rossi    | Maria |

Una `groupby` per $Nome$ e con funzione d'aggregazione $count(*)$, otteniamo la seguente tabella

$${}_{\{Nome\}} \gamma_{\{count(*)\}}(Clienti)$$

| Nome   | COUNT(\*) |
|--------|----------|
| Maria  | 4        |
| Luigi  | 3        |

Abbiamo 4 Maria e 3 Luigi.

Una `groupby` per $Cognome$ e con funzione d'aggregazione $count(*)$, otteniamo la seguente tabella

$${}_{\{Cognome\}} \gamma_{\{count(*)\}}(Clienti)$$

| Cognome  | COUNT(\*) |
|----------|----------|
| Rossi    | 3        |
| Bianchi  | 2        |
| Verdi    | 2        |


Abbiamo 3 Rossi, 2 Bianchi e 2 Verdi.

Se invece facessimo una `groupby` per $Cognome, Nome$ e con funzione d'aggregazione $count(*)$, otteniamo la seguente tabella

$${}_{\{Cognome, Nome\}} \gamma_{\{count(*)\}}(Clienti)$$

| Cognome | Nome  | count(\*) |
| ------- | ----- | --------- |
| Bianchi | Luigi | 1         |
| Bianchi | Maria | 1         |
| Rossi   | Luigi | 1         |
| Rossi   | Maria | 2         |
| Verdi   | Luigi | 1         |
| Verdi   | Maria | 1         |

Abbiamo 2 clienti che si chiamano Rossi Maria

Invece una `groupby` per $Nome, Cognome$ e con funzione d'aggregazione $count(*)$, otteniamo la seguente tabella

$${}_{\{Nome, Cognome\}} \gamma_{\{count(*)\}}(Clienti)$$

| Nome   | Cognome  | count(\*) |
|--------|----------|----------|
| Luigi  | Bianchi  | 1        |
| Luigi  | Rossi    | 1        |
| Luigi  | Verdi    | 1        |
| Maria  | Bianchi  | 1        |
| Maria  | Rossi    | 2        |
| Maria  | Verdi    | 1        |

- La partizione finale $Cognome, Nome$ è uguale a quella $Nome, Cognome$.

### Esempio con chiave primaria
Abbiamo due relazioni
- Studenti(Nome, <u>Matricola</u>, Provincia, AnnoNascita)
- Esami(<u>Materia</u>, <u>Matricola\*</u>, Data, Voto)

Per ogni studente visualizzare
- matricola
- nome
- numero di esami sostenuti

> Studenti

| Matricola | Nome   | Provincia | AnnoNascita |
|-----------|--------|-----------|-------------|
| 101       | Alice  | MI        | 2000        |
| 102       | Bob    | TO        | 1999        |
| 103       | Carol  | NA        | 2001        |
| 104       | David  | RM        | 2000        |

> Esami

| Materia | Matricola | Data       | Voto |
| ------- | --------- | ---------- | ---- |
| MAT     | 101       | 2023-01-15 | 85   |
| FIS     | 102       | 2023-02-20 | 75   |
| CHM     | 103       | 2023-03-10 | 90   |
| MAT     | 102       | 2023-01-20 | 78   |
| FIS     | 101       | 2023-02-25 | 92   |
| BIO     | 104       | 2023-03-15 | 68   |

1. Eseguiamo una [[Join|equi-join]]  su $Matricola$, cioè $$Studenti\bowtie_{S.matricola=E.matricola} Esami$$ in questa tabella le ennuple solo state combinate in base alla corrispondenza con le matricole
![[Pasted image 20230821172012.png]]
facciamo una groupby sugli attributi $Matricola,Nome$ e applichiamo la funzione $count(*)$
$${}_{\{Matricola,Nome\}} \gamma_{\{count(*)\}}(Studenti\bowtie_{S.matricola=E.matricola} Esami)$$
![[Pasted image 20230821172230.png]]
