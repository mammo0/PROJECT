package client.core;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import global.ASingelton;
import global.IServerService;

/**
 * This class is the core for the client application
 * @author Ammon
 *
 */
public class Core extends ASingelton implements ICoreClient {
	
	// the server object
	private IServerService server;
	
	
	
	/**
	 * The constructor
	 */
	public Core(){
		// connect to the server
		try {
			server = (IServerService) Naming.lookup("rmi://localhost:12345/PROJECT");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void startGui() {
		// TODO Auto-generated method stub
	}
}
