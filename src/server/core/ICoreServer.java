package server.core;

import global.IServerService;

/**
 * Interface for the Core class
 * @author Ammon
 *
 */
public interface ICoreServer extends IServerService {
	/**
	 * Returns if the server is running or not
	 */
	public boolean isServerRunning();
	
	/**
	 * This method starts the server GUI
	 */
	public void startGui();
	
	/**
	 * This method starts the PROJECT server
	 */
	public void startServer();
	
	/**
	 * This method stops the PROJECT server
	 */
	public void stopServer();
	
	/**
	 * Get the server port number
	 * @return the server port
	 */
	public int getServerPort();
	
	/**
	 * Set the port number for the server
	 * @param portNumber
	 */
	public void setServerPort(int portNumber);
	
	/**
	 * Get the directory for the project files
	 * @return the projectPath
	 */
	public String getProjectDirectory();
	
	/**
	 * Set the directory for the project files
	 * @param path
	 */
	public void setProjectDirectory(String path);
	
	/**
	 * Save the server settings to an external config file
	 * @param projectDir the project directory
	 * @param serverPort the server port
	 */
	public void saveSettings(String projectDir, int serverPort);
}
