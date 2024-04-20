package pkgMain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import pkgData.Tool;

public class MyApp {
	// Der Typ der für ein TreeSet muss Compareable implementieren!!!
	static TreeSet<Tool> collTools = new TreeSet<Tool>();
	static Tool t1;
	static Tool t2;
	static Tool t3;
	static Tool t4;
 	static Tool t5;
	
	public static void main(String[] args) {
		try {
			generateTools();
			storeTools();
			loadTools();
			printTools();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void storeTools() throws Exception {
		FileOutputStream fos = new FileOutputStream("tools.bin");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		oos.writeObject(t1);
		oos.writeObject(collTools);
		
		oos.close();
		fos.close();
	}
	private static void loadTools() throws Exception {
		FileInputStream fis = new FileInputStream("tools.bin");
		ObjectInputStream ois = new ObjectInputStream(fis);

		Tool tool = (Tool)ois.readObject();
		TreeSet<Tool> restoredColl = (TreeSet<Tool>) ois.readObject();
		
		System.out.println("...restored: " + tool);
		for (Tool t : restoredColl) {
			System.out.println("--" + t);
		}
		System.out.println("----------------");

		ois.close();
		fis.close();
	}

	private static void generateTools() {
		t1 = new Tool("Knife", 12);
		t2 = new Tool("Fork", 5);
		t3 = new Tool("Scissors", 77);
		t4 = new Tool("Light", 8);
		t5 = new Tool("Knife", 11);
		
		collTools.add(t1);
		collTools.add(t2);
		collTools.add(t3);
		collTools.add(t4);
		collTools.add(t5);
	}
	
	private static void printTools() {
		for (Tool t : collTools) {
			System.out.println("--" + t);
		}
		System.out.println("----------------");
	}
}
