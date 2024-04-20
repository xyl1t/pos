package pkgBank;

import java.math.BigDecimal;

public class Validation {
	public static final BigDecimal LOWER_BOUND = BigDecimal.ZERO;
	public static final BigDecimal UPPER_BOUND = BigDecimal.valueOf(500);

	public boolean isAmountValid(BigDecimal amount) {
		boolean retValue = false;

		if ((amount.compareTo(LOWER_BOUND) > 0) &&
			(amount.compareTo(UPPER_BOUND) <= 0) &&
			(amount.scale() <= 2)) { // prüfe ob es <= 2 Nachkommerstellen gibt
			retValue = true;
		}
		
		return retValue;
	}
	
	/**
	 * 
	 * @param tan ... given by user-input
	 * @return    ... true if exact 5 digits and 1st position no "0"
	 */
	public boolean isTANValid(String tan) {
		return tan.matches("^[1-9]\\d{4}$");
	}
	
	/**
	 * 
	 * @param password ... at least 5 chars, 1 upper, 1 lower, 1 digit; no other chars
	 * @return
	 */
	public boolean isPasswordValid(String password) {

		if (password.matches("^[a-zA-Z\\d]{5,}$")) {
			boolean lower = (password.matches(".*[a-z].*"));
			boolean upper = (password.matches(".*[A-Z].*"));
			boolean digit = (password.matches(".*\\d.*"));
			
			return lower && upper && digit;
		}
		
		return false;
	}
}
