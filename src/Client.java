import java.net.*;
import java.io.*;
import java.util.*;

public class Client {

	public static void main(String args[]) throws Exception, NotSerializableException {
		Socket s = new Socket("localhost", 3333);
		DataInputStream din = new DataInputStream(s.getInputStream());
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Scanner in = new Scanner(System.in);
		int loggedin = 0;
		System.out.println("Hello");
		String uname = "", pw = "", inbox = "";
		int choice;

		while (true) {
			// authenticating login credentials if not logged in
			if (loggedin == 0) {
				System.out.println("User : ");
				uname = br.readLine();
				if (uname.equals("exit")) {
					// 3 for exit in server
					dout.writeInt(3);
					break;
				}
				System.out.println("Password : ");
				pw = br.readLine();
				dout.writeInt(4);
				dout.writeUTF(uname);
				dout.writeUTF(pw);
				if (din.readInt() == 1) {
					loggedin = 1;
				} else {
					System.out.println("Login not successful! Try again!");
				}
			} else {
				System.out.println("Select your option : \n1.Inbox \n2.Compose Mail \n3.Logout");
				choice = in.nextInt();
				if (choice != 3)
					dout.writeInt(choice);
				switch (choice) {
				case 1:
					dout.writeUTF(uname);
					dout.flush();
					inbox = din.readUTF();
					System.out.println(inbox);

					break;
				case 2:
					Mail newMail = new Mail();
					newMail.from = uname;
					System.out.println("To : ");
					newMail.to = br.readLine();
					System.out.println("Body : ");
					newMail.body = br.readLine();
					dout.writeUTF(newMail.from);
					dout.writeUTF(newMail.to);
					dout.writeUTF(newMail.body);
					break;
				case 3:
					loggedin = 0;
					continue;
				default:
					System.out.println("Invalid choice");
				}
			}
		}

		dout.close();
		s.close();
	}
}
