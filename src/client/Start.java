package client;

import client.core.ICoreClient;
import client.core.Core;

/**
 * This is the starter class for the client
 * @author Ammon
 *
 */
public class Start {
	// create a Core object and start the GUI
	public static void main(String[] args) {
		ICoreClient core = new Core();
		core.startGui();
	}
}
