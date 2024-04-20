package pkgSubject;

public class Pizza {
    private Cook cook;
    private int pizzaId = 0;

    private static int numberOfPizza = 0;

    public Pizza(Cook cook){
        this.cook = cook;
        this.pizzaId = numberOfPizza++;
    }

    public Cook getCook() {
        return cook;
    }

    @Override
    public String toString() {
        return "made by " + cook.getCookName() + " -" + pizzaId + "-";
    }
}
