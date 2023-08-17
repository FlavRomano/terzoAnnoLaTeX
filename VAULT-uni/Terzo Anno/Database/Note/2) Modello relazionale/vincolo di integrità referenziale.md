Un **vincolo d'integrità referenziale** fra gli attributi $X$ di una relazione $R_{1}$ e un'altra relazione $R_{2}$
- impone ai valori su $X$ di $R_{1}$ di comparire come valori della [[chiave#Chiave Primaria|chiave primaria]] di $R_{2}$

```ad-example
Vincoli di integrità referenziale fra

![[Pasted image 20230817163112.png]]
l'attributo `Vigile` della [[relazione]] `Infrazioni` e la [[relazione]] `Vigili`

![[Pasted image 20230817162859.png]]
gli attributi `Stato` e `Numero` di `Infrazioni` e la [[relazione]] `Auto`
```