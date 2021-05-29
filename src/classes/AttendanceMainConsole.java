package classes;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Scanner;

public class AttendanceMainConsole {
	/*
	public static Interface IO = new Interface();
	public static boolean[][] Permissions = IO.permissions;
	*/
	
	public static void main(String[] args) {
		
		//init variables
		int ID;
		String name;
		String UI;
		String IDStr;
		Boolean isSignedIn;
		//Init today's output file
		Interface.initFiles();
		
		//cycle through processes until told to end
		while(true) {
			
			//get user ID and relevant user information
			IDStr = getUserInput("Scan your School ID Card or input its number");
			ID = Interface.getIntFromString(IDStr);
			
			//if "ENDPROGRAM" is typed, the program will end
			if (IDStr.contentEquals("END")) {break;}
			name = Interface.getName(ID);
			if(!name.contentEquals("nameNotFound")) {
				
				//display options to user
				isSignedIn = Interface.isSignedIn(ID);
				if (isSignedIn) {
					System.out.println("You are currently signed in, would you like to sign out?");
					System.out.println("Type \"Yes\" to sign in or \"No\" to cancel");
				} else if (!isSignedIn) {
					System.out.println("You are currently signed out, would you like to sign in?");
					System.out.println("Type \"Yes\" to sign out or \"No\" to cancel");
				}
				
				//receive and process user input
				while (true) {
					UI = getUserInput("");
					if (UI.contentEquals("Yes")||UI.contentEquals("yes")) {
						if(Interface.signInOut(ID)) {
							System.out.print("You have successfully been signed ");
							if(isSignedIn) {
								System.out.println("out");
							} else if(!isSignedIn) {
								System.out.println("in");
							}
						}
						break;
					} else if (UI.contentEquals("No")||UI.contentEquals("no")||UI.contentEquals("Cancel")||UI.contentEquals("cancel")) {
						break;
					}
					
					//unexpected value handling
					else {
						System.out.print("Please type either ");
						if (isSignedIn) {
							System.out.println("\"Yes\" to sign in or \"No\" to cancel");
						} else if (!isSignedIn) {
							System.out.println("\"Yes\" to sign out or \"No\" to cancel");
						}
					}
				}
			//nonexistent ID handling
			} else {
				System.out.println("The ID you have entered does not seem to exist");
				System.out.println("If you believe that this is an issue, please contact a group lead or mentor");
			}
		}
		Interface.outputFinal();
	}
	
	
	
	//displays wanted question and returns user input
	public static String getUserInput(String displayOutput) {
		if(!displayOutput.contentEquals("")) {
			System.out.println(displayOutput);
		}
		Scanner scanner = new Scanner(System.in);
		String recievedInput = scanner.next();
//		scanner.close();
		return recievedInput;
	}
}