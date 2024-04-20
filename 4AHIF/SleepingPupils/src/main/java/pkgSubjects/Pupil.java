package pkgSubjects;

public class Pupil extends Thread {
    private String name;
    private long timeToSleep;
    private boolean isRuning = true;

    public Pupil(String name, long time) {
        this.name = name;
        this.timeToSleep = time;
    }

    @Override
    public void run() {
        try {
            while (isRuning) {
                System.out.println(name + " gonna sleeping");
                sleep(timeToSleep);
                System.out.println(name + " end of sleeping");
            }
            System.out.println(name + " end of pupil *********");
        } catch (Exception e) {
            System.out.println(name + " error: " + e.getMessage());
        }
    }

    public void setPupilsName(String name) {
        this.name = name;
    }

    public void end() {
        isRuning = false;
    }
}
