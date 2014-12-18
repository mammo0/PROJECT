package server.core;

import java.io.IOException;
import java.net.ServerSocket;


/*This Class contains all Methods which are used 
 * to control the Server
 */

public class ServerService extends Thread {
	
	ServerSocket serverSocket;
	
	
	/*This Method starts the Server with creating a Server Socket
	 * 
	 */
	public void startServer(){	
	try {
		serverSocket = new ServerSocket(12345);
		
		System.out.println("Server ist gestartet.");
		
		//while(true){
			//Socket s = serverSocket.accept();
		//}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}
	
	/*This Method stops the Server with closing the Server Socket
	 * 
	 */
	public void  stopServer(){
		
		try {
			serverSocket.close();
			System.out.println("Server wurde beendet.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
