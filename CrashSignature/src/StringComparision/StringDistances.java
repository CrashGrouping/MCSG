
package StringComparision;
import java.io.File;
import java.util.List;

import CA.FileInfo;
import CA.Helper;

import com.wcohen.ss.*;

// Taken from 	http://secondstring.sourceforge.net/javadoc/
// This class will reference any external libraries or sources for string distances.
// The Goal is to keep all external string references localized here.

public class StringDistances {
	

	//Get the number of lines that match between two files.
	//t
	public int getMatchingLinesInFile(File file1, File file2){
		CA.Helper h = new CA.Helper();
		int NumberOfMatchingLines=0;
		
		for (int i = 2; i <= h.getMinLineCount(file1, file2); i++) {
		//	System.out.println("Compare Lines:" + h.getLineInformation(file1,i) + "*" + h.getLineInformation(file2,i));
			if((h.getLineInformation(file1,i).trim().equals(h.getLineInformation(file2,i).trim()))){
				NumberOfMatchingLines=NumberOfMatchingLines+1;
			}
		}
		
		return NumberOfMatchingLines;
	}
	
	// get the number of lines that match between two folders
	// t
	public int getMaxNumberMatchingLineSequenceInFiles(File file1, File file2)
	{
		CA.Helper h = new CA.Helper();
		int maxLongestSequenceCounter=0;
		int currentLongestSequenceCounter=0;
		
		for (int i = 2; i <= h.getMinLineCount(file1, file2); i++) {
			if((h.getLineInformation(file1,i).trim().equals(h.getLineInformation(file2,i).trim()))){
				currentLongestSequenceCounter = currentLongestSequenceCounter+1;
				if (currentLongestSequenceCounter > maxLongestSequenceCounter){
					maxLongestSequenceCounter = currentLongestSequenceCounter;
				}
			}else{
				//System.out.println("Reset Counter at :" +currentLongestSequenceCounter );
				currentLongestSequenceCounter=0; // reset the counter
			}
		}
	
		return maxLongestSequenceCounter;
	}
	
	// Distance value taken from Brodie Paper
	//t
	public double getBrodieDistance(File file1, File file2)
	{
		double retVal=0;
		Helper h = new Helper();
		
		// check to see if their lengths are the same
		if (h.getFileLineCount(file1) == h.getFileLineCount(file2)){
			retVal = (double)getNumberBrodieSubStringMatchesByFile(file1, file2)/(double)(h.getFileLineCount(file1)-1);
		}else{
			retVal = (double)getNumberBrodieSubStringMatchesByFile(file1, file2)/((h.getFileLineCount(file1)-1+h.getFileLineCount(file2)-1)/2);	
		}

		return h.roundDouble(retVal);
	}
	
	

	//t
	public int getBrodieNumberSubStringMatches(String val)
	{

		int retCount=0;
		if (val.equals("@!@")){			
			val="";
		}
		
		if (val.length() > 3)
		{
			if((val.substring(0,3).equals("@!@"))){
				val=val.substring(3,val.length());
			}
		}
		
		retCount=val.split("\\Q@!@"+"\\E", -1).length - 1; 
		return retCount;
	}

	//t
//	public String getBrodieLongestSubstring(String str1, String str2) {
	public String getBrodieLongestSubstring(File file1, File file2) {
		Helper h = new Helper();
		String str1 = h.getCompleteFileContentsWithLineBreak(file1);
		String str2 = h.getCompleteFileContentsWithLineBreak(file2);
		
		StringBuilder sb = new StringBuilder();
		if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty())
		  return "";

		// ignore case
		str1 = str1.toLowerCase();
		str2 = str2.toLowerCase();

		// java initializes them already with 0
		int[][] num = new int[str1.length()][str2.length()];
		int maxlen = 0;
		int lastSubsBegin = 0;

		for (int i = 0; i < str1.length(); i++) {
		for (int j = 0; j < str2.length(); j++) {
		  if (str1.charAt(i) == str2.charAt(j)) {
		    if ((i == 0) || (j == 0))
		       num[i][j] = 1;
		    else
		       num[i][j] = 1 + num[i - 1][j - 1];

		    if (num[i][j] > maxlen) {
		      maxlen = num[i][j];
		      // generate substring from str1 => i
		      int thisSubsBegin = i - num[i][j] + 1;
		      if (lastSubsBegin == thisSubsBegin) {
		         //if the current LCS is the same as the last time this block ran
		         sb.append(str1.charAt(i));
		      } else {
		         //this block resets the string builder if a different LCS is found
		         lastSubsBegin = thisSubsBegin;
		         sb = new StringBuilder();
		         sb.append(str1.substring(lastSubsBegin, i + 1));
		      }
		   }
		}
		}
		}

		//return sb.toString();
		return cleanBrodieLCS(sb.toString(), file1, file2);
		}
	
	
	//Get the distance of the longest substring between two items
	//t
	public int getNumberBrodieSubStringMatchesByFile(File file1, File file2){
		Helper h = new Helper();
		return getBrodieNumberSubStringMatches(getBrodieLongestSubstring(file1,file2));

	}
	
	
	
	//t
	public double getBrodieLineDistanceWithMatchPosistionConsideration(File file1, File file2){
		CA.Helper h = new CA.Helper();
		int minLineCount=h.getMinLineCount(file1, file2);
		int maxLineCount=h.getMaxLineCount(file1, file2);
		double retVal=0;
		double currentVal=0;
		double maxVal=0;
		for (int i = 2; i <= minLineCount; i++) {
		//	System.out.println("Compare Lines with  Weight:" + h.getLineInformation(file1,i) + "*" + h.getLineInformation(file2,i));
			if((h.getLineInformation(file1,i).trim().equals(h.getLineInformation(file2,i).trim()))){
				currentVal+= (double)((minLineCount-i)+1)/maxLineCount;
			}
			maxVal+= (double)((maxLineCount-i)+1)/(maxLineCount*2);
			
		}
		retVal=currentVal/maxVal;
		return h.roundDouble(retVal)/2;
	}
	
	//t
	public double getAvgTotalMatchesByFile(List<FileInfo> fileList)
	{
		Helper h = new Helper();
		double retVal=0;
		for (int i = 0; i <= fileList.size()-1; i++) {
			retVal+=fileList.get(i).getBrodieMatchesByFile();
		}
		
		System.out.println("Values:" + retVal + "/" + fileList.size());
		
		return h.roundDouble(retVal/fileList.size());
	}
	
	//t
	public double getAvgBrodieByFile(List<FileInfo> fileList)
	{
		Helper h = new Helper();
		double retVal=0;
		for (int i = 0; i <= fileList.size()-1; i++) {
			retVal+=fileList.get(i).getBrodieValueByFile();
		}
		return h.roundDouble(retVal/fileList.size());
	}
	
	
	
	//t
	public double getAvgBrodieLCSByFile(List<FileInfo> fileList)
	{
		Helper h = new Helper();
		double retVal=0;
		for (int i = 0; i <= fileList.size()-1; i++) {
			retVal+=fileList.get(i).getBrodieLCSValueByFile();
		}
	//	System.out.println("getAvgBrodieLCSByFile:" + retVal + "/" + fileList.size());
		return h.roundDouble(retVal/fileList.size());
	}
	
	
	// not tested
	public double getAvgBrodieLCSByLine(List<FileInfo> fileList)
	{
		Helper h = new Helper();
		double retVal=0;
		for (int i = 0; i <= fileList.size()-1; i++) {
			retVal+=fileList.get(i).getBrodieLCSValueByLine();
		}
		return h.roundDouble(retVal/fileList.size());
	}
	
	
	
	//t
	public double getAvgBrodieByWeight(List<FileInfo> fileList)
	{
		Helper h = new Helper();
		double retVal=0;
		for (int i = 0; i <= fileList.size()-1; i++) {
			//retVal+=fileList.get(i).getBrodieValueWithLineWeight();
			retVal+=fileList.get(i).getBrodieValueLineWeightByFile();
		}
		return h.roundDouble(retVal/fileList.size());
	}
	
	
	// Get the Brodie Absolute LCS as Wei described on 2/2
	// t
	public double getAbsoluteLCS(File file1, File file2)
	{
		Helper h = new Helper();
		int fileLineCount1=h.getFileLineCount(file1)-1;
		int fileLineCount2=h.getFileLineCount(file2)-1;
		int longestSubString = getNumberBrodieSubStringMatchesByFile(file1, file2);

		return h.roundDouble((double)getNumberBrodieSubStringMatchesByFile(file1, file2)/(double)((double)(fileLineCount1+fileLineCount2)/2));

	}
	
	
	

	//LCS/stack length
	public double getAvgLCSBYAbsoluteLength(List<FileInfo> fileList)
	{
		Helper h = new Helper();
		double retVal=0;
		for (int i = 0; i <= fileList.size()-1; i++) {
			retVal+=fileList.get(i).getAbsoluteLCS();
		}
		
//		LCS/((L1+2)/2)
	//	System.out.println("getAvgLCSBYAbsoluteLength:" + retVal + "/" + fileList.size());
		//return h.roundDouble(retVal/h.getTotalLinesInAllFiles(fileList, false));
		return h.roundDouble(retVal/fileList.size());
	}

	
	
	
	
	
	
	
	// get the average values
	//Matches
	//Brodie
	// BrodieWeight
	//BrodieLCS
	
	
	//Make sure that there are not starting pieces of information that need to be removed.
	//t
	public String cleanBrodieLCS(String retLCS, File file1, File file2)
	{
		// Might make sense to make this some sort of global variable
		final String startSequence = "@!@"; // done like this just in case things are changed
		Helper h = new Helper();
		if (!h.isFirstValBreak(retLCS)){
			// get everything before the first "@!@" 
			
		if(retLCS.contains(startSequence)){	
			final int firstIndexOfSeperator = retLCS.indexOf(startSequence);
			final String startVal = retLCS.substring(0,firstIndexOfSeperator);
			
			// now check to see if this value exists in any of the lines
			if (!h.isRowInFile(startVal,file1)){// I do not believe I need to check for the row in both files
				// since the value does not match, then make sure to remove it
				retLCS = retLCS.substring(firstIndexOfSeperator,retLCS.length());
			}
		}
		} // No need to do any of this if the first item is not a break.
		
		
		return retLCS;
	}
	
	//  Returns a double based on similar prefix stack comparison
	//  If an error occurs it will return -1
	//  Otherwise it will return AmountMatched/LengthOfBiggestStack
	public double getPrefixMatch(File file1, File file2){
		
		double minlen, maxlen;
		double matched = 0;
		boolean same = true;
		Helper h = new Helper();
		String str1 = h.getCompleteFileContentsWithLineBreak(file1);
		String str2 = h.getCompleteFileContentsWithLineBreak(file2);
		
		//Error, return -1
		if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty())
		  return -1;

		// ignore case
		str1 = str1.toLowerCase();
		str2 = str2.toLowerCase();
		// split on @!@ (the value used as a placeholder to split on)
		String str1Lines[] = str1.split("@!@");
		String str2Lines[] = str2.split("@!@");
		//set sizes of arrays to not hit indexOutOfBounds exceptions and calculate difference
		if(str1Lines.length > str2Lines.length){
			maxlen = str1Lines.length;
			minlen = str2Lines.length;
		}
		else{
			maxlen = str2Lines.length;
			minlen = str1Lines.length;
		}
		
		//loop until end of the smallest stack or until a difference is found
		int index = 0;
		while(index < minlen && same){
			if(str1Lines[index].equals(str2Lines[index]))
				matched++;
			else
				same = false;
			index++;
		}
		
		return (matched/maxlen);
	}
	
	// Returns an int which is the longest substring found
	// within the two file's call stacks.
	// Returns -1 if files are empty or have no length.
	public int getLongestSubstring(File file1, File file2){
		Helper h = new Helper();
		String str1 = h.getCompleteFileContentsWithLineBreak(file1);
		String str2 = h.getCompleteFileContentsWithLineBreak(file2);
		
		StringBuilder sb = new StringBuilder();
		if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty())
		  return -1;

		// ignore case
		str1 = str1.toLowerCase();
		str2 = str2.toLowerCase();

		// java initializes them already with 0
		int[][] num = new int[str1.length()][str2.length()];
		int maxlen = 0;
		int lastSubsBegin = 0;

		for (int i = 0; i < str1.length(); i++) {
		for (int j = 0; j < str2.length(); j++) {
		  if (str1.charAt(i) == str2.charAt(j)) {
		    if ((i == 0) || (j == 0))
		       num[i][j] = 1;
		    else
		       num[i][j] = 1 + num[i - 1][j - 1];

		    if (num[i][j] > maxlen) {
		      maxlen = num[i][j];
		      // generate substring from str1 => i
		      int thisSubsBegin = i - num[i][j] + 1;
		      if (lastSubsBegin == thisSubsBegin) {
		         //if the current LCS is the same as the last time this block ran
		         sb.append(str1.charAt(i));
		      } else {
		         //this block resets the string builder if a different LCS is found
		         lastSubsBegin = thisSubsBegin;
		         sb = new StringBuilder();
		         sb.append(str1.substring(lastSubsBegin, i + 1));
		      }
		   }
		}
		}
		}

		//return sb.toString();
		return maxlen;
	}
	
}
	
	
	

