package pkgMain;

import java.util.HashSet;

import pkgData.Tool;

public class MyApp {
	static HashSet<Tool> collTools = new HashSet<>();
	
	public static void main(String[] args) {
		generateTools();
		searchInTools();
		printTools();
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
	
	private static void searchInTools() {
		System.out.println("============== contains =============");
		System.out.println(".." + collTools.contains(new Tool("KnifeX", 12)));
		System.out.println(".." + collTools.contains(new Tool("Knife", 12)));
		System.out.println(".." + collTools.contains(new Tool("Knife", 120)));
		System.out.println("=====================================");
		
		// reference of item in collection
	}
	
	private static void printTools() {
		System.out.println("------------ print tools ------------");
		for (Tool t : collTools) {
			System.out.println("--" + t);
		}
		System.out.println("-------------------------------------");
	}
}
