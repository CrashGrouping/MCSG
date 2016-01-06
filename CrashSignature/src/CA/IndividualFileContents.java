package CA;

public class IndividualFileContents {
	
	private String[] Values;
	private String InputValues;
	private String FilePath;
	
	//THIS NEEDS TO BE UNIT TESTED
	public IndividualFileContents(String filepath, String inputValues) {
		InputValues = inputValues;
		FilePath = filepath;
		
		// make sure that each input value is added to the array list
		//Values = InputValues.split("\\^\\*\\^");
		Values = InputValues.split("@!@");
	}

	
	public String getEntireValueSet()
	{
		return InputValues;
	}

	public String[] getValues() {
		return Values;
	}

	public void setValues(String[] values) {
		Values = values;
	}



	public void setFilePath(String filePath) {
		FilePath = filePath;
	}



	public String getFilePath() {
		return FilePath;
	}
	
	
	
	
}
