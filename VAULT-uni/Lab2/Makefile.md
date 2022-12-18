1. Permette di esprimere dipendenze tra file. e.g `f.o` dipende dal file sorgente `f.c` e dai file header (o di intestazione) `t.h`ed `r.h`. In particolare diremo che `f.o` è detto **target** e `f.c`, `t.h` e `r.h` sono una **dependency list**.
2. Permette di esprimere cosa deve fare il sistema per aggiornare il target se uno dei file nella dependency list è stato modificato; questa regola di aggiornamento, di uno o più target, viene detta **make rule**.
3. Usando il comando `make` è possibile applicare automaticamente tutte le regole descritte nel Makefile.

## Formato della make rule
```make
target list: [dependency list]
	command 1 ... command 2
```
e.g 
```make
f.o: f.c t.h r.h
	gcc -Wall -c f.c
```

oss. All'inizio della sezione comandi deve esserci un tab.
oss. Fra due regole deve esserci una linea vuota.
oss. Il makefile deve terminare con una linea vuota.
oss. L'ordine delle regole è importante, perché `make` si costruisce l'albero delle dipendenze a partire dalla prima regola del makefile. La generazione dell'albero termina quando non ci sono più regole che hanno come target una foglia.

## Uso delle variabili
È possibile usare delle variabili per semplificare la scrittura del makefile.
```make
# nomi oggetti
OBJ = r.o f.o
# regole
exe: $(OBJ)
	gcc $(OBJ) -o exe
```

È possibile specificare anche il compilatore e i flag da utilizzare:
```make
CC = gcc
CFLAGS = -Wall
OBJ = f.o r.o

exe: f.o r.o 
	$(CC) $(CFLAGS) $(OBJ) -o exe

f.o : t.h r.h

r.o: r.h
```

## Phony targets
È possibile specificare target che non sono file e che hanno come scopo solo l'esecuzione di una sequenza di azioni.
```make
clean:
	rm $(exe) $(obj)
```

oss. `clean` è un target fittizio (phony) inserito per provocare l'esecuzione del comando in ogni caso. 

Tuttavia se casualmente nela directory viene creato un file chiamato "clean" non funzionerà più, visto che la dependency list è vuota è sempre aggiornato.

Per evitare questo comportamento è necessario dichiarare esplicitamente i target phony:

```make
.PHONY: clean
clean:
	rm $(exe) $(obj)
```