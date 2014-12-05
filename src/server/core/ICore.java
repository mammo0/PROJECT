package server.core;

/*
 * Interface for the Core class
 */
public interface ICore {
	/*
	 * Returns if the server is running or not
	 */
	public boolean isServerRunning();
	
	/*
	 * This method starts the server GUI
	 */
	public void startGui();
}
