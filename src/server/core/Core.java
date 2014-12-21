package server.core;

import global.ASingelton;
import model.project.Project;
import server.view.View;

/**
 * This class is the core for the server application
 * @author Ammon
 *
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
		// TODO Auto-generated method stub
	}

	
	@Override
	public void stopServer() {
		// TODO Auto-generated method stub
	}


	@Override
	public String[] getAllProjects() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Project calculateProject(Project project) {
		// TODO Auto-generated method stub
		return null;
	}
}
