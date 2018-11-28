import java.net.*;
import java.io.*;

class Server {
	public static void main(String args[]) throws Exception {
		System.out.println("Hi, I am server.");
		
		// Set up socket : server listens on 3333
		ServerSocket ss = new ServerSocket(3333);	

		while (true) {
			Socket s = null;
			try {
				s = ss.accept();
				System.out.println("A new client is connected : " + s);
				// Set up streams of communication with client
				DataInputStream din = new DataInputStream(s.getInputStream());
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());

				System.out.println("Assigning new thread for this client");

				// create a new thread object
				Thread t = new ClientHandler(s, din, dout);

				// Invoking the start() method
				t.start();

			} catch (Exception e) {
				s.close();				
				e.printStackTrace();
			}		
			
		}
	}
}