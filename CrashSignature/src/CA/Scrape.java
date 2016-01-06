package CA;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


import com.meterware.httpunit.*;

public class Scrape {

	private String OutputLocation;

	//private final String TopCrashList = "https://crash-stats.mozilla.com/topcrasher/products/Firefox/versions/30.0a1?days=7";
	private final String TopCrashList = "https://crash-stats.mozilla.com/topcrashers/?product=Firefox&version=46.0a1&days=7";
					
	private final String mainSiteURL="https://crash-stats.mozilla.com";

	private int FileProcessCount=0;
	private String StartTime;
	private String EndTime; 
	private int FilesCreated=0;
	private int FolderProcessCount=0;
	private int FileSkipCount=0;
	private int tempFolderCounter=0;
	private int dataTable=0;
	private String MissingList="";
		
	public void Run(String outputFolder){
		if(outputFolder == "")
			OutputLocation = System.getProperty("user.dir") + "/output/";
		else
			OutputLocation = outputFolder + "/output/";
		StartConsoleReport();
		getMainPageInfo();		
		EndConsoleReport();		
	}
	
	 // Get all of the links off of the main page
	 private void getMainPageInfo(){
		 HttpUnitOptions.setScriptingEnabled(false);	
		    try {
		      WebConversation wc = new WebConversation();
		      WebRequest request = new GetMethodWebRequest( TopCrashList );
		      WebResponse response = wc.getResponse( request );
		      int i=0;
	          WebTable[] tables = response.getTables();
	          //for (i = 1; i < tables[0].getRowCount(); i++) {
	        	  System.out.println("49" + mainSiteURL+tables[0].getTableCell(i, 3).getLinkWith("").getURLString());
	        	  getCrashReportLinks(mainSiteURL+tables[0].getTableCell(i, 3).getLinkWith("").getURLString());
	          //}
	       	          
		    } catch (Exception e) {
		       System.err.println( "Exception: " + e );
		    }
	 }

	
	// get all the links 
	private void getCrashReportLinks(String URL){	
		System.out.println(URL);

		String Info="";	
		HttpUnitOptions.setScriptingEnabled(false);	
	    try {
	    	WebConversation wc = new WebConversation();
	    	WebRequest request = new GetMethodWebRequest( URL );
	    	WebResponse response = wc.getResponse( request );
	    		    	
	    	WebTable[] tables = response.getTables();
	    	for(int k = 0; k < tables.length; k++){
	    		if(tables[k].getRowCount() > 1)
	    			dataTable = k;
	    	}
	    	for(int i = 1; i < 2; i++){
    		//for (int i = 1; i < tables[dataTable].getRowCount(); i++) {
	    		System.out.print("Processing: " + i +"\n");
	    		String Title = response.getTitle().replace(":", "").replace(" ", "").replaceAll("[^\\p{L}\\p{N}]", "");    	
	    		getCrashReportPageInfo(URL, Title, mainSiteURL+tables[dataTable].getTableCell(i, 1).getLinkWith(":").getURLString(),1);    		    	
	    	}
	    } catch (Exception e) {
		       System.err.println( "Exception: " + e );
		       e.printStackTrace();
		    }
	    System.out.println(Info);	
	    
	}

	// Generate .txt file with 1st
	private void getCrashReportPageInfo(String URL, String PageTitle, String pageURL, int tableNumber){	
		System.out.println(pageURL);
		
		setStartTime();
		HttpUnitOptions.setScriptingEnabled(false);	
	    try {
	      WebConversation wc = new WebConversation();
	      WebRequest request = new GetMethodWebRequest( pageURL );
	      WebResponse response = wc.getResponse( request );

	   // Does not contain any crash thread information.
	      // Would skip a crash with current implementation, need an example
	      // of an empty crash to implement this properly. (Should never occur) 
	      /*if (!response.getText().contains("<h2>Crashing Thread</h2>")){
	    	  System.out.println("-->Skipped: " + pageURL);
	    	  FileSkipCount+=1;
	      }else{*/
	    	  int i=0;	      
	    	  WebTable[] tables = response.getTables();
	    	  String CrashThreadInfo=pageURL+"\n";
	    	  String columnValue="";
	    	  for (i = 1; i < tables[1].getRowCount(); i++) {
	    		  columnValue=tables[1].getCellAsText(i, 2);
	    		  if (columnValue.equals("")){
	    			  columnValue="EMPTY";
	    			  MissingList+=pageURL+"\n";
	    			  System.out.println("MISSING INFO: " + CrashThreadInfo);
	    		  }
	    		 
	    		  CrashThreadInfo+=columnValue+"\n";
	    	  }
	    	  // Comment out for now. May be used later.
	    	  // writeToFile(PageTitle,"Report_"+response.getTitle().substring(response.getTitle().indexOf("ID:")+4, response.getTitle().length()),getAllCrashMetaStats(URL));
	          
	    	  writeToFile(PageTitle,"URL",URL);
	          writeToFile(PageTitle, response.getTitle().substring(response.getTitle().indexOf("ID:")+4, response.getTitle().length()),CrashThreadInfo);
	          FileProcessCount +=1;
	      //closing bracket for the if/else branch}
	    } catch (Exception e) {
	    	System.err.println( "Exception: " + e );
		}
		    endScript(URL);
	  }

	 private void writeToFile(String PageTitle, String FileLocation, String FileContents)
	 {
		 try {
			 File dir = new File(OutputLocation+PageTitle);  
			 if (dir.isDirectory()==false){
				 dir.mkdirs();
			 }
			 FileWriter outFile = new FileWriter(OutputLocation+PageTitle+"/"+FileLocation.trim()+".txt");
			 PrintWriter out = new PrintWriter(outFile);
			 out.println(FileContents);             
			 out.close();
			 
			 if (FileLocation != "URL"){
				 System.out.println("->output complete....");
			 }
			 
			 } catch (IOException e){
				 e.printStackTrace();
			 }
	 }
	 	
	 private void StartConsoleReport(){
		 System.out.println("Starting application......");
	 }
	
	 private void EndConsoleReport(){
		 System.out.println("Total files processed:" + FileProcessCount);
	 }	 
	 
	 private void WriteCrashStats(String URL)
	 {
		 FileWriter out;
		try {
			out = new FileWriter(URL, true);
			out.write("Start Time: " + StartTime +"\n");
			out.write("End Time: " + EndTime +"\n");
			out.write("Total Files Processed: " + FileProcessCount +"\n");
			out.write("Total Folders Processed: " + FolderProcessCount +"\n");
			out.write("Total Files Skipped: " + FileSkipCount +"\n");
			
		 out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 
	 }
	 
	 
	 // Perform precluding actions to 	 

	 
	 private void setStartTime(){
		 Utilities u = new Utilities();
		 StartTime = u.getTime();
	}
	 
	 private void setEndTime(){
		 Utilities u = new Utilities();
		 EndTime = u.getTime();
	}
	 
	 private void endScript(String URL)
	 {
		 setEndTime();
	//	 WriteCrashStats(URL); // Append the necessary information to the end of the file
	 }
	 
	 
	 private void updateTempFolderCounter(){
		 tempFolderCounter = tempFolderCounter+1;
	 }

	 	
		public WebTable[] getCrashTable(String URL)
		{	
			// test getting specific information about the page
			HttpUnitOptions.setScriptingEnabled(false);	
			WebTable[] tables = null;
		    try {
		    	WebConversation wc = new WebConversation();
		    	WebRequest request = new GetMethodWebRequest( URL );
		    	WebResponse response = wc.getResponse( request );
		    	tables = response.getTables();
		    	
		//    	System.out.println(getNumberOfRelatedBugs(tables));

		    } catch (Exception e) {
			       System.err.println( "Exception: " + e );
			    }
			
			return tables;
		}
		
		
		
		private String getAllCrashMetaStats(String URL){
			String retVal="";
			WebTable[] tables = getCrashTable(URL);
			Scrape_Helper sh = new Scrape_Helper();
			retVal+=URL + "\n";
			retVal+="Last Crash:" + sh.convertInputToSeconds(getTableVal("Last Crash",tables))+"\n";
			retVal+="Install Age:"  + sh.convertInputToSeconds(getTableVal("Install Age",tables))+"\n";
			retVal+="Date Processed:" + getTableVal("Date Processed",tables) + "\n"; 
			retVal+="Uptime:" + getTableVal("Uptime",tables) + "\n";
			retVal+="Install Time:" + getTableVal("Install Time",tables) + "\n";
			retVal+="Crash Reason:" + getTableVal("Crash Reason",tables) + "\n";
			retVal+="Release Channel:" + getTableVal("Release Channel",tables) + "\n";
			retVal+="Version:" + getTableVal("Version",tables) + "\n";
			retVal+="Product:" + getTableVal("Product",tables) + "\n";
			retVal+="OS:" + getTableVal("OS",tables) + "\n";
			retVal+="Crash Reason:" + getTableVal("Crash Reason",tables) + "\n";
		
			
		//	# Related bugs
			//retVal+="Date Processed:" + getTableVal("Daate Processed",tables) + "\n";
					
			return retVal;
		}
		
		
		
		
		
		private String getTableVal(String searchVal, WebTable[] tables){
			int LocationVal=getTableLocation(searchVal, tables);
			String retVal="NaN";
			if (LocationVal !=-1){
				retVal=tables[0].getCellAsText(LocationVal, 1);
			}
			return retVal;
		}
		
		
		private int getTableLocation(String val, WebTable[] tables)
		{
			int retVal=-1;
			for (int i = 0; i < tables[0].getRowCount(); i++) {
				if (tables[0].getCellAsText(i, 0).equals(val)){
					retVal=i;
				}
			}
			return retVal;
		}

		public void collectFiles(String location){
			String outputLocation, allCrashLocation;
			if(location == ""){
				outputLocation = System.getProperty("user.dir") + "/output/";
				allCrashLocation = System.getProperty("user.dir") + "/allCrashes/";
			}
			else{
				outputLocation = location + "/output/";
				allCrashLocation = location + "/allCrashes/";
			}
			Helper h = new Helper();
			List<FileInfo> fileList = new ArrayList<FileInfo>();
			fileList = h.getFolderList(outputLocation);
			try{
				for (int i = 0; i < fileList.size(); i++) {		
					Utilities.copyFolder(new File(outputLocation+fileList.get(i).getCrashName()),new File(allCrashLocation));
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	 
	}
