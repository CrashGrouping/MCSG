package CA;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
//import java.util.Random;

import StringComparision.StringDistances;

public class CrashSuper {

	// Fix this
	//public final static String CrashSuperLocation = System.getProperty("user.dir") + "/ca/";
	public static String CrashSuperLocation = "";
	private final boolean isExcludeMissingFiles = false; // decide if items with missing crash info should be excluded
	
	public static void main(String[] args) {
	//	System.out.println("run");
		CrashSuper cs = new CrashSuper();
		cs.Run(CrashSuperLocation);
	}

	public void Run(String mainFolder)
	{	
		if(mainFolder == "")
			mainFolder = System.getProperty("user.dir") + "/output/";
		else
			mainFolder = mainFolder + "/output/";
		CrashSuperLocation = mainFolder;
		Helper h = new Helper();
		List<FileInfo> fileList = new ArrayList<FileInfo>();
		fileList=getFileList(mainFolder); // Build the list of file objects to be examined
		BuildFileData(fileList, mainFolder, isExcludeMissingFiles); // Add the values to these objects		
		OutputFileDistanceInfo(fileList, mainFolder);
	}
	
	// Return List of all files
	public List<FileInfo> getFileList(String mainFolder)
	{
		Helper h = new Helper();
		List<FileInfo> fileList = new ArrayList<FileInfo>();
		fileList = h.getFolderList(mainFolder);
		return fileList;
	}
	
	// Iterate through all of the file objects
	public List<FileInfo> BuildFileData(List<FileInfo> fileList, String mainFolder, boolean isExcludeMissingStackInfo)
	{	
			Helper h = new Helper();
			Utilities util = new Utilities();
			removePreviousScrapeReports(mainFolder);
			
			
			
			for (int i = 0; i < fileList.size(); i++) {		
				// set the path of each file in the folder
				fileList.get(i).setFileLocation(mainFolder+fileList.get(i).getCrashName());
				fileList.get(i).setCrashURL(h.getURLFromFolder(mainFolder+fileList.get(i).getCrashName()));
				
				compareFilesInFolder(h.getFileList(mainFolder+fileList.get(i).getCrashName()), fileList.get(i),isExcludeMissingStackInfo);
				
				// write the output of the file
				util.WriteLogFile("CompareReport.txt", mainFolder+fileList.get(i).getCrashName(), h.getFolderReport(fileList.get(i)), false);
				System.out.println(h.getFolderReport(fileList.get(i)));
		
				
				updateStatus(i+1, fileList.size(),fileList.get(i).getCrashName(), mainFolder);
				
			}

			return fileList;		
	}
	

	
	// Compare each folder to another folder
	private void compareFilesInFolder(List<File> list, FileInfo folderToExamine,boolean isExcludeMissingStackInfo)
	{		
		
		System.out.println(folderToExamine.getFileLocation());
		Helper h = new Helper();
		StringDistances sd =  new StringDistances();
		int tempListCount=0;
		for (int i = 0; i < list.size(); i++) {
			folderToExamine.setMissingStackInfoLines(h.getMissingModuleInfoByFile(list.get(i))+folderToExamine.getMissingStackInfoLines());
			
			// Subtract 1 because the first line of any file being examined is the URL
			folderToExamine.setTotalNumberofLines(h.getFileLineCount(list.get(i))+folderToExamine.getTotalNumberofLines()-1);
					
			if ((isExcludeMissingStackInfo== true) && (h.isFileContainMissingInfoLines(folderToExamine.getFileLocation()+"/"+list.get(i).getName()) == true)){
				// do nothing
			}else{
				for (int ii = 0; ii < tempListCount; ii++) {
				
				if (i!=ii){ // do not compare the same file to itself
					if ((isExcludeMissingStackInfo== true) && (h.isFileContainMissingInfoLines(folderToExamine.getFileLocation()+"/"+list.get(i).getName()) == true)){
					}else{
						// do nothing
					}
					System.out.println("Compare files: " + list.get(i).getName() + "-" + list.get(ii).getName());					
					
					// Compare contents of two files line by line
					LoopThroughTwoFiles(list.get(i),list.get(ii), folderToExamine);
				}
			}
			}
			tempListCount=tempListCount+1;
	
		}	
		folderToExamine.setNumberOfFiles(tempListCount);
		
	}
	
	
	
	 
	
	// Look at starting the loop at 2 since the 1st line should be ignored.
	private void LoopThroughTwoFiles(File file1, File file2, FileInfo folderToExamine)
	{	
		Helper h = new Helper();
		StringDistances sd = new StringDistances();
		
		folderToExamine.setTotalLinesSameInFolder(sd.getMatchingLinesInFile(file1, file2));
		folderToExamine.setTotalLongestLineSameCountInFolder(sd.getMaxNumberMatchingLineSequenceInFiles(file1, file2));
		folderToExamine.setBrodieLCS(sd.getNumberBrodieSubStringMatchesByFile(file1, file2));
		folderToExamine.setAbsoluteLCS(sd.getAbsoluteLCS(file1, file2));
		folderToExamine.setBrodieMatches(sd.getMaxNumberMatchingLineSequenceInFiles(file1, file2));
		folderToExamine.setBrodieValue(sd.getBrodieDistance(file1, file2));
		folderToExamine.setBrodieValueWithLineWeight(sd.getBrodieLineDistanceWithMatchPosistionConsideration(file1,file2));
		
		for (int i = 2; i <= h.getMaxLineCount(file1, file2); i++) {	// start at 2 so the first line is ignored.
			// Compare each line against each other in a 1:1 fashion
			ComputeValuesLineByLine(file2, file1, i, folderToExamine); 
		}
	}

	public void ComputeValuesLineByLine(File file1, File file2, int lineNumber, FileInfo folderToExamine)
	{
		Helper h = new Helper();
		StringDistances sd =  new StringDistances();
	}	

	private void OutputFileDistanceInfo(List<FileInfo> fileList, String mainFolder)
	{
		// output final values into a text file
		Utilities u = new Utilities();
		System.out.println(getFinalReportInfo(fileList));
		u.WriteLogFile("DistancesReport.csv",mainFolder,getFinalReportInfo(fileList),false);
	}
	
	
	// get all of the return values for each file
	private String getFinalReportInfo(List<FileInfo> fileList)
	{
		StringDistances sd = new StringDistances();
		// Build the heading for the values to be returned		
		String retVal="Crash,BrodieMatches,BrodieValue,BrodieWeight,BrodieLCS,AbsoluteLCS,Number of Crashes,Number of Lines,URL\n";
		
		for (int i = 0; i < fileList.size(); i++) {	
			retVal+=getFinalReportLineInfo(fileList.get(i)) + "\n";
		}
		retVal+="Total:,"+ sd.getAvgTotalMatchesByFile(fileList)+","+ sd.getAvgBrodieByFile(fileList)+","+ sd.getAvgBrodieByWeight(fileList)+","+ sd.getAvgBrodieLCSByFile(fileList)+"," + sd.getAvgLCSBYAbsoluteLength(fileList);
		
		return retVal;
	}
	
	// Get the line information for a specific group of files.
	private String getFinalReportLineInfo(FileInfo fileinfo)
	{
		Helper h = new Helper();
		return fileinfo.getCrashName()+","+ fileinfo.getBrodieMatchesByFile()+","+ fileinfo.getBrodieValueByFile()+","+ fileinfo.getBrodieValueLineWeightByFile()+","+ fileinfo.getBrodieLCSValueByFile() + "," + fileinfo.getAbsoluteLCS() + "," + fileinfo.getNumberOfFiles() + "," + fileinfo.getTotalNumberofLines() +","+ fileinfo.getCrashURL();	
	}
	
	// Clean out the older reports.
	private void removePreviousScrapeReports(String mainFolder)
	{
		Helper h = new Helper();
		h.deleteFile(mainFolder + "/" + "StatusReport.csv");
		h.deleteFile(mainFolder + "/" + "DistancesReport.csv");	
	}
	
	
	// show the current status of the folders being run against.
	private void updateStatus(int currentFolderCount,int MaxSize, String CrashName, String mainFolder)
	{
		Helper h = new Helper();
		Utilities u = new Utilities();
		String status = currentFolderCount + "/" + MaxSize +"=" + h.roundDouble(((double)currentFolderCount/(double)MaxSize)*100) + "%, " + u.getDateTime() + "," + CrashName + "\n";
		System.out.println(status);
		if(currentFolderCount == 1)
			u.WriteLogFile("StatusReport.csv",mainFolder,"Progress,Time,File\n",true);
		u.WriteLogFile("StatusReport.csv",mainFolder, status,true);	
	}
	
	
	
}
