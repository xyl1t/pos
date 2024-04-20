package pkgMain;

import java.util.Iterator;
import java.util.TreeMap;
import pkgData.Tool;

public class MyApp {
	// Der Key muss Compareable implementieren!!!
	static TreeMap<String, Tool> collTools = new TreeMap<>();
	
	public static void main(String[] args) {
		generateTools();
		printTools();
		searchInTools();
	}
	
	private static void generateTools() {
		Tool t1 = new Tool("Knife", 12);
		Tool t2 = new Tool("Fork", 5);
		Tool t3 = new Tool("Scissors", 77);
		Tool t4 = new Tool("Light", 8);
		Tool t5 = new Tool("Knife", 11);
		
		System.out.println("put t1..." + collTools.put(t1.getName(), t1));
		collTools.put(t2.getName(), t2);
		collTools.put(t3.getName(), t3);
		collTools.put(t4.getName(), t4);
		System.out.println("put t5..." + collTools.putIfAbsent(t5.getName(), t5));
	}
	
	private static void searchInTools() {
		System.out.println("=========== searchInTools() ==========");
		System.out.println("Knife..." + collTools.get("Knife"));
		System.out.println("Knif..." + collTools.get("Knif"));
		System.out.println("Knif..." + collTools.ceilingKey("Knif"));
		System.out.println("Knif..." + collTools.floorKey("Knif"));
	}
	
	private static void printTools() {
		for (Tool t : collTools.values()) {
			System.out.println("--" + t);
		}
		System.out.println("----------------");
	}
}
