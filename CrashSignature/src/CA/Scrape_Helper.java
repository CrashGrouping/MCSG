package CA;


public class Scrape_Helper {

	
	
	
	// Convert the text of a box to seconds
	//t
	public int convertInputToSeconds(String input)
	{
		double retVal=0;
		double longValue = getOnlyNumerics(input);
		
		if (input.toLowerCase().contains("month")){
			retVal= longValue * 2592000;
		}else if (input.toLowerCase().contains("week")){	
			retVal= longValue * 604800;
		}else if (input.toLowerCase().contains("day")){
			retVal= longValue * 86400;
		}else if (input.toLowerCase().contains("hour")){
			retVal= longValue * 3600;
		}else if (input.toLowerCase().contains("minute")){
			retVal= longValue * 60;
		}else if (input.toLowerCase().contains("seconds")){
			retVal= longValue * 1;
		}else{
			retVal=0;
		}
			
		// seconds in a month = 2,592,000
		// seconds in a week = 604,800
		//seconds in a day =  86,400 
		//seconds in an hour = 3,600
		
	
		
		
		return (int)retVal;
	}
	
	
	public double getOnlyNumerics(String input)
	{
		String retVal;
	    char[] allowed = "0123456789.".toCharArray();
	    char[] charArray = input.toString().toCharArray();
	    StringBuilder result = new StringBuilder();
	    for (char c : charArray)
	    {
	        for (char a : allowed)
	        {
	            if(c==a) result.append(a);
	        }
	    }

	    retVal = result.toString();
	   
	    if ((retVal.length() >0) && (!retVal.equals("0"))){
	 	    retVal = result.toString();
	 	}else{
	 		retVal="0";
	 	}
	
	    while(retVal.substring(retVal.length()-1, retVal.length()).equals(".")){
	    	retVal = retVal.substring(0, retVal.length()-1);
	    }

	    return  Double.valueOf(retVal);
	}
		
	
}
