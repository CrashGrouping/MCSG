package CA;
// These methods may be shared across the classes.


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Utilities {

	public Utilities() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	
	
	// Write information to a log file.
	//public void WriteLogFile(String fileName, String Location, String Contents, boolean isAppend)
	//{
		 
	//}

	public void WriteLogFile(String fileName, String Location,String Contents, boolean isAppend) {
		FileWriter out;
		try {
			out = new FileWriter(Location+"/"+ fileName, isAppend);
			out.write(Contents);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}	
	
	 public String getTime(){
		 Calendar cal = Calendar.getInstance();
		 SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		 return sdf.format(cal.getTime());
	 }
	 
	 public String getDate()
	 {
		 // DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd"); 
		 Date date = new Date();
		 //System.out.println(dateFormat.format(date)); 
		 return dateFormat.format(date);
	 }
	 
	 
	 public String getDateTime()
	 {
		 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		 Date date = new Date();
		 //System.out.println(dateFormat.format(date)); 
		 return dateFormat.format(date);
	 }
	 
	 public static void copyFolder(File src, File dest)
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
		    			//Skips URL/CompareReport which each output contains
		    			if(file.endsWith("URL.txt") || file.endsWith("CompareReport.txt")){}
		    			else{
			    		   //construct the src and dest file structure
			    		   File srcFile = new File(src, file);
			    		   File destFile = new File(dest, file);
			    		   //recursive copy
			    		   copyFolder(srcFile,destFile);
		    			}
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
	 /*
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
	*/

}
