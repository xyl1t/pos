package pkgData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Database {
    public enum COLLECTIONTYPE {
        ARRAYLIST,
        TREEMAP,
        HASHMAP
    }

	private final String WORD_SEPARATOR = "[ .,\t\"!?\']";

    private ArrayList<CollEntry> alColl = new ArrayList<>();
    private TreeMap<String, CollEntry> tmColl = new TreeMap<>();
    private HashMap<String, CollEntry> hmColl = new HashMap<>();
    private String filename;

    public Database(String filename) {
        this.filename = filename;
    }

    public long loadFileInCollection(COLLECTIONTYPE colltype) throws IOException {
        long now = System.nanoTime();

		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        while (line != null) {
            String[] words = line.split(WORD_SEPARATOR);
            for (String word : words) {
                if (word.length() >= 2) {
                    if (colltype == COLLECTIONTYPE.ARRAYLIST) {
                        CollEntry ce = new CollEntry(word);
                        int idx = alColl.indexOf(ce);
                        if (idx == -1) {
                            alColl.add(ce);
                        } else {
                            alColl.get(idx).increaseCounter();
                        }
                    } else if (colltype == COLLECTIONTYPE.TREEMAP) {
                        CollEntry ce = tmColl.get(word);
                        if (ce == null) {
                            tmColl.putIfAbsent(word, new CollEntry(word));
                        } else {
                            ce.increaseCounter();
                        }
                    } else if (colltype == COLLECTIONTYPE.HASHMAP) {
                        CollEntry ce = hmColl.get(word);
                        if (ce == null) {
                            hmColl.putIfAbsent(word, new CollEntry(word));
                        } else {
                            ce.increaseCounter();
                        }
                    }
                }
            }
            line = br.readLine();
        }

        return (System.nanoTime() - now) / 1000000;
    }

    public int getSizeOfCollection(COLLECTIONTYPE colltype) throws Exception {
        int size = 0;
        if (colltype == COLLECTIONTYPE.ARRAYLIST) {
            size = alColl.size();
        } else if (colltype == COLLECTIONTYPE.TREEMAP) {
            size = tmColl.size();
        } else if (colltype == COLLECTIONTYPE.HASHMAP) {
            size = hmColl.size();
        }
        return size;
    }

    public void printCollEntries() {
        System.out.println("============================== Arraylist / TreeMap / HashMap ==============================");
        
        Object[] tmArr = tmColl.values().toArray();
        Object[] hmArr = hmColl.values().toArray();
        
        for(int i = 0; i < 10; i++) {
            System.out.print(alColl.get(i).toString() + "\t\t");
            System.out.print(((CollEntry)(tmArr[i])).toString() + "\t\t");
            System.out.println(((CollEntry)(hmArr[i])).toString());
        }
        System.out.println("===========================================================================================");
    }
}

