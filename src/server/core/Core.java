package server.core;

import global.ASingelton;
import global.IServerService;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.xml.stream.XMLStreamException;

import model.project.Phase;
import model.project.Project;
import server.view.View;

/**
 * This class is the core for the server application
 * @author Ammon
 *
 */
public class Core extends ASingelton implements ICoreServer {
	
	private View view;
	private CreateXML createxml;
	
	
	// the server object
	private IServerService server;
	private int serverPort;
	private String rmiUrl;
	
	
	
	@Override
	public int getServerPort() {
		return serverPort;
	}


	@Override
	public void setServerPort(int portNumber) {
		this.serverPort = portNumber;
	}
	
	
	
	@Override
	public boolean isServerRunning() {
		if(rmiUrl == null)
			return false;
		
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
		if(serverPort == 0)
			return;
		
		// build the rmi url
		this.rmiUrl = "rmi://localhost:"+serverPort+"/PROJECT";
		
		try {
			// set up the server 
			server = new ProjectCalculator(this);
			
			// start it
			LocateRegistry.createRegistry(serverPort);
			Naming.rebind(rmiUrl, server);
			System.out.println("Server läuft.");
		} catch (RemoteException | MalformedURLException e) {
			System.err.println(e.getMessage());
		}
	}

	
	@Override
	public void stopServer() {
		if(rmiUrl == null)
			return;
		
		try {
			Naming.unbind(rmiUrl);
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			// ignore the exception
			//e.printStackTrace();
		}
		
		// clean server object
		server = null;
		
		System.out.println("Server gestoppt.");
	}


	@Override
	public String[] getAllProjectNames() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Project calculateProject(Project project) {
		System.out.println();
		return null;
	}
	
	public Phase calculatePhase (Phase phase) {
		return null;
	}
	
	public void writeProject (Project project) throws FileNotFoundException, XMLStreamException{
		
		createxml.setFile(""+project.getProjectName()+".xml");
	    try {
	      createxml.saveConfig(project);
	      createxml.startTagPhasen();
	      createxml.endTagPhasen();
	      createxml.closeXML();
	     
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }


	@Override
	public void saveProject(Project project) throws RemoteException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Project loadProject(String projectName) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	}
	
