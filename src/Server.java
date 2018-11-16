
import java.net.*;
import java.io.*;
import java.sql.*;

class Server implements Serializable {
	public static void main(String args[]) throws Exception, NotSerializableException {
		System.out.println("Hi, I am server.");
		// Set up socket
		ServerSocket ss = new ServerSocket(3333);
		Socket s = ss.accept();
		// Set up streams of communication with client
		DataInputStream din = new DataInputStream(s.getInputStream());
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// check database set up
		Testdb t = new Testdb();
		t.check();
		// Set up connectivity with database
		String url = "jdbc:mysql://localhost:3306/Mail", db_pw = "", un = "root", ID = "", pw = "";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, un, db_pw);
		Statement st = con.createStatement();

		String address = "", str2 = "", inbox;
		int action, flag = 0;
		
		while (true) {
			inbox = "";
			System.out.println("Waiting for int....");
			action = din.readInt();
			switch (action) {
			case 1: // received retrieve inbox message from client
				address = din.readUTF();
				String query = "Select sender, body from messages where recepient = \"" + address + "\";";
				ResultSet rs = st.executeQuery(query);
				while (rs.next()) {

					inbox = inbox + rs.getString(1) + " " + rs.getString(2) + "\n";
				}
				System.out.println("Retrieving inbox of " + address);
				dout.writeUTF(inbox);

				break;
			case 2: // received compose message from client
				Mail receivedmail = new Mail();
				receivedmail.from = din.readUTF();
				receivedmail.to = din.readUTF();
				receivedmail.body = din.readUTF();

				System.out.println("Mail received from " + receivedmail.from + " to " + receivedmail.to + " which says "
						+ receivedmail.body);
				query = "insert into messages values (\"" + receivedmail.from + "\",\"" + receivedmail.to + "\",\""
						+ receivedmail.body + "\")";
				st.executeUpdate(query);
				break;

			case 3: // received exit message from client
				System.out.println("Got exit Signal ...");
				flag = 1;
				break;

			case 4: // received check user credentials from client
				ID = din.readUTF();
				pw = din.readUTF();
				query = "SELECT COUNT(*) FROM userCreds WHERE username = \"" + ID + "\" and password = \"" + pw
						+ "\" ;";
				rs = st.executeQuery(query);
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
				System.out.println("Kuch toh random mila");
			}
			if (flag == 1) {
				System.out.println("Exiting..");
				break;
			}
		}
		
		 din.close(); s.close(); ss.close();
			 
	}
}