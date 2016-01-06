package CA;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import StringComparision.LongestCommonSubstring;
import StringComparision.StringDistances;


// This class contains helper functions
// It differs from utilities because these methods are not shared.


public class Helper {

	//t
	// Get the minimum number of lines in two compared.
	public int getMinLineCount(File file1, File file2)
	{
		int retVal=0;
		int file1Count = getFileLineCount(file1); 
		int file2Count = getFileLineCount(file2); // assign these values so they only have to be called once
		if (file1Count <= file2Count){
			retVal = file1Count;
		}else{
			retVal = file2Count;
		}
		return retVal;
	}

	//t
	// Get the Maximum number of lines in two compared.
	public int getMaxLineCount(File file1, File file2)
	{
		int retVal=0;
		int file1Count = getFileLineCount(file1); 
		int file2Count = getFileLineCount(file2); // assign these values so they only have to be called once
		if (file1Count >= file2Count){
			retVal = file1Count;
		}else{
			retVal = file2Count;
		}
		return retVal;
	}
	
	//t
	// Get the number of lines in a file
	public int getFileLineCount(File file)
	{
        Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int count = 0;
        while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                count++;
        }
        return count;
	}
	
	
	
	// Return the line information
	//t
	public String getLineInformation(File file, int lineNumber)
	{
		// This is all a pretty inneficient way to compare the files.
		//		This can be enchanced.
		String line = "";
        int lineNo;
        try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                for (lineNo = 1; lineNo < 300; lineNo++) {
                        if (lineNo == lineNumber) {
                                line = br.readLine();
                        } else
                                br.readLine();
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        
        // if the line does not exist, just make it ""
        // This will help in the line total calculation values.
        if(line==null || line.equals("")){
        	line="";
       }
	return line;
	}	

	//t
	// get all of the folders in a specific location
	public List<FileInfo> getFolderList(String parentLocation)
	{
		List<FileInfo> fileList = new ArrayList<FileInfo>();
		File folder = new File(parentLocation);
		File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {	  
				if(isExamineFolder(listOfFiles[i])==true){
				//if (listOfFiles[i].isDirectory() && !(listOfFiles[i].getName().toLowerCase().contains("noscan"))) {
				
					fileList.add(new FileInfo(listOfFiles[i].getName()));  
		    	  }
		    }	
			return fileList;
	}
	
	
	//DUPLICATE NAME. THINK ABOUT RENAMING THIS.
	//t
	// get all of the folders in a specific location
	public List<File> getFileList(String parentLocation)
	{
		Helper h = new Helper();
		List<File> fileList = new ArrayList<File>();
		File folder = new File(parentLocation);
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) { 
	    	 if(h.isExamineFile(listOfFiles[i])){
	    		 //System.out.println("Add:" + listOfFiles[i]);
		    	  fileList.add(listOfFiles[i]);
		      } 
		    }
		    
		 return fileList;
	}
	
	
	
	
	// -xxx (first value after 2nd string) = the size of the sequence being looked for
	//
	/*
	public NWData getNWData(String f1,String f2){
		NWAlign nw = new NWAlign();
		NWData nwd= nw.NeedlemanWunsch(f1, f2, -4, 1);
		return nwd;	
	}
	*/
	
	
	
	
	/// Below is the section where all of the distance measurements are called
	
	
	
	
	// Get the generic distance between two strings
		
	// Put into a central location because this comparision is likely to be reused.
	//t
	public boolean isExamineFile(File item)
	{
		 if (item.isFile() && !item.getName().contains("URL") && !item.getName().toLowerCase().contains("report") && !item.getName().toLowerCase().contains("svn")) { 
			 return true;
		 }else{
			 return false;
		 }
	}


	// Check to see if the folder should be examined.
	public boolean isExamineFolder(File item)
	{
		if (item.isDirectory() && !(item.getName().toLowerCase().contains("noscan")) && !item.getName().toLowerCase().contains("svn")) {
			 return true;
		 }else{
			 return false;
		 }
	}

	
	
	
	
	// generate final report for each folder
	public String getFolderReport(FileInfo folderToExamine)
	{
		// ADD MORE VALUES TO THIS
		// IT WILL BE OUTPUT TO THE TEXT FILES
		String retVal = "";
		retVal+="\n\nFolder Values for: "+ folderToExamine.getCrashName() +"\n";
		retVal+="Number of Files:" + folderToExamine.getNumberOfFiles() +"\n";
	//	retVal+="Generic String Distance:" + folderToExamine.getCompleteGenericStringDistanceTotal()+"\n";
		retVal+="Missing Crash Line Count:" + folderToExamine.getMissingStackInfoLines()+"\n";
		
		return retVal;
	}

	
	
	// generate final report for each folder
//	public String getCompleteReport(List<FileInfo> fileList, int TotalNumberofFiles, int TotalNumberofDirectories)
	public String OutputAllData(List<FileInfo> fileList, boolean isExcludeMissing)
	{
		CrashSuper cs = new CrashSuper();
		StringDistances sd = new StringDistances();
		String retVal = "";
		retVal+="---------------Complete Values-------------"+"\n";

	//	retVal+="Number of Directories:" + fileList.size() +"\n";
	//	retVal+="Number of Files:" + TotalNumberofFiles +"\n";
		retVal+="Total Number of Lines:" + getTotalLinesInAllFiles(fileList, isExcludeMissing) +"\n";
		retVal+="Total Number of Lines with Missing Crash Info:" + getTotalLinesMissingCrashInfoInAllFiles(fileList, isExcludeMissing) +"\n";
//DK		retVal+="Total Number of Files with Missing Crash Info:" + getMissingStackInfoFiles(fileList,cs.CrashSuperLocation, isExcludeMissing) +"\n";
		retVal+="Total Number of Folders:" + getTotalNumberOfFolders(fileList, isExcludeMissing) +"\n";
		retVal+="Total Number of Folders With Missing Crash Info:" + getTotalNumberOfFoldersWithMissingCrashInfo(fileList, isExcludeMissing) +"\n";
		retVal+="\n******* String Distance Values **********\n";
			
		
		//GD
	//	retVal += "Final Generic Distance by folder: " + sd.getGenericDistanceMultiFolderAVGByFile(fileList) +"\n";
	//	retVal += "Final Generic Distance by line: " + sd.getGenericDistanceMultiFolderAVGByLine(fileList) +"\n";
	
				

		retVal +=getMissingAndImperfectInfo(fileList);
		return retVal;
	}
	
	// t
	// get the number of lines in a file list
	public int getTotalLinesInAllFiles(List<FileInfo> fileList, boolean isExcludeMissing)
	{
		int retCount=0;
		for (int i = 0; i <= fileList.size()-1; i++) {
			// This should be cleaned up
			if (isExcludeMissing == true){
				if(isFileMissingStackInfo(fileList.get(i))==true){
					retCount+=fileList.get(i).getTotalNumberofLines();
				}
			}else{
				retCount+=fileList.get(i).getTotalNumberofLines();
			}
			//}
		}
		return retCount;
	}
	
	
	
	
	// t
	// get the number of lines with missing crash information in a file list
	public int getTotalLinesMissingCrashInfoInAllFiles(List<FileInfo> fileList, boolean isExcludeMissing)
	{
		int retCount=0;
		for (int i = 0; i <= fileList.size()-1; i++) {
			// This should be cleaned up
			if (isExcludeMissing == true){
				if(isFileMissingStackInfo(fileList.get(i))==false){
					retCount+=fileList.get(i).getMissingStackInfoLines();
				}
			}else{
				retCount+=fileList.get(i).getMissingStackInfoLines();
			}
			//}
		}
		return retCount;
	}
	
	
	
	// Get total number of folders 
	// t
	public int getTotalNumberOfFolders(List<FileInfo> fileList, boolean isExcludeMissing)
	{
		int folderCount=0;
		for (int i = 0; i <= fileList.size()-1; i++) {
			// This should be cleaned up
			if (isExcludeMissing == true){
				if(isFileMissingStackInfo(fileList.get(i))==false){
					folderCount+=1;
				}
			}else{
				folderCount+=1;
			}
		}
		//return fileList.size();
		return folderCount;
	}
	
	
	
	// Get total number of folders with missing crash info
	// t
	public int getTotalNumberOfFoldersWithMissingCrashInfo(List<FileInfo> fileList, boolean isExcludeMissing)
	{
		int RetVal=0;
		for (int i = 0; i <= fileList.size()-1; i++) {
			// This should be cleaned up
			if (isExcludeMissing == true){
				if(isFileMissingStackInfo(fileList.get(i))==false){
					if (fileList.get(i).getMissingStackInfoLines() >0){
						RetVal+=1;
					}
				}
			}else{
				if (fileList.get(i).getMissingStackInfoLines() >0){
					RetVal+=1;
				}
			}
		}
		return RetVal;
	
		
	}
	
		
	//t
	// Get the number of missing crash information for each file
	public int getMissingModuleInfoByFile(File file)
	{
		int NumberofImperfectStack=0;
		Helper h = new Helper();
		for (int i = 2; i < h.getFileLineCount(file); i++) { // start at 2 to ignore the 1st line with just the URL		
			if(isMissingModule(h.getLineInformation(file, i))){
				NumberofImperfectStack+=1;
			}	
		}		
		return NumberofImperfectStack;
	}
	
	//t
	// Check to see if the Module is missing from the file
	public boolean isMissingModule(String lineInfo)
	{
		boolean retVal=false;
		if (lineInfo.length() >3){ // make sure that the line you are comparing is long enough
			if(lineInfo.substring(0, 3).toString().equals("^*^")){
				retVal=true;
			}else{
				retVal=false;
			}
		}else{
			retVal=false;
		}
		return retVal;
	}
	
	//t
	// get the index of a string based on the name
	public int getArrayIndex(List<FileInfo> fileList,String FolderName)
	{
		int retVal=-99;
		for (int i = 0; i < fileList.size(); i++) {	
			if (fileList.get(i).getCrashName().equals(FolderName)){
				retVal=i;		
			}
		}
		return retVal;
	}
	
	
	// Get all the contents of a specified file
	// t
	public String getCompleteFileContents(File file1)
	{
		String retVal="";
		for (int i = 2; i < getFileLineCount(file1)+1; i++) {	 // Start at 2 to ignore the first line
			retVal+= (getLineInformation(file1,i));
		}
		return retVal;
	}
	
	
	// Get all the contents of a specified file with lineBreaks
	public String getCompleteFileContentsWithLineBreak(File file1)
	{
		String retVal="";
		for (int i = 2; i < getFileLineCount(file1)+1; i++) {	 // Start at 2 to ignore the first line
			retVal+= (getLineInformation(file1,i))+"@!@";
		}
		return retVal;
	}
	
	
	
	
	// Round the value of a double to two decimal places
	// t
	public double roundDouble(double val)
	{
		double retVal=0;
		if (Double.isNaN(val)== false){ // ensure that a NaN value is not getting passed in.
			DecimalFormat twoDForm = new DecimalFormat("#.##");
			retVal =Double.valueOf(twoDForm.format(val));
		}
		return retVal;
		
	}
	
	
	// Using string input, determine if file has missing information
	public boolean isFileContainMissingInfoLines(String fileName)
	{
		boolean retVal = false;
		File F1=new File(fileName); 
		for (int i = 2; i < getFileLineCount(F1)+1; i++) {	 // Start at 2 to ignore the first line
			if(isMissingModule(getLineInformation(F1,i))==true){
				retVal=true;
			}
		}
		return retVal;
	}
	
	
	// Take the path of the file and create a file object
	//public File convertFileNameToFile(String FileName){return new File(FileName); }
	
	
	// NOT SURE IF THIS IS NEEDED ANYMORE
	// Check to see if the file is missing any stack 
	public boolean isFileMissingStackInfo(FileInfo file)
	{
		if (file.getMissingStackInfoLines() >0){
			return true;
		}else{
			return false;
		}
	}
	
	
	

	
	
	
	// ADD UNIT TESTS TO THIS TO ACCOUNT FOR IS EXCLUDEMISSING
	// Trim this down to make it more usable
	// Return the number of files in a folder group that contain missing stack information
	public int getMissingStackInfoFiles(List<FileInfo> fileList, String CrashSuperLocation,boolean isExcludeMissing)
	{
		
		// Cycle through all folders
		List<String> ItemsToRemove=new ArrayList<String>();
		
		// Cycle through all of the folders in the main directory
			CrashSuper cs = new CrashSuper();
			Utilities u = new Utilities();
			Helper h = new Helper();
			File folder = new File(CrashSuperLocation);
			File[] listOfFiles = folder.listFiles();
		
			for (int i = 0; i < listOfFiles.length; i++) {	  
				if(h.isExamineFolder(listOfFiles[i])==true){	
					//System.out.println(listOfFiles[i]);
					List<FileInfo> fileList2 = new ArrayList<FileInfo>();
					fileList2 = cs.getFileList(CrashSuperLocation+listOfFiles[i].getName()+"/");
					File folder2 = new File(CrashSuperLocation+listOfFiles[i].getName()+"/");
					
					File[] listOfFiles2 = folder2.listFiles();
					for (int ii = 0; ii < listOfFiles2.length; ii++) {
						
						//System.out.println(listOfFiles2[i].getName());
						// if(h.isFileContainMissingInfoLines(CrashSuperLocation+listOfFiles[i].getName()+"/"+listOfFiles2[ii].getName())){
						if(isScanFileByStringName(listOfFiles2[ii].getName())==true){
							//System.out.println(CrashSuperLocation+listOfFiles2[ii].getName());
							if(h.isFileContainMissingInfoLines(CrashSuperLocation+listOfFiles[i].getName()+"/"+listOfFiles2[ii].getName())){
								ItemsToRemove.add(CrashSuperLocation+listOfFiles[i].getName()+"/"+listOfFiles2[ii].getName());
							}
						}
					}
					
					
				}
			}
		//	System.out.println("size is:" + ItemsToRemove.size());
		return ItemsToRemove.size();
	}

	
	// FIX THIS TO USE ARRAY
	// TIE INTO THE COMPARISONS BEING DONE ABOVE
	public boolean isScanFileByStringName(String FileName)
	{
	//	String[] VALUES = new String[] {"report","url","svn"};
		
		
//		System.out.println("test" + FileName);
		if (FileName.toLowerCase().contains("report") || FileName.toLowerCase().contains("svn") || FileName.toLowerCase().contains("url")){
		//if (FileName.toLowerCase().contains(VALUES[]))
			return false;
		}else{
			return true;
		}
	}
	
	// Create an array with all of the file names in a given master parent directory
	public List<String> getAllFileNames(String parentDirectory){
		CrashSuper cs = new CrashSuper();
		Utilities u = new Utilities();
		Helper h = new Helper();
		List<FileInfo> fileList = new ArrayList<FileInfo>();
		fileList = cs.getFileList(parentDirectory);
		File folder = new File(parentDirectory);
		File[] listOfFiles = folder.listFiles();
		List<String> AllFiles=new ArrayList<String>();	
		
		for (int i = 0; i < listOfFiles.length; i++) {	  
			if(h.isExamineFolder(listOfFiles[i])==true){
				
				List<FileInfo> fileList2 = new ArrayList<FileInfo>();
				fileList2 = cs.getFileList(parentDirectory+listOfFiles[i].getName()+"/");
				File folder2 = new File(parentDirectory+listOfFiles[i].getName()+"/");
				File[] listOfFiles2 = folder2.listFiles();
	
				// Cycle through each folder and reference each file in the folder
				for (int ii = 0; ii < listOfFiles2.length; ii++) {			
					if(isScanFileByStringName(listOfFiles2[ii].getName())==true){
						AllFiles.add(parentDirectory+listOfFiles[i].getName()+"/"+listOfFiles2[ii].getName());
					}
				}
	    	 }
		
	}
		return AllFiles;
	
}

	// WRITE UNIT TESTS FOR THIS
	// 
	public void setLineInformation(File file, String updatedText, int lineNumber) {
		Utilities u = new Utilities();
		String newFileVal="";
		for (int i = 1; i < getFileLineCount(file)+1; i++) {	
			if(i==lineNumber){
				newFileVal+=updatedText+"\n";
			}else{
				newFileVal+=getLineInformation(file, i)+"\n";;
			}
		}		
	
		// Now update the text file with appropriate data
		u.WriteLogFile(getFileNameFromAbsolutePath(file.getAbsolutePath()), getFilePathFromAbsolultePath(file.getAbsolutePath()), newFileVal, false);		
	}	

	
	public String getFileNameFromAbsolutePath(String AbsolutePath)
	{	
		return new File(AbsolutePath).getName();
	}
	
	//
	public String getFilePathFromAbsolultePath(String AbsolutePath)
	{
		return new File(AbsolutePath).getParentFile().toString() +"/";
	}
	
	
	
	// It would be nice to find a way to test this.
	public String getMissingAndImperfectInfo(List<FileInfo> fileList)
	{
		String retVal="";
		double badNW=0;
		double badLCS=0;
		double badGD=0;
		double perfectNW=0;
		double perfectLCS=0;
		double perfectGD=0;		
		
		
		// Cycle through all of the file info and set
		for (int i = 0; i < fileList.size(); i++) {	
			if (fileList.get(i).getMissingStackInfoLines() >0){
				//System.out.println("Bad NW:" + fileList.get(i).getNW());
				//badNW+=fileList.get(i).getNW();
				//badLCS+=fileList.get(i).getLCS();
				//badGD+=fileList.get(i).getGenericStringDistanceTotal();
				
			}else{
				//System.out.println("Perfect NW:" + fileList.get(i).getNW());
				//perfectNW+=fileList.get(i).getNW();
				//perfectLCS+=fileList.get(i).getLCS();
				//perfectGD+=fileList.get(i).getGenericStringDistanceTotal();
			}
		}
		
		getTotalLinesInAllFiles(fileList, false);
		
		retVal+="------- Missing and Perfect Groupings ---------\n";
		retVal+="Missing Generic Distance >Total: " + badGD + "\tCrash:" + roundDouble(badGD/fileList.size()) + "\tLine:" + roundDouble(badGD/getTotalLinesInAllFiles(fileList, false)) + "\n";
		retVal+="Missing LCS > Total: "+ badLCS + "\tCrash:" + roundDouble(badLCS/fileList.size()) + "\tLine:" + roundDouble(badLCS/getTotalLinesInAllFiles(fileList, false)) + "\n";
		retVal+="Missing NW > Total: " + badNW + "\tCrash:" + roundDouble(badNW/fileList.size()) + "\tLine:" + roundDouble(badNW/getTotalLinesInAllFiles(fileList, false)) + "\n";
		retVal+="Perfect Generic Distance> Total: " + perfectGD + "\tCrash:" + roundDouble(perfectGD/fileList.size()) + "\tLine:" + roundDouble(perfectGD/getTotalLinesInAllFiles(fileList, false)) + "\n";
		retVal+="Perfect LCS> Total: " + perfectLCS + "\tCrash:" + roundDouble(perfectLCS/fileList.size()) + "\tLine:" + roundDouble(perfectLCS/getTotalLinesInAllFiles(fileList, false)) + "\n";
		retVal+="Perfect NW> Total: " + perfectNW + "\tCrash:" + roundDouble(perfectNW/fileList.size()) + " \tLine:" + roundDouble(perfectNW/getTotalLinesInAllFiles(fileList, false)) + "\n";
		
		return retVal;
	}

	// Get the longest size between two strings
	//t
	public int getLongestArrayLength(ArrayList<String> file1list, ArrayList<String> file2list){
		int retVal=0;
		if (file1list.size() > file2list.size()){
			retVal=file1list.size();
		}else{
			retVal=file2list.size();
		}
		
		return retVal;
	}
	
	
	// Get the shortest size between two strings
	//t
	public int getShortestArrayLength(ArrayList<String> file1list, ArrayList<String> file2list){
		int retVal=0;
		if (file1list.size() < file2list.size()){
			retVal=file1list.size();
		}else{
			retVal=file2list.size();
		}
		
		return retVal;
	}

	//t
	public List<String> getLongestArray(ArrayList<String> file1list, ArrayList<String> file2list){
		if (file1list.size() >= file2list.size()){
			return file1list;
		}else{
			return file2list;
		}
	}
	
	//t
	public List<String> getShortestArray(ArrayList<String> file1list, ArrayList<String> file2list){
		//if (file1list.size() <= file2list.size()){
		if (file2list.size() > file1list.size()){
			return file1list;
		}else{
			return file2list;
		}
	}
	
	// Build a giant string with all the values from an array
	//t
	public String getCompleteArrayContents(ArrayList<String> file1list){
		String retVal ="";
		for (int i = 0; i < file1list.size(); i++) {	
			retVal += file1list.get(i) + "&*&";
		}
		return retVal;
	}

	// Remove a certain number of items from an array
	//t
	 public List<String> getRemoveItemsEndArray(ArrayList<String> filelist, int numberToRemove)
	 {
		if((filelist.size() >0) && (numberToRemove >0)){
			for (int i = 0; i < numberToRemove; i++) {	
				filelist.remove(filelist.size()-1);
				//System.out.println(getCompleteArrayContents(filelist));
			}
		}
		 return filelist;
	 }
	
	// Remove an item from the start of an array
	//t
	 public List<String> getRemoveItemsStartArray(ArrayList<String> filelist, int numberToRemove)
	 {
		if((filelist.size() >0) && (numberToRemove >0)){
			for (int i = 0; i < numberToRemove; i++) {	
				//System.out.println(getCompleteArrayContents(filelist));
				filelist.remove(0);
			}
		}
		 return filelist;
	 }
	
	/*
	 * 
	// Get a specific item in a line
	public String getLineItem(String lineInfo, int itemPosistion)
	{
		return lineInfo.split("\\^\\*\\^")[itemPosistion];
	}
	*/
	 
	 
	 // Get the URL of the main crash information
	 //t
	 public String getURLFromFolder(String folderLocation)
	 {
		 String retVal="URL not found";
		 File f1=new File(folderLocation+"/URL.txt");
		 if(f1.exists()) {
			 retVal=getLineInformation(f1,1);
		 }
		 return retVal;
	 }
	/* 
	 public boolean deleteDir(File dir) {
	        if (dir.isDirectory()) {
	            String[] children = dir.list();
	            for (int i=0; i<children.length; i++) {
	                boolean success = deleteDir(new File(dir, children[i]));
	                if (!success) {
	                    return false;
	                }
	            }
	        }
	    
	        // The directory is now empty so delete it
	        return dir.delete();
	    }
	 */
	 
	 
	 // Should work, could use some unit tests though
	 public void deleteFile(String fileToRemove) {
		 File file=new File(fileToRemove);
	        if (file.isFile()) {
	        	file.delete();
	        }

	    }
	 
	 
	 // Is the first item from a return string the line break
	 //t
	 public boolean isFirstValBreak(String input){
		 return input.startsWith("@!@");
	 }
	 
	 
	 // Check to see if the value exists in the file
	 //t
	 public boolean isRowInFile(String chk, File file)
	 {
		 boolean retVal=false;
		 for (int i = 2; i <= getFileLineCount(file); i++) {
			 if (chk.toLowerCase().equals(getLineInformation(file, i).toLowerCase().trim())){
				 retVal=true;
			 }
		 }
		 return retVal;		 
	 }
	 
	 
	
	 
	 
}
