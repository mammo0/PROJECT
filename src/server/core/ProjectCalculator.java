package server.core;

import global.IServerService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
	public String[] getAllProjects() {
		return core.getAllProjects();
	}

	@Override
	public Project calculateProject(Project project) {
		return core.calculateProject(project);
	}
}
