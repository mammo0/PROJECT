package server;

import server.core.Core;
import server.core.ICore;


/*
 * This is the starter class for the server
 */
public class start {
	public static void main(String[] args) {
		ICore core = new Core();
		core.startGui();
	}
}
