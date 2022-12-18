Una struct è un tipo di dati personalizzato nel linguaggio C. Le struct possono contenere più membri di tipi di dati diversi in una singola unità. Gli elementi di una struct sono memorizzati in posizioni di memoria contigue e possono essere recuperati e consultati in qualsiasi momento.

Una union è un tipo di dati definito dall'utente. È come una struct, ma tutti i suoi membri iniziano in una posizione esatta della memoria. L'unione combina oggetti di tipi di dati diversi nell'esatta posizione di memoria. L'utente può definire un'union con molti membri, ma solo un membro può contenere un valore in qualsiasi momento. Lo spazio di memoria allocato per la variabile union è pari allo spazio totale richiesto dal membro di dati più grande in byte nell'union.

L'union fornisce variabili a cui si può accedere contemporaneamente in diversi modi e nell'esatta posizione di memoria. Un'union fornisce un modo efficiente di utilizzare un'unica posizione di memoria per diversi compiti.

## Similitudini
1. Entrambi sono tipi di dati definiti dall'utente, utilizzati per memorizzare dati di tipo diverso come un'unica unità.
2. I loro membri possono essere oggetti di qualsiasi tipo, comprese altre strutture e unioni o array.
3. Sia le struct che le union supportano solo gli operatori di assegnazione = e sizeof.
4. Una struct o un'union può essere passata come valore alle funzioni e restituita come valore dalle funzioni. 
5. L'operatore '.' o operatore di selezione viene utilizzato per accedere alle variabili membro all'interno di entrambi i tipi di dati definiti dall'utente.

## Caratteristiche Struct
- **Dimensione**. Quando una variabile viene associata a una struct, il compilatore alloca memoria per ogni membro. La grandezza in byte della struct è maggiore o uguale alla somma della grandezza in byte di ogni suo membro.
- **Memoria**. A ogni membro all'interno di una struct è dedicata una porzione di memoria.
- **Alterare un valore**. Alterare il valore di un membro di una struct non crea effetti collaterali agli altri membri della struct.
- **Inizializzare i membri**. Diversi membri di una struttura possono essere inizializzati una sola volta.

## Caratteristiche Union
- **Dimensione**. Quando una variabile viene associata a una union, il compilatore alloca memoria considerando la dimensione in byte del membro più grande. La grandezza in byte dell'union è maggiore o uguale della grandezza in byte del membro più grande.
- **Memoria**. La memoria allocata è condivisa dai singoli membri dell'union.
- **Alterare un valore**. Alterare il valore di un membro di una union potrebbe alterare il valore di altri membri.
- **Accedere ai membri**. Si può accedere a un solo membro alla volta
- **Inizializzare i membri**. Solo il primo membro di una union viene inizializzato.

## Osservazioni 
Le struct sono migliori delle union, poiché la memoria è condivisa in un'union, il che comporta un po' di ambiguità. Tuttavia, dal punto di vista tecnico, le union sono migliori in quanto aiutano a risparmiare molta memoria, con un vantaggio complessivo sulle struct nel lungo periodo.

Le struct vengono utilizzate quando è necessario memorizzare valori distinti per tutti i membri in un'unica posizione di memoria, mentre le union aiutano a gestire la memoria in modo efficiente.