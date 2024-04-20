package pkgGUI;

import java.util.Scanner;

public class Gui {
	
	private Scanner scanner = new Scanner(System.in);
	
	public void printMenu() { 
		/*
		******************************************************************
		* load <filename> ................. load teams and results       *
		* list {-name | -points} .......... list order by                *
		* quit ............................ end of app                   *
		******************************************************************
		===>
		*/
		
		String output =	"******************************************************************\n"
					  + "* load <filename> ................. load teams and results       *\n"
					  + "* add <team1-team2,goal1:goal2> ... add result                   *\n"
					  + "* list {-name | -points} .......... list order by                *\n"
					  + "* store {-xml | -html} ............ teams => xml (order by name) *\n"
					  + "* read ............................ xml => teams                 *\n"
					  + "* quit ............................ end of app                   *\n"
					  + "******************************************************************\n"
					  + "===>";
		
		System.out.print(output);
	}
	
	public String getInput() {
		return scanner.nextLine();
	}
}
