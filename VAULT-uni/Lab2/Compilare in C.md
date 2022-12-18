I programmi C tipicamente iniziano con una serie di `#include` e `#define`. Al fine di includere le informazioni presenti negli headers e applicare le sostituzioni definite dalle macro, la compilazione di un programma C avviene in **quattro passaggi**.
## 1. Preprocessor
Vengono valutate le linee che iniziano con il simbolo #, il preprocessore cerca nella directory standard delle librerie (su Linux in /usr/include) il file specificato e lo include nel programma. Quindi sostituisce nel testo del programma eventuali macro definite con `#define`.

## 2. Compiler
Il codice amplicato dal preprocessore viene passato al compilatore. Il compilatore converte il codice preprocessato in codice macchina (assembly).

## 3. Assembler
Il codice macchina viene convertito in codice oggetto dall'assembler. Il nome del file oggetto generato dall'assembler è lo stesso del file sorgente `.c`. e.g `hello.c` genera l'oggetto `hello.o`.

## 4. Linker
Tutti i programmi scritti in C utilizzano funzioni di libreria. Il lavoro principale del linker consiste nel combinare il codice oggetto dei file di libreria con il codice oggetto del nostro programma. L'output del linker è il file eseguibile.

e.g
```
gcc -Wall -g sorgente.c
# crea l'eseguibile 'a.out'
```
```
gcc -Wall -g sorgente.c -o compilato
# crea l'esegubile 'compilato'
```
