package CA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileCleaning {
	// private final static String CrashSuperLocation= "C:/SignatureOutput/noscan/100/Perfect/";

	
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		FileCleaning fc = new FileCleaning();
		fc.Run("C:/SignatureOutput/noscan/100/");
	}
	
	
	public void Run(String CrashSuperLocation) throws IOException, InterruptedException
	{
		List<String> ItemsToRemove=new ArrayList<String>();
	
	// Cycle through all of the folders in the main directory
		CrashSuper cs = new CrashSuper();
		Utilities u = new Utilities();
		Helper h = new Helper();
		List<FileInfo> fileList = new ArrayList<FileInfo>();
		fileList = cs.getFileList(CrashSuperLocation);
		File folder = new File(CrashSuperLocation);
		File[] listOfFiles = folder.listFiles();
	
		for (int i = 0; i < listOfFiles.length; i++) {	  
			if(h.isExamineFolder(listOfFiles[i])==true){
				
				List<FileInfo> fileList2 = new ArrayList<FileInfo>();
				fileList2 = cs.getFileList(CrashSuperLocation+listOfFiles[i].getName()+"/");
				File folder2 = new File(CrashSuperLocation+listOfFiles[i].getName()+"/");
				File[] listOfFiles2 = folder2.listFiles();
	
				// Cycle through each folder and reference each file in the folder
				for (int ii = 0; ii < listOfFiles2.length; ii++) {				
					if(h.isFileContainMissingInfoLines(CrashSuperLocation+listOfFiles[i].getName()+"/"+listOfFiles2[ii].getName())){
						ItemsToRemove.add(CrashSuperLocation+listOfFiles[i].getName()+"/"+listOfFiles2[ii].getName());
					}
				}				
	    	 }
	    }	
	

	// Now cycle through the array and remove the items
		for (int x = 0; x < ItemsToRemove.size(); x++) {	
			
			System.gc();
			Thread.sleep(1000);
			DeleteFile(ItemsToRemove.get(x).toString());
			System.gc();
			Thread.sleep(1000);
			System.gc();
		}
	 		

		
		/*
		// Do the following for cleanup up purposes during testing.
		File parentCopy = new File("C:/SignatureOutput/noscan/100/Perfect/noscantest/");
		File childCopy1 = new File("C:/SignatureOutput/noscan/100/Perfect/lala/");
		File childCopy2 = new File("C:/SignatureOutput/noscan/100/Perfect/lala2/");

		copyFolder(parentCopy, childCopy1);
		copyFolder(parentCopy, childCopy2);
		*/
		
		
		// Output the results 
		System.out.println(getFileCleaningResults(ItemsToRemove.size()));
		u.WriteLogFile("FileCleanReport.txt",CrashSuperLocation,u.getDateTime()+getFileCleaningResults(ItemsToRemove.size()), false);
		
		
	}
	
	private String getFileCleaningResults(int totalFilesRemoved)
	{
		String retVal="";
		retVal+="\n******* Clean Values **********\n";
		retVal+="Total Files Removed: "+ totalFilesRemoved  +"\n";
		
		return retVal;
	}
	
	

	
	public void DeleteFile(String fileName){
		System.out.print("Deleting file:" + fileName);
		System.gc();
		File currentFile = new File(fileName);
		System.gc();
		currentFile.setWritable(true);
		System.gc();
		 boolean ok = currentFile.delete();
		 System.gc();
		    if(ok == false){
		    	System.gc();
		    	System.out.println("Failed to remove " + currentFile.getAbsolutePath());
		        return;
		    }else{
		    	System.out.println("-- Success ");
		    }
		//    currentFile = null;
		    System.gc();
	}
	
	
	// This will be used for unit testing
	// Copy contents of one directory to another
	
	private void CopyFiles(String frmLocation, String toLocation)
	{
		try{
			  File f1 = new File(frmLocation);
			  File f2 = new File(toLocation);
			  InputStream in = new FileInputStream(f1);
			  
			  //For Append the file.
			//  OutputStream out = new FileOutputStream(f2,true);

			  //For Overwrite the file.
			  OutputStream out = new FileOutputStream(f2);

			  byte[] buf = new byte[1024];
			  int len;
			  while ((len = in.read(buf)) > 0){
				  out.write(buf, 0, len);
			  }
			  in.close();
			  out.close();
			  System.out.println("File copied.");
			  }
			  catch(FileNotFoundException ex){
				  System.out.println(ex.getMessage() + " in the specified directory.");
				  System.exit(0);
			  }
			  catch(IOException e){
				  System.out.println(e.getMessage());  
			  }
		}	
	
	public void copyFolder(File src, File dest)
	throws IOException{

	if(src.isDirectory()){

		//if directory not exists, create it
		if(!dest.exists()){
		   dest.mkdir();
		   System.out.println("Directory copied from " 
                          + src + "  to " + dest);
		}

		//list all the directory contents
		String files[] = src.list();

		for (String file : files) {
		   //construct the src and dest file structure
		   File srcFile = new File(src, file);
		   File destFile = new File(dest, file);
		   //recursive copy
		   copyFolder(srcFile,destFile);
		}

	}else{
		//if file, then copy it
		//Use bytes stream to support all file types
		InputStream in = new FileInputStream(src);
	        OutputStream out = new FileOutputStream(dest); 

	        byte[] buffer = new byte[1024];

	        int length;
	        //copy the file content in bytes 
	        while ((length = in.read(buffer)) > 0){
	    	   out.write(buffer, 0, length);
	        }

	        in.close();
	        out.close();
	        System.out.println("File copied from " + src + " to " + dest);
	}
}


}
