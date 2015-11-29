/**
 * TCP Client
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient  {
	
	private String host;
	private String username;
	private int port;
	private Socket s;
	private ObjectInputStream is;		
	private ObjectOutputStream os;		


	public TCPClient(String host, int port, String username) {

		this.host = host;
		this.port = port;
		this.username = username;
	}

	
	public static void main(String[] args) {
		int port = 1234;
		String host = "localhost";
		String username = "Anonymous";

		//read the username the user entered
		switch(args.length) {
			case 1: 
				username = args[0];
			case 0:
				break;
			default:
				System.out.println("Usage is: > java Client [username]");
			return;
		}

		//Create the TCPClient object
		TCPClient c = new TCPClient(host, port, username);

		//Call the go function
		c.go();

		

		while(true) {
			System.out.print("> ");
			//read client's input
			Scanner incoming = new Scanner(System.in);
			String msg = incoming.nextLine();
			
			//if input is LOGOUT, send LOGOUT MessageType object to server
			if(msg.equals("LOGOUT")) {
				c.sendMessage(new MessageType(MessageType.LOGOUT, ""));
				break;
			}
			else {
				//else send the MESSAGE MessageType object to server
				c.sendMessage(new MessageType(MessageType.MESSAGE, msg));
			}
		}
		c.close();	
	}
	
	public boolean go() {
 
		try
		{
			//Create socket
			s = new Socket(host, port);
			//Create I/O stream
			is  = new ObjectInputStream(s.getInputStream());
			os = new ObjectOutputStream(s.getOutputStream());
		}
		catch (IOException eIO) {
			
			System.out.println("Creation of input or output streams failed " + eIO);
			return false;
		}

		//Create the ServerThread object to listen from the server
		ServerThread serverRunnable = new ServerThread();
		Thread t = new Thread(serverRunnable);
		t.start();
		
		try
		{
			//write username into output stream
			os.writeObject(username);
		}
		catch (IOException eIO) {
			System.out.println("Login failed: " + eIO);
			close();
			return false;
		}
		return true;
	}

	//a method to send message to server
	void sendMessage(MessageType msg) {
		try {
			os.writeObject(msg);
		}
		catch(IOException e) {
			System.out.println("Sending messages to server failed: " + e);
		}
	}

	//a method to close all the connection
	private void close() {
		try { 
			if(is != null) {is.close();
			
			}
		}
		catch(Exception e) {
			
		} 
		try {
			if(os != null) {
				os.close();
			
			}
		}
		catch(Exception e) {
			
		} 
        try{
			if(s != null) {
				s.close();
			
			}
		}
		catch(Exception e) {
			
		} 
			
	}


	//a thread for clients to listen from the server
	class ServerThread implements Runnable {

		public void run() {
			while(true) {
				try {
					//get message in the input stream
					String msg = (String) is.readObject();
					System.out.println(msg);
					System.out.print("> ");
					}
				catch(IOException e) {
					System.out.println("Connection has closed: " + e);
					//e.printStackTrace();
					break;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
				}
			}
		}
	} 
}
