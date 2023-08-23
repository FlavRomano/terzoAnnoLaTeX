Il prodotto cartesiano tra due relazioni $R\times S$
- è un [[Join#Join naturale|join naturale]] senza attributi in comune
	- contiene sempre un numero di ennuple **pari** al prodotto delle cardinalità degli operandi $$\# A = 4,\;\# B=6\implies\# A\times B = 24$$
- è **importante** che i **nomi dei campi** siano **distinti**

![[Pasted image 20230820120906.png]]

## Ha senso il prodotto cartesiano
È sicuramente un operatore fondamentale, tuttavia 
- concatena tuple **non necessariamente correlate** dal punto di vista **semantico**

il che lo rende poco utile da solo, molto utile invece se combinato con una [[Selezione|selezione]]
- spesso la selezione serve per mantenere solo le tuple, nel prodotto cartesiano, con valori uguali su un attributo di entrambe le relazioni.

## Prodotto cartesiano e chiavi esterne
Date le relazioni
- Studenti(Nome, <u>Matricola</u>, Provincia, AnnoNascita)
- Esami(<u>Materia</u>,<u>Matricola\*</u>, Data, Voto)

Come possiamo ottenere la tabella 
- EsamiStudenti(Matricola, Nome, Materia, Data, Voto) ?

`Esami.Matricola` è un [[vincolo di integrità referenziale]] verso `Studenti.Matricola`, quest'ultima è [[chiave#Chiave Primaria|chiave primaria]] di `Studenti`.

È ovvio che qui bisogna fare il prodotto cartesiano tra `Esami` e `Studenti` (eseguo una ridenominazione su `Studenti` perché entrambi hanno un attributo chiamato `Matricola`) $$Esami\times \rho_{S}\;Studenti$$ 
ciò detto, non tutte le righe del prodotto cartesiano mi servono.

Consideriamo le tabelle

> Studenti

| Nome          | <u>Matricola</u> | Provincia | AnnoNascita |
|---------------|-----------|-----------|-------------|
| Mario Rossi   | 1001      | Roma      | 1998        |
| Laura Bianchi | 1002      | Milano    | 1999        |
| Andrea Russo  | 1003      | Napoli    | 2000        |
| Sofia Esposito| 1004      | Torino    | 1997        |
| Luca Marino   | 1005      | Firenze   | 1998        |

> Esami

| <u>Materia</u> | <u>Matricola\*</u> | Data       | Voto |
| -------------- | ------------------ | ---------- | ---- |
| Matematica     | 1001               | 2023-05-15 | 28   |
| Storia         | 1002               | 2023-06-10 | 25   |
| Fisica         | 1003               | 2023-05-20 | 30   |
| Letteratura    | 1004               | 2023-07-02 | 27   |
| Chimica        | 1005               | 2023-06-25 | 29   |
| Informatica    | 1001               | 2023-06-18 | 26   |
| Biologia       | 1003               | 2023-07-15 | 28   |
| Geografia      | 1002               | 2023-07-05 | 23   |
| Arte           | 1004               | 2023-06-30 | 30   |
| Musica         | 1005               | 2023-07-10 | 25   |

- Per semplicità eseguiamo $Studenti \times \rho_{E}\; Esami$ solo sulle prime due ennuple di `Studenti` e le prime tre ennule di `Esami`

| Nome         | Matricola | Provincia | AnnoNascita | Materia      | E.Matricola | Data       | Voto |
|--------------|-----------|-----------|-------------|--------------|-------------|------------|------|
| Mario Rossi  | 1001      | Roma      | 1998        | Matematica   | 1001        | 2023-05-15 | 28   |
| Mario Rossi  | 1001      | Roma      | 1998        | Storia       | 1002        | 2023-06-10 | 25   |
| Mario Rossi  | 1001      | Roma      | 1998        | Fisica       | 1003        | 2023-05-20 | 30   |
| Laura Bianchi| 1002      | Milano    | 1999        | Matematica   | 1001        | 2023-05-15 | 28   |
| Laura Bianchi| 1002      | Milano    | 1999        | Storia       | 1002        | 2023-06-10 | 25   |
| Laura Bianchi| 1002      | Milano    | 1999        | Fisica       | 1003        | 2023-05-20 | 30   |

- Com'è facile osservare vi sono ennuple non utili
	- perché dovrebbe interessarmi una ennupla con nome `Mario Rossi`, matricola $1001$ ma l'esame verbalizzato è di `Laura Bianchi` con matricola $1002$?

Per eliminare queste ennuple inutili facciamo una [[Selezione|selezione]] con condizione $matricola = E.matricola$, cioè $$\sigma_{matricola=E.matricola}(Studenti \times \rho_{E}\; Esami)$$
Invece per eliminare le colonne superflue facciamo una [[Proiezione|proiezione]] sugli attributi `Matricola, Nome, Materia, Data, Voto`
$$\pi_{Matricola, Nome, Materia, Data, Voto}(\sigma_{matricola=E.matricola}(Studenti \times \rho_{E}\; Esami))$$ otteniamo quindi 

| Matricola | Nome          | Materia    | Data       | Voto |
| --------- | ------------- | ---------- | ---------- | ---- |
| 1001      | Mario Rossi   | Matematica | 2023-05-15 | 28   |
| 1002      | Laura Bianchi | Storia     | 2023-06-10 | 25   |

Mediante la rappresentazione ad albero: 

![[Pasted image 20230820163646.png]]