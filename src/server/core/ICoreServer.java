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
}
