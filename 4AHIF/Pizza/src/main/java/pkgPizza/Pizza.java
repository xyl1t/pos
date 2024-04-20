package pkgPizza;

public class Pizza {

    private String name;
    private static int numberOfPizza = 0;

    private int pizzaId = 0;

    public Pizza(String name){
        this.name = name;

        this.pizzaId = numberOfPizza;
        this.numberOfPizza++;
    }

    @Override
    public String toString() {
        return name + pizzaId;
    }
}
