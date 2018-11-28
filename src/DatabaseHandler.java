
import java.sql.*;

public class DatabaseHandler {
	// JDBC driver name and database URL
	final String JDBC_DRIVER;
	final String DB_URL;

	// Database credentials
	final String USER;
	final String PASS;
	
	Connection conn;
	public Statement stmt;
	
	//Constructor
	public DatabaseHandler(String driver, String url, String username, String pw) {
		this.JDBC_DRIVER = driver;
		this.DB_URL = url;
		this.USER = username;
		this.PASS = pw;
	}
	
	/*Function to establish a connection with database*/
	public void createConnection() {
		try {
			//Register JDBC driver
			Class.forName(JDBC_DRIVER);

			//Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			//Execute a query
			System.out.println("Creating database...");
			stmt = conn.createStatement();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}

	}
	
	/*Function to close the connection*/
	public void closeConnection() {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException se2) {
		}
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}

	}

	/*Function to execute queries necessary for Database set-up*/
	public void executedefaultqueries() {
		try {
			String sql = "CREATE DATABASE IF NOT EXISTS Mail";
			String table1 = "CREATE TABLE If not exists Mail.messages ( sender varchar(15) DEFAULT NULL,recepient varchar(15) DEFAULT NULL, body varchar(25) DEFAULT NULL)";
			String table2 = "CREATE TABLE if not exists Mail.userCreds (username varchar(15) DEFAULT NULL,password varchar(15) DEFAULT NULL )";
			stmt.executeUpdate(sql);
			stmt.executeUpdate(table1);
			stmt.executeUpdate(table2);
			String ins = "insert ignore into Mail.userCreds values ('karthik', 'karthik');";
			stmt.executeUpdate(ins);
			ins = "insert ignore into Mail.userCreds values ('bhargavi', 'bhargavi');";
			stmt.executeUpdate(ins);
			ins = "insert ignore into Mail.userCreds values ('cotiviti', 'cotiviti');";
			stmt.executeUpdate(ins);
			System.out.println("Database is set up successfully.....");
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}

	}
}