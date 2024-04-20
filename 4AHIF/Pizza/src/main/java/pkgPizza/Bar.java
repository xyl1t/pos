package pkgPizza;

import java.util.Stack;

public class Bar {
    Stack<Pizza> pizzaList;
    private int capacity;

    public Bar(){
        pizzaList = new Stack<>();
        this.capacity = 3;
    }

    public void addPizza(Pizza p){
        pizzaList.push(p);
    }
    public Pizza takePizza(){
        return pizzaList.pop();
    }

    public int getCapacity() {
        return capacity;
    }
}
