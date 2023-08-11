Descrive 
- la struttura degli insiemi di dati e delle relazioni fra loro
- secondo un certo modello dei dati
- senza nessun riferimento alla loro organizzazione fisica nella memoria permanente

e.g:
```sql
Studenti(Matricola char(8), Nome char(20), login char(8), AnnoNascita int, Reddito real)
Corsi(IdeC char(8), Titolo char(20), Credito int) 
Esami(Matricola char(8), IdeC char(8), Voto int)
```

Noto anche come schema logico