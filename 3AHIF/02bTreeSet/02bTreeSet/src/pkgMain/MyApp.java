package pkgMain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import pkgData.Tool;
import pkgMisc.ToolComparator;

public class MyApp {
	static TreeSet<Tool> collTools = new TreeSet<Tool>();
	
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
		
		collTools.add(t1);
		collTools.add(t2);
		collTools.add(t3);
		collTools.add(t4);
		collTools.add(t5);
	}
	
	private static void printTools() {
		// first method (worse)
		// for (Iterator<Tool> it = collTools.iterator(); it.hasNext(); ) {
		// 	Tool t = it.next();
		// 	System.out.println("--" + t);
		// }
		// System.out.println("----------------");
		
		// second method (better)
		for (Tool t : collTools) {
			System.out.println("--" + t);
		}
		System.out.println("----------------");
	}
	
	private static void searchInTools() {
		System.out.println(".." + collTools.contains(new Tool("Knife", 12)));
		
		// reference of item in collection
	}
}
