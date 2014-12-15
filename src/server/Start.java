package server;

import server.core.Core;
import server.core.ICoreServer;

/**
 * This is the starter class for the server
 * @author Ammon
 *
 */
public class Start {
	// create a Core object and start the GUI
	public static void main(String[] args) {
		ICoreServer core = new Core();
		core.startGui();
	}
}
