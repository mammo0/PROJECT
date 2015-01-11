package client.view;

import javafx.scene.Node;


/**
 * Interface for the View class
 * @author Ammon
 *
 */
public interface IViewClient extends IComponents {
	/**
	 * This method shows the JFrame on the display
	 */
	public void showFrame();
	
	/**
	 * This method returns the root pane of the view
	 * @return the root pane
	 */
	public Node getViewRootPane();
}
