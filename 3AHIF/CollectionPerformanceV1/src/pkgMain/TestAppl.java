package pkgMain;

import pkgData.Database;
import pkgData.Database.COLLECTIONTYPE;

public class TestAppl {
	private static String FILENAME = "script.txt";
	private static Database db = null;
	
	public static void main(String[] args) {
		try {
			db = new Database(FILENAME);
			long duration = 0;
			duration = db.loadFileInCollection(COLLECTIONTYPE.ARRAYLIST);
			System.out.println("load/seek in arraylist: " + duration + " msec.");
			System.out.println("     size of arraylist: " + db.getSizeOfCollection(COLLECTIONTYPE.ARRAYLIST));
			duration = db.loadFileInCollection(COLLECTIONTYPE.TREEMAP);
			System.out.println("load/seek in treemap: " + duration + " msec.");
			System.out.println("     size of treemap: " + db.getSizeOfCollection(COLLECTIONTYPE.TREEMAP));
			duration = db.loadFileInCollection(COLLECTIONTYPE.HASHMAP);
			System.out.println("load/seek in hashmap: " + duration + " msec.");
			System.out.println("     size of hashmap: " + db.getSizeOfCollection(COLLECTIONTYPE.HASHMAP));
			db.printCollEntries();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
