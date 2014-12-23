package server.core;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import global.ASingelton;
import global.IServerService;
import model.project.Project;
import server.view.View;

/**
 * This class is the core for the server application
 * @author Ammon
 *
 */
public class Core extends ASingelton implements ICoreServer {
	
	private View view;
	
	// the server object
	private IServerService server;
	private int serverPort;
	private String rmiUrl;
	
	
	/**
	 * The constructor
	 */
	public Core(){
		// set up the server parameter
		this.serverPort = 4711;
		this.rmiUrl = "rmi://localhost:"+serverPort+"/PROJECT";
	}
	
	
	
	@Override
	public boolean isServerRunning() {
		try {
			if(Naming.lookup(rmiUrl) != null)
				return true;
			else
				return false;
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			return false;
		}
	}

	
	@Override
	public void startGui() {
		view = new View(this);
		view.showFrame();
	}

	
	@Override
	public void startServer() {
		try {
			// set up the server 
			server = new ProjectCalculator(this);
			
			// start it
			LocateRegistry.createRegistry(serverPort);
			Naming.rebind(rmiUrl, server);
			System.out.println("Server läuft.");
		} catch (RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void stopServer() {
		try {
			Naming.unbind(rmiUrl);
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			e.printStackTrace();
		}
		
		// clean server object
		server = null;
		
		System.out.println("Server gestoppt.");
	}


	@Override
	public String[] getAllProjects() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Project calculateProject(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

}