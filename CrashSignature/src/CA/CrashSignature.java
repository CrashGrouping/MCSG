package CA;

public class CrashSignature {
	
	//If you want a different value for your scraped data, set this variable
	//If left as empty string will default to *ProjectDir*/output/
	public final static String HardCodeLocation = "";
	
	public static void main(String[] args) {
		Scrape s = new Scrape();
		CrashSuper cs = new CrashSuper();
		s.Run(HardCodeLocation);
		//This line creates a copy of all outputs into a single folder
		s.collectFiles(HardCodeLocation);
		cs.Run(HardCodeLocation);
	}
}
