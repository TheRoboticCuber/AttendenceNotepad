import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

public class Interface {
//	public static boolean[][] permissions;
	public static String inputFileName = "memberList.csv";
	public static String outputFileName = "outputLog.csv";
	public static String machineListFileName = "machineList.csv";
	public static String outputLogDir = "";
	public static String outputTableDir = "";
    public static BufferedReader bufferedReader;
    public static BufferedWriter bufferedWriter;
    public static Dictionary<Integer, String[]> people = initPeopleList();
    public static int[] IDList = initIDList(people);
    
    //Finds a certain yes/no value for a given ID and machine
    public static boolean getValue(int ID, int dataPoint) {
    	//init vars
    	String line;
    	String value;
    	
    	try {
    		//init reader
        	bufferedReader = new BufferedReader(new FileReader(inputFileName));
        	
        	//loop through all lines of the notepad one by one
          	while((line = bufferedReader.readLine()) != null) {
          		
          		//check if the ID matches the one inputted into the function
          		if(getValueFromString(line, 0).contentEquals(ID+"")) {
          			
          			//get the wanted datapoint from the line with the wanted ID
          			value = getValueFromString(line, dataPoint);
          			//return the boolean value corresponding to the Y/N value
          			if(value.contentEquals("Y")) {									
          				return true;
          			} else if(value.contentEquals("N")) {
          				return false;
          			}
          		}
        	}
        }
    	//exception handling
    	catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + inputFileName + "'");
        } catch(IOException ex) {
            System.out.println("Error reading file '" + inputFileName + "'");
        }

        try {
			bufferedReader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
    	//so function doesn't error
		return false;
    }
    
    
    
    //literally the exact same as the previous function but modified to return Name
    public static String getName(int ID) {
    	for (int IDNum : IDList) {
    		if (IDNum==ID) {
    			String[] person = (String[]) people.get(ID);
    			return person[0];
    		}
    	}
        
    	//empty return in case of error
		return "nameNotFound";
    }
    
    
    
    //function to convert string to integer (no idea how this works)
	public static int getIntFromString(String input) {
        Pattern regex = Pattern.compile("\\D*(\\d*)");
        Matcher matcher = regex.matcher(input);
        if(matcher.matches() && matcher.groupCount() == 1 && input.matches("-?\\d+(\\.\\d+)?")) {
            String digitStr = matcher.group(1);
            return Integer.parseInt(digitStr);
        }
		return -1;
	}
	
	
	
	//find one data value from a comma seperated list
	public static String getValueFromString(String line, int dataPoint) {
		
    	//init variables
    	int lastKnownBreak = 0;				//horizontal value for start of wanted datapoint
    	int evenLasterKnownBreak = 0;		//horizontal value for end of wanted datapoint
    	int dataPointIterator = 0;
		
    	
  		//keep looking for commas until reaching the wanted datapoint and save beginning and end points
  		while(dataPointIterator<dataPoint+1) {
    		evenLasterKnownBreak = lastKnownBreak;
    		lastKnownBreak = line.indexOf(",", lastKnownBreak+1);
    		dataPointIterator++;
    		if(lastKnownBreak==-1) {
    			return line.substring(evenLasterKnownBreak+1, line.length());
    		}
  		}
  		if(dataPoint==0) {
  	  		return line.substring(evenLasterKnownBreak, lastKnownBreak);
  		}
  		return line.substring(evenLasterKnownBreak+1, lastKnownBreak);
	}
	
	
	
	//initialize an array containing list of machine names from machineList notepad
	public static Dictionary<Integer, String[]> initPeopleList() {
		
		//init variables
		String line;
		Dictionary<Integer, String[]> people = new Hashtable<Integer, String[]>();
		
		try {
			//count the amount of lines in the text document
			bufferedReader = new BufferedReader(new FileReader(inputFileName));
			while((line = bufferedReader.readLine())!= null) {
				if (!getValueFromString(line, 0).contentEquals("ID")) {
					String[] person = {getValueFromString(line, 1), "F", "", ""};
					people.put(getIntFromString(getValueFromString(line, 0)), person);
				}
			}
			//return list
			return people;
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return empty string in case of error
		Dictionary<Integer, String[]> s = new Hashtable<Integer, String[]>();
		return s;
	}
	
	
	
	//initialize array containing all IDs
	public static int[] initIDList(Dictionary<Integer, String[]> dict) {
		int[] list = new int[dict.size()];
		int i = 0;
		for (Enumeration<Integer> e = dict.keys(); e.hasMoreElements();) {
	        list[i] = (int) e.nextElement();
	        i++;
	    }
		return list;
	}
	
	
	
	//creates new folders/files for today's output
	public static void initFiles() {
		
		//creates dateformat values
		Date currentDateTime = Calendar.getInstance().getTime();
		DateFormat yearFormat = new SimpleDateFormat("yyyy");
		DateFormat monthFormat = new SimpleDateFormat("MMMM");
		DateFormat dayFormat = new SimpleDateFormat("dd");
		String year = yearFormat.format(currentDateTime);
		String month = monthFormat.format(currentDateTime);
		String day = dayFormat.format(currentDateTime);
		
		//create folders/file for today's log
		outputLogDir = System.getProperty("user.dir") + "\\files\\logs\\" + year + "\\" + month + "\\" + day + ".txt";
		File file1 = new File(outputLogDir);
		outputTableDir = System.getProperty("user.dir") + "\\files\\tables\\" + year + "\\" + month + "\\" + day + ".csv";
		File file2 = new File(outputTableDir);
		File file3 = new File(System.getProperty("user.dir") + "\\files\\mostRecentLog.txt");
		File file4 = new File(System.getProperty("user.dir") + "\\files\\mostRecentTable.csv");
		file1.getParentFile().mkdirs();
		file2.getParentFile().mkdirs();
		file3.getParentFile().mkdirs();
		file4.getParentFile().mkdirs();
		try {
			file1.createNewFile();
			file2.createNewFile();
			file3.createNewFile();
			file4.createNewFile();
		}
		catch(IOException e) {e.printStackTrace();}
	}
	
	
	
	//modifies true/false signed-in value
	public static boolean signInOut(int ID) {
		//init/format current time
		Date currentDateTime = Calendar.getInstance().getTime();
		DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
		String time = timeFormat.format(currentDateTime);
		
		//modify 'person' dictionary values
    	for (int IDNum : IDList) {
    		if (IDNum==ID) {
    			String[] person = (String[]) people.get(ID);
    			if (person[1].contentEquals("T") && person[3].contentEquals("")) {
    				person[1] = "F";
    				person[3] = time;
    				outputToLog(ID, false);
    				return true;
    			}
    			else if (person[1].contentEquals("F") && person[2].contentEquals("")) {
    				person[1] = "T";
    				person[2] = time;
    				outputToLog(ID, true);
    				return true;
    			}
    			people.put(ID, person);
    		}
    	}
    	return false;
	}
	
	
	
	//checks if a person is signed in
	public static boolean isSignedIn(int ID) {
		//checks second dictionary value for true/false
    	for (int IDNum : IDList) {
    		if (IDNum==ID) {
    			String[] person = (String[]) people.get(ID);
    			if (person[1].contentEquals("T")) {
    				return true;
    			}
    			else {
    				return false;
    			}
    		}
    	}
    	return false;
	}
	
	
	
	//outputs a constant stream of log in/outs
	public static void outputToLog(int ID, Boolean IO) {
		//init reader/writer/doclog
		BufferedWriter bufferedWriter = null;
		BufferedReader bufferedReader = null;
		ArrayList<String> doc = new ArrayList<String>();
		String[] person = (String[]) people.get(ID);
		String inout = null;
		
		//init time function
		Date currentDateTime = Calendar.getInstance().getTime();
		DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
		String time = timeFormat.format(currentDateTime);
		
		try {
			//read all text within the document and add into doc
			String line = "";
			bufferedReader = new BufferedReader(new FileReader(outputLogDir));
			while((line = bufferedReader.readLine()) != null) {
				doc.add(line);
			}
			bufferedReader.close();
			
			//write from doc to output document
			bufferedWriter = new BufferedWriter(new FileWriter(outputLogDir));
			for(int i = 0; i < doc.size(); i++) {
				bufferedWriter.write(doc.get(i));
				bufferedWriter.newLine();
			}
			
			//add log statement to document
			if (IO) {
				inout = "in";
			} else if (!IO) {
				inout = "out";
			}
			bufferedWriter.write(ID + " signed " + inout + " at " + time);
			bufferedWriter.newLine();
		
		//catch/finally error handling
		} catch(IOException e){
			e.printStackTrace();
		}
		finally {
			try {
				if(bufferedWriter!=null) {
					bufferedWriter.close();
				}
			} catch(Exception ex) {
				System.out.println("Error in closing the BufferedWriter"+ex);
			}
		}
	}
	
	
	
	//outputs log in/out table when the program shuts down
	public static void outputFinal() {
		//init reader/writer/doclog
		BufferedWriter bufferedWriter = null;
		BufferedReader bufferedReader = null;
		ArrayList<String> doc = new ArrayList<String>();
		
		//init time function
		Date currentDateTime = Calendar.getInstance().getTime();
		DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
		String time = timeFormat.format(currentDateTime);
		
		//init other variables
		int ID;
		String[] person;
		
		
		//output to normal output log and copy current output log to recent output log
		try {
			//read all text within the document and add into doc
			String line = "";
			bufferedReader = new BufferedReader(new FileReader(outputLogDir));
			while((line = bufferedReader.readLine()) != null) {
				doc.add(line);
			}
			bufferedReader.close();
			//write from doc to output document
			bufferedWriter = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "\\files\\mostRecentLog.txt"));
			for(int i = 0; i < doc.size(); i++) {
				bufferedWriter.write(doc.get(i));
				bufferedWriter.newLine();
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		finally {
			try {
				if(bufferedWriter!=null) {
					bufferedWriter.close();
				}
			} catch(Exception ex) {
				System.out.println("Error in closing the BufferedWriter"+ex);
			}
		}
		
		
		//output table to normal output table
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(outputTableDir));
			bufferedWriter.write("ID,Full Name,Time Signed In,Time Signed Out,");
			bufferedWriter.newLine();
			for (Enumeration e = people.keys(); e.hasMoreElements();) {
				ID = (int) e.nextElement();
				person = (String[]) people.get(ID);
				bufferedWriter.write(ID + "," + person[0] + "," + person[2] + "," + person[3] + ",");
				bufferedWriter.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(bufferedWriter!=null) {
					bufferedWriter.close();
				}
			} catch(Exception ex) {
				System.out.println("Error in closing the BufferedWriter"+ex);
			}
		}
		
		
		//output table to recent output table
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "\\files\\mostRecentTable.csv"));
			bufferedWriter.write("ID,Full Name,Time Signed In,Time Signed Out,");
			bufferedWriter.newLine();
			for (Enumeration e = people.keys(); e.hasMoreElements();) {
				ID = (int) e.nextElement();
				person = (String[]) people.get(ID);
				bufferedWriter.write(ID + "," + person[0] + "," + person[1] + "," + person[2] + "," + person[3] + ",");
				bufferedWriter.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(bufferedWriter!=null) {
					bufferedWriter.close();
				}
			} catch(Exception ex) {
				System.out.println("Error in closing the BufferedWriter"+ex);
			}
		}
	}
	
	
	
	//outputs a log of events
/*	public static void outputToLog(int ID, int machine, Boolean wasSuccessful, String extraText) {
		//init reader/writer/doclog
		BufferedWriter bufferedWriter = null;
		BufferedReader bufferedReader = null;
		ArrayList<String> doc = new ArrayList<String>();
		ArrayList<String> machineList = initMachineNameList();
		String selectedMachineName = machineList.get(machine);
		
		//init time function
		Date currentDateTime = Calendar.getInstance().getTime();
		DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
		String time = timeFormat.format(currentDateTime);
		
		try {
			//read all text within the document and add into doc
			String line = "";
			bufferedReader = new BufferedReader(new FileReader(outputFileDir));
			while((line = bufferedReader.readLine()) != null) {
				doc.add(line);
			}
			bufferedReader.close();
			
			//write from doc to output document
			bufferedWriter = new BufferedWriter(new FileWriter(outputFileDir));
			for(int i = 0; i < doc.size(); i++) {
				bufferedWriter.write(doc.get(i));
				bufferedWriter.newLine();
			}
			
			//add log statement to document
			String printStatement = "";
			if(wasSuccessful) {
				printStatement = "User " + ID + " (" + getName(ID) + ") attempted to log in to the " + selectedMachineName + " at " + time + " and was successful.";
			} else if(!wasSuccessful) {
				printStatement = "User " + ID + " (" + getName(ID) + ") attempted to log in to the " + selectedMachineName + " at " + time + " and was not successful";
			}
			if(!extraText.equals("")) {
				printStatement += ". Oh, also: " + extraText;
			}
			bufferedWriter.write(printStatement);
			bufferedWriter.newLine();
		
		//catch/finally error handling
		} catch(IOException e){
			e.printStackTrace();
		}
		finally {
			try {
				if(bufferedWriter!=null) {
					bufferedWriter.close();
				}
			} catch(Exception ex) {
				System.out.println("Error in closing the BufferedWriter"+ex);
			}
		}
	} 
	*/
	
		
	/*	public static ArrayList<ArrayList<String>> initMachinePeopleList() {
	 *		
	 *		//init variables
	 *		String line;
	 * 		ArrayList<ArrayList<String>> emptyList = new ArrayList<ArrayList<String>>(); 
	 *		
	 *		try {
	 *			//count the amount of lines in the text document
	 *			bufferedReader = new BufferedReader(new FileReader("machineList.txt"));
	 *			while ((line = bufferedReader.readLine())!= null) {
	 * 				ArrayList<String> emptyMachineList = new ArrayList<String>(getIntFromString(getValueFromString(line, 1)));
	 *				emptyList.add(emptyMachineList);
	 *			}
	 *			
	 *				
	 *			//return list
	 * 			return emptyList;
	 * 		} catch (IOException e) {
	 *			// TODO Auto-generated catch block
	 *			e.printStackTrace();
	 *		}
	 *		//return empty string in case of error
	 *		ArrayList s = new ArrayList();
	 *		return s;
	 *	*/
} 
