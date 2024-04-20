package pkgMisc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.util.StringConverter;

public class DateFormatConverter extends StringConverter<LocalDate> {

	private static final String PATTERN = "dd.MM.yyyy";
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(PATTERN);

	@Override
	public LocalDate fromString(String strDate) {
		LocalDate retValue = null;
		
		if (strDate != null && !strDate.isEmpty()) {
			retValue = LocalDate.parse(strDate, dateFormatter);
		}
		return retValue;
	}

	@Override
	public String toString(LocalDate date) {
		String retValue = "";

		if (date != null) {
			retValue = dateFormatter.format(date);
		}

		return retValue;
	}
	
	public static String getPattern() {
		return PATTERN;
	}
}

