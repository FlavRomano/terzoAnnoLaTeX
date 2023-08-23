Consideriamo l'associazione `haSostenuto(Studenti, Esami)` con cardinalità $1:N$

![[Pasted image 20230817175158.png]]

In `Esami` manca l'attributo `Matricola`, aggiungiamolo allo schema di `Esami` col nome di `Candidato`

## Prima soluzione
### Schema
- Studenti(Nome: string, <u>Matricola</u>: string, Provincia: string, AnnoNascita: int)
- Esami(<u>Materia</u>: string, <u>Candidato*</u>: string, Data: string, Voto: int)

> Studenti

| Nome  | <u>Matricola</u> | Provincia | AnnoNascita |
| ----- | --------- | --------- | ----------- |
| Tazio | 614801    | SR        | 1869        |
| Lucio | 069420    | BO        | 1943        |
| Mario | 132465    | MI        | 1999        |
| Gigi  | 609811    | LI        | 2002        |

> Esami

| <u>Materia</u> | <u>Candidato*</u> | Data     | Voto |
| -------------- | ----------------- | -------- | ---- |
| BD             | 069420            | 12/12/12 | 30   |
| BD             | 132465            | 15/01/12 | 30   |
| LPP            | 069420            | 25/02/12 | 24   |

```ad-faq
- `Materia` e `Candidato` sono [[chiave#Chiave Primaria|chiave primaria]]
- `Candidato` deve essere contenuto in `Matricola`
```

## Seconda soluzione
### Schema
- Studenti(Nome: string, <u>Matricola</u>: string, Provincia: string, AnnoNascita: int)
- Esami(Numero: int, <u>Materia</u>: string, <u>Candidato*</u>: string, Data: string, Voto: int)

> Studenti

| Nome  | <u>Matricola</u> | Provincia | AnnoNascita |
| ----- | --------- | --------- | ----------- |
| Tazio | 614801    | SR        | 1869        |
| Lucio | 069420    | BO        | 1943        |
| Mario | 132465    | MI        | 1999        |
| Gigi  | 609811    | LI        | 2002        |

> Esami

| Numero | <u>Materia</u> | <u>Candidato*</u> | Data     | Voto |
| ------ | -------------- | ----------------- | -------- | ---- |
| 12     | BD             | 069420            | 12/12/12 | 30   |
| 12     | BD             | 132465            | 15/01/12 | 30   |
| 11     | LPP            | 069420            | 25/02/12 | 24   |

## Terza soluzione
### Schema
- Studenti(Nome: string, <u>Matricola</u>: string, Provincia: string, AnnoNascita: int)
- Esami(<u>Numero</u>: int, Materia: string, Data: string, Voto: int)
- StudentiEsami(<u>Esame\*</u>: int, Candidato\*: string)

In questa soluzione abbiamo che `Esame` e `Numero` corrispondono a un ID di verbalizzazione, quindi
- essendo `Esame` un id di verbalizzazione, è **[[chiave#Chiave Primaria|chiave minimale primaria]]** perché è **univoco** per ogni singolo candidato

Abbiamo rappresentato un'associazione [[rappresentazione associazione uno a molti|uno a molti]]