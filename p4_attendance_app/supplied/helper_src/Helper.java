import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

/*
 * @author:  Sal LaMarca
 * @version:  0.1
 * @date:  01/18/2012
 *
 * This class contains methods to parse an attendance scanner
 * file and append its attendance data to an old csv file
 * that contains class attendance data for one class.
 * 
 */
public class Helper {
	
	
	//Store the mappings of dates and student ids for each date in a scanner file
	TreeMap<String, ArrayList<String>> datesMappedToStudentIdsForScannerData = new TreeMap<String, ArrayList<String>>();

	//Store the mappings of student ids to their attendance info for each student in an old attendance csv file
	TreeMap<String, String> studentIdsMappedToOldAttendanceData = new TreeMap<String, String>();
	
	
	/*
	 * Parse an attendance file from scanner and append its attendance data to an attendance file for one class.
	 * 
	 * The first argument to main is the name of the old attendance file for one class.
	 * The second argument to main is the scanner attendance file from OPN2001 barcode scanner in plain text format.
	 * 
	 */
    
    
    
    public void parseFile(String oldFile, String attendanceFile, int out_number) {
     
        String oldAttendanceFile = oldFile;
        String scannerFile = attendanceFile;
        
        if(oldAttendanceFile.toLowerCase().contains(".csv") || oldAttendanceFile.toLowerCase().contains(".txt")){
			parseScanner(oldAttendanceFile, scannerFile, out_number);			
		}
		else{
			System.out.println("Error:  Old attendance file name must end with .txt or .csv");
			return;
		}
        
    }
    
    
	public static void main(String args[]){
		
        Helper attObj = new Helper();
        
        attObj.parseFile("default.csv", "01_25.12.txt", 1);
        
        System.out.println("done!");
        
        
		/* if(args.length < 2){
			System.out.println("Error:\t\t Must input old attendance file name and scanner attendance file name");
			System.out.println("Correct Usage:\t java Attendance <old attendance file> <scanner attendance file>");
			System.out.println("Example:\t java Attendance oldAttendance.csv scannerFile.txt ");
			System.out.println("Note:\t\t oldAttendance.csv must be in csv format with at least three columns: ");
			System.out.println("\t\t\t Last Name, First Name, Student Id");
			System.out.println("\t\t\t Student Id must be in the third column");
			return;
		}

		Attendance attObj = new Attendance();
		String oldAttendanceFile = args[0];
		String scannerFile = args[1];
		if(oldAttendanceFile.toLowerCase().contains(".csv") || oldAttendanceFile.toLowerCase().contains(".txt")){
			attObj.parseScanner(oldAttendanceFile, scannerFile);			
		}
		else{
			System.out.println("Error:  Old attendance file name must end with .txt or .csv");
			return;
		} */
		
		
	}
	
	//Parse the scanner attendance file and fill the scannerStudentIds and scannerDates
	//This method assumes that the scanner attendance file is the default one from
	//  extracted
	public String parseScanner(String oldAttendanceFile,String scannerFile, int out_number){
		String result = "Error occured!\nPlease verify that the inputted old attendance " +
				"file is for one class, formatted in csv properly, and the third " +
				"column contains valid Student Ids (810 numbers)";
		result += "\nAlso make sure the inputted scanner file is a plain text file "+
				" from an OPN2001 barcode scanner.";
		try{
			FileReader fr = new FileReader(scannerFile);
			BufferedReader br = new BufferedReader(fr);
			String s = "";
			String[] s1 = s.split(",");
			//Loop through the scanner attendance file and grab the studentIds and dates
			while((s = br.readLine()) != null) {
				if(!s.trim().equalsIgnoreCase("")){
					s1 = s.split(",");
					//Parse studentIds from scanner file
					String studentId = s1[0].substring(6, s1[0].length()-1);
					//Parse the date
					String date = s1[3];
					date = date.replace('/', '_');
					date = date.trim();
					studentId = studentId.trim();
					if(!datesMappedToStudentIdsForScannerData.containsKey(date)){
						ArrayList<String> studentIds = new ArrayList<String>();
						studentIds.add(studentId);
						datesMappedToStudentIdsForScannerData.put(date, studentIds);
					}
					else{
						datesMappedToStudentIdsForScannerData.get(date).add(studentId);
					}
				}
			}
			
			fr = new FileReader(oldAttendanceFile);
			br = new BufferedReader(fr);
			s = "";
			s = br.readLine();
			String columnInfo = s.trim();
			s1 = s.split(",");
			//Loop through the oldAttendance file and grab the information
			while((s = br.readLine()) != null) {
				if(!s.trim().equalsIgnoreCase("")){
					s1 = s.split(",");
					//Parse studentIds from old Attendance file
					String studentId = s1[2];
					studentId = studentId.trim();
					if(!studentIdsMappedToOldAttendanceData.containsKey(studentId)){
						studentIdsMappedToOldAttendanceData.put(studentId, s);
					}
					else{
						datesMappedToStudentIdsForScannerData.get(studentId).add(studentId);
					}
				}
			}
			
			for( String date : datesMappedToStudentIdsForScannerData.keySet() ){
				columnInfo += "," + date;
				for( String studentIdAttendance : studentIdsMappedToOldAttendanceData.keySet() ){
					String studentInfo = studentIdsMappedToOldAttendanceData.get(studentIdAttendance);
					if(datesMappedToStudentIdsForScannerData.get(date).contains(studentIdAttendance)){
						studentInfo += ",1";
					}
					else{
						studentInfo += ",0";
					}
					studentIdsMappedToOldAttendanceData.put(studentIdAttendance, studentInfo);
				}
			}

			result = columnInfo + "\n";
			String unsortedResult = "";

			for( String studentId : studentIdsMappedToOldAttendanceData.keySet() ){
				unsortedResult += studentIdsMappedToOldAttendanceData.get(studentId) + "\n";
			}
			
			String[] a = unsortedResult.split("\n");
			unsortedResult = "";
			Arrays.sort(a);
			for( String sortedInfo : a ){
				unsortedResult += sortedInfo + "\n";
			}
			result += unsortedResult;
			writeToFile(oldAttendanceFile, result, out_number);
			//System.out.println(result);
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			return result;
		}
		
	}
	
	/*
	 * Write stringToWrite to oldAttendanceFile_new
	 */
	public void writeToFile(String oldAttendanceFile, String stringToWrite, int out_number){
		String newFileName = "newAttendanceFile.csv";
        
        String appendum = "_" + out_number + ".csv";
		if(oldAttendanceFile.toLowerCase().contains(".txt")){
			newFileName = oldAttendanceFile.replace(".txt", "_" + out_number + ".txt");
		}
		if(oldAttendanceFile.toLowerCase().contains(".csv")){
            System.out.println("in here!");
			newFileName = oldAttendanceFile.replace(".txt", appendum);			
		}
		try{
			
            newFileName = newFileName + "_" + out_number;
            
			FileWriter fstream = new FileWriter(newFileName);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(stringToWrite);
			out.close();
			
			System.out.println(newFileName + " was created succesfully!");

		}
		catch (Exception e){
			System.out.println("An error occured while writing to file " + newFileName);
			System.out.println("Error: " + e.getMessage());
		}
		
	}

	

}
