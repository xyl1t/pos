package pkgMain;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.TreeSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
			// doReflection();
			generateTools();
			storeTools();
			loadTools();
			// printTools();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void doReflection() {
		Class<Tool> myClass = Tool.class;
		for (int i = 0; i < myClass.getDeclaredFields().length; i++) {
			System.out.println("..." + myClass.getDeclaredFields()[i]);
		}
		for (int i = 0; i < myClass.getDeclaredMethods().length; i++) {
			System.out.println("..." + myClass.getDeclaredMethods()[i]);
		}
	}

	private static void storeTools() throws IOException {
		FileWriter fw = new FileWriter("tools.json");
		Gson gson = new Gson();

		gson.toJson(collTools, fw);

		fw.close();
	}

	private static void loadTools() throws IOException  {
		Gson gson = new Gson();
		FileReader fr = new FileReader("tools.json");

		Type collType = new TypeToken<TreeSet<Tool>>(){}.getType();
		TreeSet<Tool> mytools = gson.fromJson(fr, collType);
		for (Tool tool : mytools) {
			System.out.println(" ====== " + tool);
		}

		fr.close();
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
