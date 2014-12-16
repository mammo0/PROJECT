package client.core;

import global.ASingelton;
import client.view.IViewClient;
import client.view.View;

/**
 * This class is the core for the client application
 * @author Ammon
 *
 */
public class Core extends ASingelton implements ICoreClient {
	
	private IViewClient view;

	
	@Override
	public void startGui() {
		view = new View(this);
		view.showFrame();
	}
}
