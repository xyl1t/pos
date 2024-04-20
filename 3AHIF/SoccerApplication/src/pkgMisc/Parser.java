package pkgMisc;

public class Parser {
	public enum Commands {
		load,
		add,
		list,
		store,
		read,
		quit
	}
	
	public String getCommand(String userInput) throws Exception { 
		if (userInput.isEmpty()) {
			throw new Exception("no command supplied");
		} 
		String[] splittedUserInput = userInput.trim().toLowerCase().split(" "); // remove whitespace left and right and make everything lower case so that the user can type in any case he wants
		String cmd = splittedUserInput[0];
		for (Commands c : Commands.values()) {
			if (cmd.equalsIgnoreCase(c.name())) {
				return cmd;
			}
		}
		throw new Exception("command '" + cmd + "' not known");
	}
	public String getParameterOfCommand(String userInput) throws Exception { 
		String cmd = getCommand(userInput);
		String[] splittedUserInput = userInput.trim().split(" "); // remove whitespace left and right from user input
		if (splittedUserInput == null || splittedUserInput.length < 2) {
			throw new Exception("no argument supplied");
		} 
		String param = splittedUserInput[1];
		
		if (cmd.equals("list") && !param.equals("-name") && !param.equals("-points") ||
			cmd.equals("store") && !param.equals("-xml") && !param.equals("-html")) {
			throw new Exception("command " + cmd + " with argument '" + param + "' unkown");
		}
		// if (cmd.equals("add")) {
		// 	// if (param.matches("[A-z]*-[A-z]*,\\d*:\\d*")) throw new IllegalArgumentException("add command invalid");
		// }
		
		return param;
	}
}
