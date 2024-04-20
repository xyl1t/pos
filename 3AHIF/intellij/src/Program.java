import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class Occurrence {
    public int occ;
    public char c;

    public Occurrence(int occ, char c) {
        this.occ = occ;
        this.c = c;
    }
}

public class Program {
    public static void main(String[] args) {
        System.out.println("Hello Intellij");

        final int arraySize = 255;
        ArrayList<Occurrence> al = new ArrayList<>(arraySize);
        for (int i = 0; i < arraySize; i++) {
            al.add(new Occurrence(0, (char)i));
        }

        try {
            URL url = new URL("https://www.w3.org/TR/PNG/iso_8859-1.txt");
            Scanner fileFromInternet = new Scanner(url.openStream());
            FileWriter fw = new FileWriter("downloadedFile.txt");

            for (int lineNr = 0; fileFromInternet.hasNext(); lineNr++) {
                String line = fileFromInternet.nextLine();
                fw.write("Line" + String.format("%3d", lineNr) + ": " + line + "\n");

                for (char c : line.toCharArray()) {
                	al.get(c).occ++;
                }
            }

            fw.close();
            fileFromInternet.close();

            Collections.sort(al, (o1, o2) -> { return o1.occ < o2.occ ?  1 :  -1; });

            for (int ch = 0; ch < al.size(); ch++) {
                if (al.get(ch).occ > 0) {
                    System.out.printf("Occurrence: %d, character: %c\n", al.get(ch).occ, al.get(ch).c);
                }
            }

        } catch(Exception e) {
            System.out.println("URL invalid");
            e.printStackTrace();
        }
    }
}
