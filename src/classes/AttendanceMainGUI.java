package classes;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Scanner;

public class AttendanceMainGUI {
	/*
	public static Interface IO = new Interface();
	public static boolean[][] Permissions = IO.permissions;
	*/
	
	public static void main(String[] args) {
		
		Interface.initFiles();
		Boolean end = false;
		while (!end) {
			Attendance gui = new Attendance();
			gui.openGUI();
			Interface.signInOut(gui.inputID);
			end = gui.end;
			Interface.outputFinal();
		}
	}
}