package CA;

public class FileInfo {
	
	
	Helper h = new Helper();
	
	public FileInfo(String crashName) {
		CrashName = crashName;
	}

	// Crash information
	private String FileLocation;
	private String CrashName;
	private String CrashURL;
	private int NumberOfFiles;
	private int MissingStackInfoLines;
	private int TotalNumberofLines;
	private int CompleteDirectoryTotal;
	private int CompleteFileTotal;
	
	
	
	private double AbsoluteLCS;
	
	public double getAbsoluteLCS() {
		return AbsoluteLCS;
	}
	public void setAbsoluteLCS(double absoluteLCS) {
		AbsoluteLCS += absoluteLCS;
	//	System.out.println("Add:" + absoluteLCS + ":" + AbsoluteLCS);
	}

	// Distance values
	private int TotalLinesSameInFolder=0; // Total number of lines that match up between files
	private int TotalLongestLineSameCountInFolder=0; // LongestSequence of similar items.

	
	public int getTotalLinesSameInFolder() {
		return TotalLinesSameInFolder;
	}
	public void setTotalLinesSameInFolder(int totalLinesSameInFolder) {
		TotalLinesSameInFolder += totalLinesSameInFolder;
	}
	public int getTotalLongestLineSameCountInFolder() {
		return TotalLongestLineSameCountInFolder;
	}
	
	public void setTotalLongestLineSameCountInFolder(int totalLongestLineSameCountInFolder) {
		TotalLongestLineSameCountInFolder += totalLongestLineSameCountInFolder;
	}
	

	public double getAvgByLineTotalLinesSameInFolder(){return  h.roundDouble((double)TotalLinesSameInFolder/(double)TotalNumberofLines);}
	public double getAvgByLineTotalLongestLineSameCountInFolder(){return  h.roundDouble((double)TotalLongestLineSameCountInFolder/(double)TotalNumberofLines);}
	
	
	public double getAvgByCrashLongestLineSameCountInFolder() {
		return Double.valueOf(TotalLongestLineSameCountInFolder)/ Double.valueOf(NumberOfFiles);
	}
	
	public double getAvgByCrashTotalLinesSameInFolder() {
		return Double.valueOf(TotalLinesSameInFolder)/ Double.valueOf(NumberOfFiles);
	}
	
	public int getTotalNumberofLines()
	{
		return TotalNumberofLines;
	}
	
	public void setTotalNumberofLines(int totalNumberofLines)
	{
		TotalNumberofLines = totalNumberofLines;
	}
	
	public int getMissingStackInfoLines()
	{
		return MissingStackInfoLines;
	}
	
	public void setMissingStackInfoLines(int missingStackInfoLines)
	{
		MissingStackInfoLines = missingStackInfoLines;
	}
	
	public String getCrashName() {
		return CrashName;
	}
	public void setCrashName(String crashName) {
		CrashName = crashName;
	}
	public String getCrashURL() {
		return CrashURL;
	}
	public void setCrashURL(String crashURL) {
		CrashURL = crashURL;
	}
	public int getNumberOfFiles() {
		return NumberOfFiles;
	}
	
	public String getFileLocation(){
		return FileLocation;
	}
	
	public void setFileLocation(String fileLocation)
	{
		FileLocation = fileLocation;
	}
	
	public void setNumberOfFiles(int numberOfFiles) {
		NumberOfFiles = numberOfFiles;
	}
	
	public int getCompleteDirectoryTotal() {
		return CompleteDirectoryTotal;
	}
	public void setCompleteDirectoryTotal(int completeDirectoryTotal) {
		CompleteDirectoryTotal = completeDirectoryTotal;
	}
	public int getCompleteFileTotal() {
		return CompleteFileTotal;
	}
	public void setCompleteFileTotal(int completeFileTotal) {
		CompleteFileTotal = completeFileTotal;
	}


	
	// Brodie Distance values
	private double BrodieValue; //calculated Brodie value between two files
	private int BrodieLCS; // Brodie Longest Common Sequence
	private int BrodieMatches; //Number of matching lines between two Brodie files
	private double BrodieValueWithLineWeight;

	public double getBrodieValue() {
		return BrodieValue;
	}
	public void setBrodieValue(double brodieValue) {
		BrodieValue += brodieValue;
	}
	
	public double getBrodieValueWithLineWeight() {
		return BrodieValueWithLineWeight;
	}
	public void setBrodieValueWithLineWeight(double brodieValueWithLineWeight) {
		BrodieValueWithLineWeight += brodieValueWithLineWeight;
	}
	
	
	public int getBrodieLCS() {
		return BrodieLCS;
	}
	public void setBrodieLCS(int brodieLCS) {
		BrodieLCS += brodieLCS;
	}
	public int getBrodieMatches() {
		return BrodieMatches;
	}
	public void setBrodieMatches(int brodieMatches) {
		BrodieMatches += brodieMatches;
	}
	
	
	// now get the average calculations for each
	public double getBrodieValueByLine() {return h.roundDouble((double)BrodieValue/(double)TotalNumberofLines);}
	public double getBrodieValueByFile() {return h.roundDouble((double)BrodieValue/(double)NumberOfFiles);}
	
	public double getBrodieLCSValueByLine() {return h.roundDouble((double)BrodieLCS/(double)TotalNumberofLines);}
	public double getBrodieLCSValueByFile() {return h.roundDouble((double)BrodieLCS/(double)NumberOfFiles);}
	
	public double getBrodieMatchesByLine() {return h.roundDouble((double)BrodieMatches/(double)TotalNumberofLines);}
	public double getBrodieMatchesByFile() {return h.roundDouble((double)BrodieMatches/(double)NumberOfFiles);}
	

	public double getBrodieValueLineWeightByLine() {return h.roundDouble((double)BrodieValueWithLineWeight/(double)TotalNumberofLines);}
	public double getBrodieValueLineWeightByFile() {return h.roundDouble((double)BrodieValueWithLineWeight/(double)NumberOfFiles);}
	
	
	
	
	
	
	
}