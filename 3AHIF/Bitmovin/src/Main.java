import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

class CharacterOccurrence {
	private final char character;
	private int occurrence;

	public CharacterOccurrence(char character) {
		this.character = character;
		this.occurrence = 0;
	}

	public char getCharacter() {
		return character;
	}
	public void incrementOccurrence() {
		this.occurrence++;
	}

	public int getOccurrence() {
		return occurrence;
	}
}

public class Main {
	public static void main(String[] args) throws Exception {
		URL url = new URL("https://www.w3.org/TR/PNG/iso_8859-1.txt");
		Scanner fileData = new Scanner(url.openStream());
		FileWriter fileOnDisk = new FileWriter("file.txt");

		final int SIZE = 255;
		ArrayList<CharacterOccurrence> characters = new ArrayList<>(SIZE);
		for (int i = 0; i < SIZE; i++) {
			characters.add(new CharacterOccurrence((char)i));
		}

		for (int lineNr = 0; fileData.hasNext(); lineNr++) {
			String line = fileData.nextLine();
			fileOnDisk.write("Line" + String.format("%03d", lineNr) + ": " + line + "\n");

			for (char ch : line.toCharArray()) {
				characters.get(ch).incrementOccurrence();
			}
		}

		fileData.close();
		fileOnDisk.close();

		characters.sort((o1, o2) -> { return o1.getOccurrence() < o2.getOccurrence() ? 1 : -1; });

		for (CharacterOccurrence oc : characters) {
			if (oc.getOccurrence() > 0) {
				System.out.printf("Character: '%c', occurrence: %d\n", oc.getCharacter(), oc.getOccurrence());
			}
		}
	}
}
