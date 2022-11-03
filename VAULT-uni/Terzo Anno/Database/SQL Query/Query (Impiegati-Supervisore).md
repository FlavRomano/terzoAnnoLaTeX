```start-multi-column
ID: ID_rcbs
Number of Columns: 2
Largest Column: standard
```

Impiegati(==Matricola==, Nome, Età, Stipendio)

| ==Matricola== |        Nome | Eta | Stipendio |
|-----------|-------------|-----|-----------|
|      1002 |  Gilbertina |  50 |        58 |
|      1003 |     Nicolea |  28 |        79 |
|      1004 |     Whitman |  41 |        36 |
|      1005 | Clerkclaude |  65 |        69 |
|      1006 |        Tybi |  19 |        84 |
|      1007 |        Ethe |  44 |        68 |
|      1008 |    Hortense |  35 |        43 |
|      1009 |     Cherise |  35 |        75 |
|      1010 |      Thayne |  44 |        67 |
|      1011 |     Emelyne |  33 |        33 |
|      1012 |      Donalt |  57 |        44 |
|      1013 |       Vevay |  54 |        83 |
|      1014 |        Milt |  44 |        54 |
|      1015 |        Doll |  62 |        41 |
|      1016 |       Suzie |  28 |        55 |
|      1017 |     Carlynn |  36 |        44 |
|      1018 |     Guthrie |  26 |        77 |
|      1019 |    Davidson |  36 |        40 |
|      1020 |      Alicea |  29 |        43 |
|      1021 |      Karine |  50 |        37 |
|      1022 |      Eimile |  48 |        50 |
|      1023 |      Amandy |  20 |        70 |
|      1024 |        Inge |  28 |        72 |
|      1025 |       Emmet |  43 |        49 |
|      1026 |      Adrian |  61 |        73 |
|      1027 |    Gretchen |  57 |        37 |
|      1028 |   Marmaduke |  40 |        41 |
|      1029 |   Philomena |  63 |        30 |
|      1030 |       Kylie |  38 |        55 |
|      1031 |     Ruthann |  26 |        78 |
|      1032 |      Blinny |  48 |        86 |
|      1033 |      Thaine |  25 |        55 |
|      1034 |    Veronica |  59 |        42 |
|      1035 |      Arlina |  47 |        73 |
|      1036 |        Jojo |  51 |        74 |
|      1037 |       Levon |  20 |        80 |
|      1038 |   Catharine |  46 |        74 |
|      1039 |       Meggy |  50 |        72 |
|      1040 |        Dirk |  34 |        90 |
|      1041 |      Tallie |  49 |        90 |
--- column-end ---

Supervisione(==Impiegato==\*, ==Capo==\*)

| ==Impiegato==\* | ==Capo==\* |
|-----------|------|
|      1002 | 1022 |
|      1003 | 1023 |
|      1004 | 1024 |
|      1005 | 1025 |
|      1006 | 1026 |
|      1007 | 1027 |
|      1008 | 1028 |
|      1009 | 1029 |
|      1010 | 1030 |
|      1011 | 1031 |
|      1012 | 1032 |
|      1013 | 1033 |
|      1014 | 1034 |
|      1015 | 1035 |
|      1016 | 1036 |
|      1017 | 1037 |
|      1018 | 1038 |
|      1019 | 1039 |
|      1020 | 1040 |
|      1021 | 1041 |
=== end-multi-column





1. Trovare nome e stipendio dei capi degli impiegati che guadagnano più di 40 mila euro.
**Query 1**:
```sql
    SELECT I.Nome, I.Stipendio
    FROM (SELECT *
		  FROM Supervisione S, Impiegati I
	      WHERE I.Matricola = S.Impiegato and I.Stipendio > 40) as T, Impiegati I
    WHERE I.Matricola = T.capo;
```

**[Results][2]**:

|      Nome | Stipendio |
|-----------|-----------|
|    Eimile |        50 |
|    Amandy |        70 |
|     Emmet |        49 |
|    Adrian |        73 |
|  Gretchen |        37 |
| Marmaduke |        41 |
| Philomena |        30 |
|     Kylie |        55 |
|    Blinny |        86 |
|    Thaine |        55 |
|  Veronica |        42 |
|    Arlina |        73 |
|      Jojo |        74 |
|     Levon |        80 |
| Catharine |        74 |
|      Dirk |        90 |

  [1]: http://sqlfiddle.com/#!9/6c0e85b/50
  [2]: http://sqlfiddle.com/#!9/6c0e85b/50/0
  
2. Trovare le matricole dei capi i cui impiegati guadagnano tutti più di 40 mila euro
**Query 2**:
```sql
    SELECT I.Matricola
    FROM (SELECT *
		  FROM Supervisione S, Impiegati I
	      WHERE I.Matricola = S.Impiegato and I.Stipendio > 40) as T, Impiegati I
    WHERE I.Matricola = T.capo;
```

**[Results][2]**:

| Matricola |
|-----------|
|      1022 |
|      1023 |
|      1025 |
|      1026 |
|      1027 |
|      1028 |
|      1029 |
|      1030 |
|      1032 |
|      1033 |
|      1034 |
|      1035 |
|      1036 |
|      1037 |
|      1038 |
|      1040 |

  [1]: http://sqlfiddle.com/#!9/6c0e85b/53
  [2]: http://sqlfiddle.com/#!9/6c0e85b/53/0

3. Trovare gli impiegati che guadagnano più del proprio capo, mostrando matricola, nome e stipendio dell'impiegato e del capo. 

**Query 3**:
```sql
SELECT Sottoposti.Matricola, Sottoposti.Nome, Sottoposti.Stipendio, Capi.*
FROM (SELECT Matricola, Nome, Stipendio 
      FROM Supervisione S, Impiegati I
      WHERE I.Matricola = S.Capo) as Capi, 
      (SELECT Matricola, Nome, Stipendio, Capo
       FROM Supervisione S, Impiegati I
       WHERE I.Matricola = S.Impiegato) as Sottoposti
WHERE Sottoposti.Capo = Capi.Matricola 
      and Sottoposti.Stipendio > Capi.Stipendio;
```

**[Results](http://sqlfiddle.com/#!9/6c0e85b/88/0)**:

| Matricola | Nome        | Stipendio | Matricola | Nome      | Stipendio |
| --------- | ----------- | --------- | --------- | --------- | --------- |
| 1002      | Gilbertina  | 58        | 1022      | Eimile    | 50        |
| 1003      | Nicolea     | 79        | 1023      | Amandy    | 70        |
| 1005      | Clerkclaude | 69        | 1025      | Emmet     | 49        |
| 1006      | Tybi        | 84        | 1026      | Adrian    | 73        |
| 1007      | Ethe        | 68        | 1027      | Gretchen  | 37        |
| 1008      | Hortense    | 43        | 1028      | Marmaduke | 41        |
| 1009      | Cherise     | 75        | 1029      | Philomena | 30        |
| 1010      | Thayne      | 67        | 1030      | Kylie     | 55        |
| 1013      | Vevay       | 83        | 1033      | Thaine    | 55        |
| 1014      | Milt        | 54        | 1034      | Veronica  | 42        |
| 1018      | Guthrie     | 77        | 1038      | Catharine | 74        |

4. Trovare quali sono gli impiegati che hanno stipendio massimo

**Query 4**:
```sql
SELECT *
FROM Impiegati
WHERE Impiegati.Stipendio in (SELECT MAX(Stipendio) FROM Impiegati);
```

** [Results](http://sqlfiddle.com/#!9/5bd169/1/0)**:

| Matricola |   Nome | Eta | Stipendio |
|-----------|--------|-----|-----------|
|      1040 |   Dirk |  34 |        90 |
|      1041 | Tallie |  49 |        90 |
