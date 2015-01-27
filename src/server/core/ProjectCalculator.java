package server.core;

import global.IServerService;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import model.project.Project;

/**
 * The method of this class are called by the client.
 * All methods should be forwarded to the core.
 * 
 * @author ammon
 *
 */
@SuppressWarnings("serial")
public class ProjectCalculator extends UnicastRemoteObject implements IServerService {

	// the core object
	private ICoreServer core;
	
	
	
	/**
	 * The constructor
	 * @param core the core object
	 * @throws RemoteException
	 */
	public ProjectCalculator(ICoreServer core) throws RemoteException {
		super();
		
		// add the core object
		this.core = core;
	}

	
	
	@Override
	public ArrayList<String> getAllProjectNames() throws RemoteException {
		return core.getAllProjectNames();
	}

	@Override
	public Project calculateProject(Project project) throws RemoteException {
		return core.calculateProject(project);
	}

	@Override
	public void saveProject(Project project) throws RemoteException {
		core.saveProject(project);
	}
	
	@Override
	public Project loadProject(String projectName) throws RemoteException {
		return core.loadProject(projectName);
	}

	@Override
	public void deleteProject(String projectName) throws RemoteException {
		core.deleteProject(projectName);
	}



	@Override
	public File getProjectCSV(Project project) throws RemoteException {
		return core.getProjectCSV(project);
	}
}
