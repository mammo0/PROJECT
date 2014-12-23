package global;

import java.rmi.Remote;
import java.rmi.RemoteException;

import model.project.Project;


/**
 * This Class contains all Methods which are used 
 * to control the Server
 * 
 * @author ammon
 *
 */
public interface IServerService extends Remote{
	/**
	 * This method returns all names of projects saved on the server
	 * @return all project names
	 * @throws RemoteException 
	 */
	public String[] getAllProjects() throws RemoteException;
	
	/**
	 * This method calculates the costs and people days for a project
	 * @param project the project with all parameters
	 * @return the project object containing the calculated results
	 * @throws RemoteException 
	 */
	public Project calculateProject(Project project) throws RemoteException;
}
