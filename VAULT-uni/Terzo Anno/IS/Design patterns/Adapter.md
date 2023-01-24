```java
// CLIENT
ITarget target = new Adapter( new Adaptee() );
target.request();

interface ITarget {
	void request();
}

class Adapter implements ITarget {
	Adaptee adaptee;
	public Adapter(Adaptee a) {
		this.adaptee = a;
	}
	public void request() {
		 // deve delegare adaptee
		 this.adaptee.specificRequest();
	}
}

class Adaptee {
	public void specificRequest() {
		// ...
	}
}
```

# Facade
Per evitare di usare allo stesso tempo 200'000 classi a causa dell'eredità ecc... 
La facade (facciata) è un'ottima metafora, nasconde tutto ciò che costituisce una casa (fondamenta, tubi, cazzi, ecc...). 
- Principio di Least Knowledge $\texttt{a.b.}\not\texttt{c}$, posso invocare solo metodi dell'oggetto in cui sono o dell'oggetto di cui mi hanno passato il riferimento come parametro.
- Il Client di prima interagisce con una Facciata che interagisce con il casino di classi di cui abbiamo parlato prima.