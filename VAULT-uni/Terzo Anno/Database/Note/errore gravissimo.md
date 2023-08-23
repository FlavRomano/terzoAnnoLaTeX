Non è possibile utilizzare in una stessa **select**: 
- una **proiezione** su alcuni attributi della tabella considerata 
- e **operatori aggregati** sulla stessa tabella.

le funzioni di aggregazione non possono essere usate insieme ad attributi normali, perché la target list **deve essere omogenea**.

## Scorretto
```sql
SELECT nome, max(reddito) -- di chi è il nome?
FROM persone
```

## Corretto
```sql
SELECT min(eta), avg(reddito)
FROM persone
```