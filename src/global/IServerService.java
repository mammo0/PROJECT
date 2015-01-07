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
	public String[] getAllProjectNames() throws RemoteException;
	
	/**
	 * This method calculates the costs and people days for a project
	 * @param project the project with all parameters
	 * @return the project object containing the calculated results
	 * @throws RemoteException 
	 */
	public Project calculateProject(Project project) throws RemoteException;
	
	/**
	 * This method saves the project to a XML file
	 * @param project the project to be saved
	 * @throws RemoteException
	 */
	public void saveProject(Project project) throws RemoteException;
	
	/**
	 * This method loads a project from a XML file
	 * @param projectName the name of the project
	 * @return the project
	 * @throws RemoteException
	 */
	public Project loadProject(String projectName) throws RemoteException;
}
