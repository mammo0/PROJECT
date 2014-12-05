package server.core;

import server.view.View;

public class Core implements ICore {
	
	private View view;
	

	@Override
	public boolean isServerRunning() {
		return true;
	}

	@Override
	public void startGui() {
		view = new View(this);
	}
}
