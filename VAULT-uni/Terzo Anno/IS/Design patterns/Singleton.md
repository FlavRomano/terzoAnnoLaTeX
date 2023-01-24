Si assicura che una classe abbia una sola istanza e fornisca un metodo di accesso globable (a quella istanza). 
- Non si istanza due volte la stessa classe.
- Il costruttore è privato $\implies$ come si crea il primo singleton? Con un metodo statico che ritorna un'istanza della classe.
- La classe ha un attributo statico che contiene l'istanza singleton, se non è ancora stato istanziato richiama il costruttore altrimenti abbiamo già l'unica possibile istanza.

```java
class Singleton {
	static private Singleton instance;
	private Singleton() {
		// ...
	}
	public static Singleton getInstance() {
		if (instance == null)
			instance = new Singleton();
		return instance;
	}
	// ...
}
```