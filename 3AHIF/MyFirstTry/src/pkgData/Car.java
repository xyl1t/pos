package pkgData;

public class Car {
	private String name;
	private int hp;

	public Car(String name, int hp) {
		super();
		this.name = name;
		this.hp = hp;
	}
	
	@Override
	public String toString() {
		return "Car [name=" + name + ", hp=" + hp + "]";
	}
	
	
}
