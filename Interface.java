import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class Interface {
//	public static boolean[][] permissions;
	public static String inputFileName = "permissions.txt";
	public static String outputFileName = "outputLog.txt";
	public static String machineListFileName = "machineList";
	public static String outputFileDir = "";
    public static BufferedReader bufferedReader;
    public static BufferedWriter bufferedWriter;
    
    
    //Finds a certain yes/no value for a given ID and machine
    public static boolean getValue(int ID, int dataPoint) {
    	//init vars
    	String line;
    	String value;
    	
    	try {
    		//init reader
        	bufferedReader = new BufferedReader(new FileReader(inputFileName));
        	
        	//loop through all lines of the notepad one by one
          	while ((line = bufferedReader.readLine()) != null) {
          		
          		//check if the ID matches the one inputted into the function
          		if (getValueFromString(line, 0).contentEquals(ID+"")) {
          			
          			//get the wanted datapoint from the line with the wanted ID
          			value = getValueFromString(line, dataPoint);
          			//return the boolean value corresponding to the Y/N value
          			if (value.contentEquals("Y")) {									
          				return true;
          			} else if (value.contentEquals("N")) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
    	//so function doesn't error
		return false;
    }
    
    
    
    //literally the exact same as the previous function but modified to return Name
    public static String getName(int ID) {
    	String line;
    	
    	try {
    		//init reader
        	bufferedReader = new BufferedReader(new FileReader(inputFileName));
        	//cycle thrugh all notepad lines until the line with correct ID is found, then send back the name on that line
          	while ((line = bufferedReader.readLine()) != null) {
          		if (getValueFromString(line, 0).contentEquals(ID+"")) {
          			return getValueFromString(line, 1);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    	//empty return in case of error
		return "nameNotFound";
    }
    
    
    
    //function to convert string to integer (no idea how this works)
	public static int getIntFromString (String input) {
        Pattern regex = Pattern.compile("\\D*(\\d*)");
        Matcher matcher = regex.matcher(input);
        
        if (matcher.matches() && matcher.groupCount() == 1) {
            String digitStr = matcher.group(1);
            return Integer.parseInt(digitStr);
        }
		return 0;
	}
	
	
	
	//find one data value from a comma seperated list
	public static String getValueFromString(String line, int dataPoint) {
		
    	//init variables
    	int lastKnownBreak = 0;				//horizontal value for start of wanted datapoint
    	int evenLasterKnownBreak = 0;		//horizontal value for end of wanted datapoint
    	int dataPointIterator = 0;
		
    	
  		//keep looking for commas until reaching the wanted datapoint and save beginning and end points
  		while (dataPointIterator<dataPoint+1) {
    		evenLasterKnownBreak = lastKnownBreak;
    		lastKnownBreak = line.indexOf(",", lastKnownBreak+1);
    		dataPointIterator++;
    		if (lastKnownBreak==-1) {
    			return line.substring(evenLasterKnownBreak+1, line.length());
    		}
  		}
  		if(dataPoint==0) {
  	  		return line.substring(evenLasterKnownBreak, lastKnownBreak);
  		}
  		return line.substring(evenLasterKnownBreak+1, lastKnownBreak);
	}
	
	
	
	//initialize an array containing list of machine names from machineList notepad
	public static ArrayList<String> initMachineNameList() {
		
		//init variables
		String line;
		ArrayList<String> machineList = new ArrayList<String>(); 
		
		try {
			//count the amount of lines in the text document
			bufferedReader = new BufferedReader(new FileReader("machineList.txt"));
			while ((line = bufferedReader.readLine())!= null) {
			    machineList.add(getValueFromString(line, 0));
			}
			
			//return list
			return machineList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return empty string in case of error
		ArrayList<String> s = new ArrayList<String>();
		return s;
		
	}
	
	
	
	//creates new folders/files for today's output
	public static void createOutputFile() {
		
		//creates dateformat values
		Date currentDateTime = Calendar.getInstance().getTime();
		DateFormat yearFormat = new SimpleDateFormat("yyyy");
		DateFormat monthFormat = new SimpleDateFormat("MMMM");
		DateFormat dayFormat = new SimpleDateFormat("dd");
		String year = yearFormat.format(currentDateTime);
		String month = monthFormat.format(currentDateTime);
		String day = dayFormat.format(currentDateTime);
		
		//create folders/file for today's log
		outputFileDir = System.getProperty("user.dir") + "\\" + year + "\\" + month;
		File file = new File(outputFileDir);
		file.mkdirs();
		file = new File(outputFileDir + "\\" + day + ".txt");
		try {file.createNewFile();}
		catch (IOException e) {e.printStackTrace();}
	}
	
	
	
	//outputs a log of events
	/*NOT FINISHED, ADD DATETIME FUNCTIONALITY*/
	public static void outputToLog(int ID, int machine, Boolean wasSuccessful, String extraText) {
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
			while ((line = bufferedReader.readLine()) != null) {
				doc.add(line);
			}
			bufferedReader.close();
			
			//write from doc to output document
			bufferedWriter = new BufferedWriter(new FileWriter(outputFileDir));
			for (int i = 0; i < doc.size(); i++) {
				bufferedWriter.write(doc.get(i));
				bufferedWriter.newLine();
			}
			
			//add log statement to document
			String printStatement = "";
			if (wasSuccessful) {
				printStatement = "User " + ID + " attempted to log in to the " + selectedMachineName + " at " + time + " and was successful.";
			} else if (!wasSuccessful) {
				printStatement = "User " + ID + " attempted to log in to the " + selectedMachineName + " at " + time + " and was not successful";
			}
			if (!extraText.equals("")) {
				printStatement += ". Oh, also: " + extraText;
			}
			bufferedWriter.write(printStatement);
			bufferedWriter.newLine();
		
		//catch/finally error handling
		} catch (IOException e){
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
