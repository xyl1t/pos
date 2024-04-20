package pkgApplication;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

import pkgData.Database;
import pkgData.Team;
import pkgGUI.Gui;
import pkgMisc.AlternateComparator;
import pkgMisc.Parser;
import pkgMisc.XmlParser;

public class SoccerApplication {
	private static Database db = new Database();
	private static Gui gui = new Gui();
	private static Parser parser = new Parser();
	private static AlternateComparator ac = new AlternateComparator();
	private static boolean dbHasData = false;
	private static final String FILENAME = "teams";

	public static void main(String[] args) {
		boolean alive = true;

		// loop as long as the user hasn't exited
		while (alive) {
			gui.printMenu();
			String userInput = gui.getInput();
			String param = "";

			// handle input and check for validity
			String cmd = "";
			try {
				cmd = parser.getCommand(userInput);
			} catch(Exception e) {
				System.out.println("Error: " + e.getMessage());
			}

			try {
				// handle valid commands
				switch(cmd) {
					case "load": 
						readingTextfile(parser.getParameterOfCommand(userInput));
						System.out.println("File has been successfully loaded!");
						dbHasData = true;
						break;

					case "add":
						param = parser.getParameterOfCommand(userInput);
						addTeam(param);
						dbHasData = true;
						break;

					case "list": 
						param = parser.getParameterOfCommand(userInput);
						printTeams(param);
						break;

					case "store":
						param = parser.getParameterOfCommand(userInput);
						store(param);
						if (userInput.equals("-xml")) {
							System.out.println("Database has been stored successfully to teams.xml");
						} else {
							System.out.println("Database has been stored successfully to teams.html");
						}
						break;

					case "read":
						readDBFromXML();
						System.out.println("Database has been populated with teams from the xml file!");
						break;

					case "quit": 
						alive = false;
						break;
				}
			} catch(Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
			
			System.out.println();
		}
	}

	private static void addTeam(String data) throws IllegalArgumentException {
		db.addResult(data);
	}

	public static void printTeams(String orderby) { 
		if (dbHasData) {
			if (orderby.equals("-name")) {
				db.defaultSort();
				System.out.println("-------------- order by name --------------");
			} else if (orderby.equals("-points")) {
				db.alternateSort(ac);
				System.out.println("------------- order by points -------------");
			} else {
				throw new IllegalArgumentException("'orderby' can only be '-name' or '-points'");
			}
			
			for (Team t : db.getTeams()) {
				System.out.println(t.toString());
			}
		} else {
			throw new IllegalArgumentException("no data to list");
		}
	}

	public static void readingTextfile(String filename) throws IOException { 
		XmlParser xmlParser = new XmlParser();
		xmlParser.Parse(filename);
		db.clear(); // clear the content of the database if it has some old data
		for (XmlParser.Node node : xmlParser.getRoot().getChildNodes()) {
			db.addResult(node.getText());
		}
	}

	private static void store(String param) throws IOException {
		if (dbHasData) {
			if (param.equals("-xml")) {
				storeDBToXML();
			} else if (param.equals("-html")) {
				generateHTML();
			}
		} else {
			throw new IllegalArgumentException("no data to store");
		}
	}

	private static void generateHTML() throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILENAME + ".html")));
		Team[] teams = db.getTeams();

		String cssStyle = 
			  "<style>\n"
			+ "  h1, h2, h3, h4, h5 {\n"
			+ "  color: teal;\n"
			+ "  background-color: lime;\n"
			+ "  letter-spacing: 0.25em;\n"
			+ "  border-bottom: 1px solid black;\n"
			+ "}	\n"
			+ "table {\n"
			+ "  border: 3px dashed black;\n"
			+ "}\n"
			+ "th {\n"
			+ "  border: 2px dotted red;\n"
			+ "  background-color: #DBFFFF;\n"
			+ "}\n"
			+ "td {\n"
			+ "  border: 1px dotted red;\n"
			+ "  background-color: #FBFFCC;\n"
			+ "}\n"
			+ "td, th {\n"
			+ "  padding: 2em;\n"
			+ "}\n"
			+ "</style>\n\n";
		out.write(cssStyle);

		out.write("<h1>List of Teams</h1>\n\n");

		out.write("<table border=\"1\">\n");
		out.write("  <tr>\n");
		out.write("    <th>team</th>\n");
		out.write("    <th>points</th>\n");
		out.write("    <th>goals shot</th>\n");
		out.write("    <th>goals got</th>\n");
		out.write("  </tr>\n");

		for (Team t : teams) {
			out.write("  <tr>\n");
			out.write("    <td>" + t.getName() + "</td>\n");
			out.write("    <td>" + Integer.toString(t.getPoints()) + "</td>\n");
			out.write("    <td>" + Integer.toString(t.getGoalsShot()) + "</td>\n");
			out.write("    <td>" + Integer.toString(t.getGoalsGot()) + "</td>\n");
			out.write("  </tr>\n");
		}
		out.write("</table>\n");

		out.close();

		Runtime.getRuntime().exec("firefox " + FILENAME + ".html");
	}

	private static void storeDBToXML() throws IOException {
		FileOutputStream fos = new FileOutputStream(FILENAME + ".xml");
		XMLEncoder encoder = new XMLEncoder(fos);

		encoder.writeObject(db);

		encoder.close();
		fos.close();
	}

	private static void readDBFromXML() throws IOException {
		FileInputStream fis = new FileInputStream(FILENAME + ".xml");
		XMLDecoder decoder = new XMLDecoder(fis);

		db = (Database)decoder.readObject();

		decoder.close();
		fis.close();

		dbHasData = true;
	}

}
