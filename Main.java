import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	/*
	public static Interface IO = new Interface();
	public static boolean[][] Permissions = IO.permissions;
	*/
	
	public static void main(String[] args) {
		
		//init variables
		ArrayList<String> machineList = Interface.initMachineNameList();
		int ID;
		String name;
		int selection = -1;
		Boolean permissionValue = null;
		String selectedMachineName = "";
		Boolean keepGoing = true;
		String UI;
		Boolean exit = false;
		Boolean cancel = false;
		String IDStr;
		//Init today's output file
		Interface.createOutputFile();
		
		//cycle through processes until told to end
		while(keepGoing) {
			//get user ID and relevan user information
			IDStr = getUserInput("Scan your School ID Card or input its number");
			ID = Interface.getIntFromString(IDStr);
			if (IDStr.contentEquals("ENDPROGRAM")) {
				break;
			}
			name = Interface.getName(ID);
			
			
			if(!name.contentEquals("nameNotFound")) {
				
				//get usre machine number input
				System.out.println("Hello " + name + ", please type the code of the machine you would like to use");
				System.out.println("The list of available machines and their codes are:");
				int i = 0;
				for(i = 0; i<machineList.size(); i++) {
					System.out.print(machineList.get(i));
					System.out.println(": " + (i+1));
				}
				System.out.println("Or type 'Cancel' to reset back to quit");
				while(true) {
					UI = getUserInput("");
					selection = Interface.getIntFromString(UI)-1;
					
					//get permission value
					if(selection <= i-1 && selection >= 0) {
						permissionValue = Interface.getValue(ID, selection+3);
						selectedMachineName = machineList.get(selection);
						break;
					} else if(UI.contentEquals("Cancel")||UI.contentEquals("cancel")) {
						cancel = true;
						permissionValue = false;
						break;
					} else {
						System.out.println("The value you have entered is not a valid number, please input a number corresponding to a machine or the word 'cancel'");
					}
				}
				
				//output yes/no/ERROR and add to log
				if(permissionValue && !cancel) {
					System.out.println("You have permission to use the " + selectedMachineName + "!");
					Interface.outputToLog(ID, selection, true, "");
				} else if(permissionValue != null && !cancel){
					System.out.println("Unfortunately, you do not have permission to use the " + selectedMachineName);
					Interface.outputToLog(ID, selection, false, "");
				} else if(!cancel) {
					System.out.println("Error");
					continue;
				}
				
				//reset variables
				cancel = false;
				permissionValue = null;
				selection = -1;
				selectedMachineName = "";
			} else {
				System.out.println("The ID you have entered does not seem to exist");
				System.out.println("If you believe that this is an issue, please contact a group lead or mentor");
			}
		}
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