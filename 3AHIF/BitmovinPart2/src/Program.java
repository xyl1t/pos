public class Program {


	public static void main(String args[]) throws Exception {

		final String EXAMPLE = "PT1M1.2S";

		System.out.println(parseXSDuration(EXAMPLE));
		System.out.println(parseXSDuration("P1DT2H3M4.2S"));
		System.out.println(parseXSDuration("T1.2S"));
		//System.out.println(parseXSDuration(EXAMPLE));

	}

	public static float parseXSDuration(String duration) throws Exception {
		float seconds = 0.0f;

		if (duration.isEmpty()) throw new Exception("Duration cannot be empty");
		if (!duration.startsWith("P")) throw new Exception("Duration has to start with letter 'P'");

		String numberString = "";
		boolean hasTSeparator = false;

		for (char letter : duration.toCharArray()) {
			if (letter == 'P') { continue; }
			if (letter == 'T') { hasTSeparator = true; continue; }

			if (Character.isDigit(letter) || letter == '.') {
				numberString += letter;
				continue;
			}

			float number = Float.parseFloat(numberString);
			switch (letter) {
				case 'D':
					seconds += number * 60 * 60 * 24;
					break;
				case 'H':
					if (!hasTSeparator) throw new Exception("T separator not found");
					seconds += number * 60 * 60;
					break;
				case 'M':
					if (!hasTSeparator) throw new Exception("T separator not found");
					seconds += number * 60;
					break;
				case 'S':
					if (!hasTSeparator) throw new Exception("T separator not found");
					seconds += number;
					break;
				default:
					throw new Exception(letter + " not supported");
			}

			numberString = "";
		}

		return seconds;
	}
}
