Un Decorator **instanzia** un oggetto a partire da una classe astratta di cui cui **ha** una rappresentazione.
e.g Una bevanda e un AddonDecorator, AddonDecorator estende la bevanda ed ha una bevanda.
- Si comporta come una bevanda.
- **Wrappa** una bevanda.

Decora un oggetto layer by layer mantenendo uno stato aggregato tra i decorator.
oss. Un decorator serve per dare additional responsability a un componente concreto a run-time.

```java
abstract class Beverage {
	public abstract int cost();
}

abstract class AddonDecorator extends Beverage {
	public abstract int cost();
}

public class Caramel extends AddonDecorator {
	Bevarage bevarage;
	// La classe Caramel ha una bevanda!
	public Caramel(Bevarage b) {
		this.bevarage = b;
	}
	public int cost() { return this.beverage.cost() + 2; }
}

public class Espresso extends Beverage {
	public int cost() { return 1; }
}
```