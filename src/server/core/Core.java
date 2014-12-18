package server.core;

import global.ASingelton;
import server.view.View;

/**
 * This class is the core for the server application
 * @author Ammon
 *
 */
public class Core extends ASingelton implements ICoreServer {
	
	private View view;
	private ServerService sDienst;
	@Override
	public boolean isServerRunning() {
		//TODO: implement this method
		return true;
	}

	
	@Override
	public void startGui() {
		view = new View(this);
		view.showFrame();
	}

	
	@Override
	public void startServer() {
		sDienst = new ServerService();
		sDienst.startServer();
		
	}

	
	@Override
	public void stopServer() {
		sDienst.stopServer();
	}

}