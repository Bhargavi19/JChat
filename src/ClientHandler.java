import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;

class ClientHandler extends Thread {
	DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
	DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
	final DataInputStream din;
	final DataOutputStream dout;
	final Socket s;
	

	// Constructor
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
		this.s = s;
		this.din = dis;
		this.dout = dos;
		
	}

	@Override
	public void run() {
		String url = "jdbc:mysql://localhost:3306/Mail", driver = "com.mysql.jdbc.Driver", db_pw = "", un = "root";
		String address = "", inbox;
		int action, flag = 0;
		DatabaseHandler db = new DatabaseHandler(driver, url, un, db_pw);
		db.createConnection();
		
		while (true) {
			try {

				inbox = "";
				System.out.println("Waiting for int....");
				action = din.readInt();
				switch (action) {
				case 1: // received retrieve inbox message from client
					address = din.readUTF();
					String query = "Select sender, body from messages where recepient = \"" + address + "\";";
					ResultSet rs = db.stmt.executeQuery(query);
					while (rs.next()) {

						inbox = inbox + rs.getString(1) + " : " + rs.getString(2) + "\n";
					}
					System.out.println("Retrieving inbox of " + address);
					dout.writeUTF(inbox);

					break;
				case 2: // received compose message from client
					Mail receivedmail = new Mail();
					receivedmail.from = din.readUTF();
					receivedmail.to = din.readUTF();
					receivedmail.body = din.readUTF();

					System.out.println("Mail received from " + receivedmail.from + " to " + receivedmail.to
							+ " which says " + receivedmail.body);
					query = "insert into messages values (\"" + receivedmail.from + "\",\"" + receivedmail.to + "\",\""
							+ receivedmail.body + "\")";
					db.stmt.executeUpdate(query);
					break;

				case 3: // received exit message from client
					System.out.println("Got exit Signal ...");
					flag = 1;
					break;

				case 4: // received check user credentials from client
					System.out.println("Checking for credentials....");
					String receivedID, receivedPw;
					receivedID = din.readUTF();
					receivedPw = din.readUTF();
					query = "SELECT COUNT(*) FROM userCreds WHERE username = \"" + receivedID + "\" and password = \""
							+ receivedPw + "\" ;";
					rs = db.stmt.executeQuery(query);
					rs.next();
					if (rs.getString(1).equals("0")) {
						System.out.println("Login Failed");
						dout.writeInt(0);
					} else {
						System.out.println("Login Success");
						dout.writeInt(1);
					}
					break;

				default:
					System.out.println("Invalid Input...");
				}
				if (flag == 1) {
					System.out.println("Exiting..");
					break;
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException se) {
				// Handle errors for JDBC
				se.printStackTrace();
			}
		}
		try {
			// closing resources
			db.closeConnection();
			this.din.close();
			this.dout.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
