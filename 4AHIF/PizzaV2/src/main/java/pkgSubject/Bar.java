package pkgSubject;

import java.util.Stack;

public class Bar {
    Stack<Pizza> pizzaList;
    Stack<Order> orderList;
    private int capacity;

    public Bar(int capacity){
        pizzaList = new Stack<>();
        orderList = new Stack<>();
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void addPizza(Pizza p){
        pizzaList.push(p);
    }
    public Pizza takePizza(){
        return pizzaList.pop();
    }

    public Order createOrder(Customer customer) {
        Order o = new Order(customer, null);
        orderList.push(o);
        return o;
    }
    public Order takeOrder(Cook cook) {
        return orderList.pop();
    }
}
