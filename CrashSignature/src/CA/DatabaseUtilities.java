package CA;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import com.mysql.jdbc.Statement;
// Not sure if I need this or not.

public class DatabaseUtilities {
	 
	    private DatabaseUtilities() throws ClassNotFoundException {
	        Class.forName("com.mysql.jdbc.Driver");
	    }
	    
	    private static DatabaseUtilities INST;
	    
	    public static Connection getNewConnection(String host, String database, String user, String password)
	            throws SQLException, ClassNotFoundException {
	        if (INST == null) {
	            INST = new DatabaseUtilities();
	        }
	        return INST.createConnection(host, database, user, password);
	    }
	 
	    public Connection createConnection(String host, String database, String user, String password) throws SQLException {
	        String url = new String("jdbc:mysql://" + host + "/" + database);
	        return DriverManager.getConnection(url, user, password);
	    }

	    //Test main to check for connectivity the code above could be added directly to Utilities if desired.
	    public static void main(String[]args) throws ClassNotFoundException{
	    	try {
	    		DatabaseUtilities dbu = new DatabaseUtilities();
				Connection con = dbu.getNewConnection("localhost:3306", "mysql", "root", "root");
				java.sql.Statement stmt = con.createStatement();
				stmt.executeUpdate("CREATE DATABASE CrashSignature");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
}

