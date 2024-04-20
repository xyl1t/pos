package pkgThread;

public class Animal extends Thread {
    protected String name;
    protected int from;
    protected int to;

    public Animal(String nameAnimal, int from, int to) {
        this.name = nameAnimal;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "nameAnimal='" + name + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }

    @Override
    public void run() {
        try {
            for (int i = from; i < to; i++) {
                int sleepTime = (int) (Math.random() * 1000 + 1000);
                System.out.println(this.getClass() + " " + name + i + " ... starts sleeping: " + sleepTime);
                Thread.sleep(sleepTime);
                System.out.println(this.getClass() + " " + name + i + " ... ends sleeping");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
