/**
 * TCP Server
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
  
public class TCPServer {
	private int port;	
	private ArrayList<ConnectionHandler> cal;
	private SimpleDateFormat timeFormat;
	private static double count_1 = 0.0;
	private static double count_2 = 0.0;
	private static double count_3 = 0.0;
	private static double count_4 = 0.0;
	private static double count_5 = 0.0;
	private static int connectionID;
	
	public TCPServer() {
		port = 1234;
		timeFormat = new SimpleDateFormat("HH:mm:ss");
		cal = new ArrayList<ConnectionHandler>();
	}
	
	public static void main(String[] args) {
		//Create the TCPServer object
		TCPServer server = new TCPServer();
		server.go();		
	}
	
	public void go() {
		try 
		{
			//Open the Server Socket
			ServerSocket ss = new ServerSocket(port);
			while(true) 
			{
				
				System.out.println("Port " + port + " is waiting clients.");
				//If connect, Create ConnectionHandler object and add it to ArrayList
				Socket s = ss.accept();  	
			    ConnectionHandler chandler = new ConnectionHandler(s);
			    Thread t = new Thread(chandler);
				cal.add(chandler);
				t.start();
			}
		
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//A method to remove thread from the ArrayList
	synchronized void remove(int id) {
		for(int i = 0; i < cal.size(); ++i) {
			ConnectionHandler cr = cal.get(i);
			if(cr.id == id) {
				cal.remove(i);
				return;
			}
		}
	}

	//The ConnectionHandler class for multithreading
	class ConnectionHandler implements Runnable {

		protected Socket s;
		protected ObjectInputStream is;
		protected ObjectOutputStream os;
		protected int id;
		protected String username;
		protected MessageType cm;
		protected File directory;
		protected int Tcount_1;
		protected int Tcount_2;
		protected int Tcount_3;
		protected int Tcount_4;
		protected int Tcount_5;

		public ConnectionHandler(Socket s) {
			id = ++connectionID;
			Tcount_1 = 0;
			Tcount_2 = 0;
			Tcount_3 = 0;
			Tcount_4 = 0;
			Tcount_5 = 0;
			this.s = s;
		}
		

		//overwrite the run method
		public void run() {
			//print out the time of login
			String time = timeFormat.format(new Date());
			
			try
			{
				//create I/O stream for the thread
				os = new ObjectOutputStream(s.getOutputStream());
				is  = new ObjectInputStream(s.getInputStream());
				//read the username from the input stream
				username = (String) is.readObject();
				System.out.println(time + " " + username + " just connected.");
				//open the test.txt file and write it into output stream
				try{
					String pathname = "test.txt"; 
					directory = new File("files/");
					File filename = new File(directory,pathname);   
					if ( (! filename.exists()) || filename.isDirectory() ) {
						os.writeObject("ERROR");
					}
					else {
						BufferedReader br = new BufferedReader(new FileReader(filename));  
						while(true){
							String line = br.readLine();  
							if (line == null){
					               break;}
							os.writeObject(line+"\n");
							
						}
					}
				}catch (Exception e) {  

		            e.printStackTrace();  

		            }
			}
			catch (IOException e) {
				System.out.println("Exception creating new Input/output Streams: " + e);
				return;
			}
			catch (ClassNotFoundException e) {
			}
			boolean keepGoing = true;
			
			while(keepGoing) {
				try {
					//read the MessageType object from input stream
					cm = (MessageType) is.readObject();
				}
				catch (IOException e) {
					System.out.println(username + " Reading stream failed: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}
				
				//get message content
				String message = cm.getMessage();
				
				//get message type
				switch(cm.getType()) {

				//If it's a message, get the number of the question first
				//then judge whether it's right or wrong
				//if right, sent message to all connected clients
				case MessageType.MESSAGE:
					if(message.charAt(0) == '1'){
						if (Tcount_1 == 0){
							if (message.charAt(1) == 'A'){
								count_1++;
								Tcount_1++;
								String msg1 = time + " " + username + " " + "gave the correct answer of question 1";
								System.out.print(msg1 + "\n");
								for(int i = cal.size(); --i >= 0;) {
									ConnectionHandler cr = cal.get(i);
									cr.sendMsg(msg1);
									}
								String msg2 = time + " " + "now " + (int)count_1 + " users gave the correct answer to question 1";
								System.out.print(msg2 + "\n");
								for(int i = cal.size(); --i >= 0;) {
									ConnectionHandler cr = cal.get(i);
									cr.sendMsg(msg2);
								}
							}else{
								try {
									os.writeObject("Wrong answer. Please answer again!");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}else{
							try {
								os.writeObject("You have answered this question once!");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					if(message.charAt(0) == '2'){
						if (Tcount_2 == 0){
							if (message.charAt(1) == 'B'){
								count_2++;
								Tcount_2++;
								String msg1 = time + " " + username + " " + "gave the correct answer to question 2";
								System.out.print(msg1 + "\n");
								for(int i = cal.size(); --i >= 0;) {
									ConnectionHandler cr = cal.get(i);
									cr.sendMsg(msg1);
								}
								String msg2 = time + " " + "now " + (int)count_2 + " users gave the correct answer to question 2";
								System.out.print(msg2 + "\n");
								for(int i = cal.size(); --i >= 0;) {
									ConnectionHandler cr = cal.get(i);
									cr.sendMsg(msg2);
								}
							}else{
								try {
									os.writeObject("Wrong answer. Please answer again!");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}else{
							try {
								os.writeObject("You have answered this question once!");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					if(message.charAt(0) == '3'){
						if (Tcount_3 == 0){
							if (message.charAt(1) == 'C'){
								count_3++;
								Tcount_3++;
								String msg1 = time + " " + username + " " + "gave the correct answer to question 3";
								System.out.print(msg1 + "\n");
								for(int i = cal.size(); --i >= 0;) {
									ConnectionHandler cr = cal.get(i);
									cr.sendMsg(msg1);
								}
								String msg2 = time + " " + "now " + (int)count_3 + " users gave the correct answer to question 3";
								System.out.print(msg2 + "\n");
								for(int i = cal.size(); --i >= 0;) {
									ConnectionHandler cr = cal.get(i);
									cr.sendMsg(msg2);
								}
							}else{
								try {
									os.writeObject("Wrong answer. Please answer again!");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}else{
							try {
								os.writeObject("You have answered this question once!");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					if(message.charAt(0) == '4'){
						if (Tcount_4 == 0){
							if (message.charAt(1) == 'D'){
								count_4++;
								Tcount_4++;
								String msg1 = time + " " + username + " " + "gave the correct answer to question 4";
								System.out.print(msg1 + "\n");
								for(int i = cal.size(); --i >= 0;) {
									ConnectionHandler cr = cal.get(i);
									cr.sendMsg(msg1);

								}
								String msg2 = time + " " + "now " + (int)count_4 + " users gave the correct answer to question 4";
								System.out.print(msg2 + "\n");
								for(int i = cal.size(); --i >= 0;) {
									ConnectionHandler cr = cal.get(i);
									cr.sendMsg(msg2);

								}
							}else{
								try {
									os.writeObject("Wrong answer. Please answer again!");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}else{
							try {
								os.writeObject("You have answered this question once!");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					if(message.charAt(0) == '5'){
						if (Tcount_5 == 0){
							if (message.charAt(1) == 'D'){
								count_5++;
								Tcount_5++;
								String msg1 = time + " " + username + " " + "gave the correct answer to question 5";
								System.out.print(msg1 + "\n");
								for(int i = cal.size(); --i >= 0;) {
									ConnectionHandler cr = cal.get(i);
									cr.sendMsg(msg1);

								}
								String msg2 = time + " " + "now " + (int)count_5 + " users gave the correct answer to question 5";
								System.out.print(msg2 + "\n");
								for(int i = cal.size(); --i >= 0;) {
									ConnectionHandler cr = cal.get(i);
									cr.sendMsg(msg2);
								}
							}else{
								try {
									os.writeObject("Wrong answer. Please answer again!");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}else{
							try {
								os.writeObject("You have answered this question once!");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					//calculate the percentage of number of correct answers
					//send results to all clients
					NumberFormat nt = NumberFormat.getPercentInstance();
					nt.setMinimumFractionDigits(2);
					double per_1 = count_1/cal.size();
					String msg3 = time + " " + "now " + nt.format(per_1) + " of users gave the correct answer to question 1";
					System.out.print(msg3 + "\n");
					for(int i = cal.size(); --i >= 0;) {
						ConnectionHandler cr = cal.get(i);
						cr.sendMsg(msg3);
					}
					double per_2 = count_2/cal.size();
					String msg4 = time + " " + "now " + nt.format(per_2) + " of users gave the correct answer to question 2";
					System.out.print(msg4 + "\n");
					for(int i = cal.size(); --i >= 0;) {
						ConnectionHandler cr = cal.get(i);
						cr.sendMsg(msg4);
					}
					double per_3 = count_3/cal.size();
					String msg5 = time + " " + "now " + nt.format(per_3) + " of users gave the correct answer to question 3";
					System.out.print(msg5 + "\n");
					for(int i = cal.size(); --i >= 0;) {
						ConnectionHandler cr = cal.get(i);
						cr.sendMsg(msg5);
					}
					double per_4 = count_4/cal.size();
					String msg6 = time + " " + "now " + nt.format(per_4) + " of users gave the correct answer to question 4";
					System.out.print(msg6 + "\n");
					for(int i = cal.size(); --i >= 0;) {
						ConnectionHandler cr = cal.get(i);
						cr.sendMsg(msg6);
					}
					double per_5 = count_5/cal.size();
					String msg7 = time + " " + "now " + nt.format(per_5) + " of users gave the correct answer to question 5";
					System.out.print(msg7 + "\n");
					for(int i = cal.size(); --i >= 0;) {
						ConnectionHandler cr = cal.get(i);
						cr.sendMsg(msg7);
					}
					try {
						os.writeObject("Please enter the question number and answer, e.g. 1A" + "\n");
						os.writeObject("If you want to logout, please enter 'LOGOUT'." + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				
				//if it's a logout request
				//counter minus one if the client gave the correct answer to the question 
				//then remove the thread from the arraylist and close the connection	
				case MessageType.LOGOUT:
					System.out.println(username + " disconnected with a LOGOUT message.");
					if(Tcount_1 > 0){
						count_1--;
					}
					if(Tcount_2 > 0){
						count_2--;
					}
					if(Tcount_3 > 0){
						count_3--;
					}
					if(Tcount_4 > 0){
						count_4--;
					}
					if(Tcount_5 > 0){
						count_5--;
					}
					keepGoing = false;
					break;
				}
			}
			remove(id);
			close();
		}
		
		//a method to close the connection
		private void close() {
			try {
				if(os != null) os.close();
			}
			catch(Exception e) {}
			try {
				if(is != null) is.close();
			}
			catch(Exception e) {};
			try {
				if(s != null) s.close();
			}
			catch (Exception e) {}
		}

		//a method to send message to clients
		private boolean sendMsg(String msg) {
			try {
				os.writeObject(msg);
			}
			catch(IOException e) {
				System.out.println("Send message to " + username + " failed.");
				System.out.println(e.toString());
			}
			if(!s.isConnected()) {
				close();
				return false;
			}
			return true;
		}
		
		
	}		
}

