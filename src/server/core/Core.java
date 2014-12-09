package server.core;

import server.ASingelton;
import server.view.View;

/*
 * This class is the core for the server application
 */
public class Core extends ASingelton implements ICoreServer {
	
	private View view;
	
	
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
		// TODO implement this method
	}

	
	@Override
	public void stopServer() {
		// TODO implement this method
	}
}
