package pkgThread;

public class Person extends Thread {
    private String namePerson;
    private int from;
    private int to;

    public Person(String namePerson, int from, int to) {
        this.namePerson = namePerson;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "Person{" +
                "namePerson='" + namePerson + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }

    @Override
    public void run() {
        try {
            for (int i = to; i >= from; i--) {
                long sleepTime = (long) (Math.random() * 4000 + 3000);
                System.out.println(this.getClass() + " " + namePerson + i + " ... starts sleeping: " + sleepTime);
                Thread.sleep(sleepTime);
                System.out.println(this.getClass() + " " + namePerson + i + " ... ends sleeping");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
