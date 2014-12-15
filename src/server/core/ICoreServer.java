package server.core;

/**
 * Interface for the Core class
 * @author Ammon
 *
 */
public interface ICoreServer {
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
}
