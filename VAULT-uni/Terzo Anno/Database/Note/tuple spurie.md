Una **tupla spuria** si riferisce a una riga o una tupla risultante da **un'operazione di [[JOIN SQL|JOIN]] tra più tabelle**, che appare **erroneamente duplicata** più volte nel risultato del JOIN
- si verifica quando le relazioni tra le tabelle coinvolte non sono state gestite adeguatamente.

Cerchiamo di progettare schemi di relazione in modo tale che 
- quando esegui un JOIN tra tabelle utilizzando [[chiave#Chiave Primaria|chiavi primarie]] e [[chiave#Chiave esterna|chiavi esterne]], eviti l'insorgenza di tuple spurie
- per garantire che le tabelle siano correlate in modo accurato e che le operazioni di JOIN siano condotte in modo appropriato, evitando duplicati non intenzionali nei risultati.