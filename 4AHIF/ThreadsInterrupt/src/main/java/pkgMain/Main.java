package pkgMain;

import pkgSubjects.Pupil;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<Thread> threads = new ArrayList<>();
    private static Pupil th1 = new Pupil("Anton", 1000);
    private static Pupil th2 = new Pupil("Berta", 2000);
    private static Pupil th3 = new Pupil("Cesar", 4000);

    public static void main( String[] args ) throws InterruptedException {
        try{
            sendToBed();
            Thread.sleep(2000);
            th3.setPupilsName("Brutus");
            ringAlarmClock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sendToBed() {
        th1.start();
        th2.start();
        th3.start();
    }

    private static void ringAlarmClock() {
        th1.interrupt();
        th2.interrupt();
        th3.interrupt();
    }
}
