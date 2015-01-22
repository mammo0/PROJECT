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
	
	/**
	 * Get a node by its name
	 * @param fxmlName the name of the node
	 * @return the node
	 */
	public Node getNode(String fxmlName);
	
	/**
	 * Tests if the node is empty
	 * @return true if the node is empty
	 */
	public boolean isEmpty();
	
	/**
	 * Disable or enabling  the writing for a project
	 * @param disable
	 */
	public void disableWrite(boolean disable);
}
