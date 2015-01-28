package client.view;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.stage.Stage;


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
	
	/**
	 * Get the primary stage of the view
	 * @return the primary stage
	 */
	public Stage getViewPrimaryStage();
	
	/**
	 * This method displays a status text for a given period of time on the view
	 * @param status the status message
	 * @param displayTime the time how long the message is displayed
	 */
	public void setStatus(String status, int displayTime);
	
	/**
	 * This method marks a node on the view to set an input
	 * @param fxmlName the name of the node
	 */
	public void markNode(Node parent, String fxmlName);
	
	/**
	 * Display the results in the result tab
	 */
	public void displayResults();
	
	/**
	 * Display the project in the right list view
	 * @param projectNames
	 */
	public void displayProjects(ArrayList<String> projectNames);
}
