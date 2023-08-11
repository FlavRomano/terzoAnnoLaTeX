## Definizione base di dati

```sql
CREATE DATABASE EsempioEsami
```

## Definizione schema

```sql
CREATE TABLE 
	Esami (Materia char(5), Candidato char(8), Voto int, Lode char(1), Data char(6))
```

## Inserzione di dati

```sql
INSERT INTO Esami 
	VALUES ('BDSI1','080709', 30, 'S', 070900)
```

## Interrogazione

```sql
SELECT Candidato
FROM Esami
WHERE Materia = "BDSI1" AND Voto = 30
--Output: Candidato "080709"
```

