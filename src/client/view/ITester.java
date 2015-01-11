package client.view;

import javafx.scene.Node;

public interface ITester {
	/**
	 * This method checks the input of a node
	 * @param node the node which should be checked
	 * @return <code>true</code> if the input is correct
	 */
	public boolean checkInput(Node node);
}
