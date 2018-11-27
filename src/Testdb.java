import java.sql.*;

public class Testdb {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "";
   
   
   public static void check() {
   Connection conn = null;
   Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      //STEP 4: Execute a query
      System.out.println("Creating database...");
      stmt = conn.createStatement();
      
      String sql = "CREATE DATABASE IF NOT EXISTS Mail";
      String table1 = "CREATE TABLE If not exists Mail.`messages` ( `sender` varchar(15) DEFAULT NULL,`recepient` varchar(15) DEFAULT NULL, `body` varchar(25) DEFAULT NULL)";
      String table2 = "CREATE TABLE if not exists Mail.`userCreds` (`username` varchar(15) DEFAULT NULL,`password` varchar(15) DEFAULT NULL )";
      stmt.executeUpdate(sql);
      stmt.executeUpdate(sql);
      stmt.executeUpdate(sql);
      //insertdefaultvalues();
      String ins = "insert ignore into Mail.userCreds values ('karthik', 'karthik');";	   
      stmt.executeUpdate(ins);
	   ins = "insert ignore into Mail.userCreds values ('bhargavi', 'bhargavi');";
	   stmt.executeUpdate(ins);
	   ins = "insert ignore into Mail.userCreds values ('cotiviti', 'cotiviti');";
	   stmt.executeUpdate(ins);
      System.out.println("Database is set up successfully.....");
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }
   }
   
}
}