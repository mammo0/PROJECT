package client.view;

import javafx.scene.Node;

public interface IExpandableNode {
	/**
	 * Set the parent phase pane wrapper
	 * @param parent
	 */
	public void setParentNode(Node parent);
	
	/**
	 * This method is called when the node will be removed from the expandable table
	 */
	public void removeNode();
}
