public class TestDB {
	public static void main(String args[]) throws Exception{
		String url = "jdbc:mysql://localhost:3306/", driver = "com.mysql.jdbc.Driver", db_pw = "", un = "root";
		DatabaseHandler db = new DatabaseHandler(driver, url, un, db_pw);
		db.createConnection();
		db.executedefaultqueries();
		db.closeConnection();		
	}
}
