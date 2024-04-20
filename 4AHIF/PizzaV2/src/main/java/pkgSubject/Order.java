package pkgSubject;

public class Order {
    private Customer customer;
    private Pizza pizza;
    private int orderId = 0;

    private static int numberOfOrders = 0;

    public Order(Customer customer, Pizza pizza){
        this.customer = customer;
        this.pizza = pizza;
        this.orderId = numberOfOrders++;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public int getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "[customer="+customer+", pizza="+(pizza != null ? pizza.toString() : "not in work yet")+"]";
    }
}
