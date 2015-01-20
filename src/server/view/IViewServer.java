package server.view;

import javafx.stage.Stage;

/**
 * Interface for the View class
 * @author Ammon
 *
 */
public interface IViewServer {
	/**
	 * This method shows the JFrame on the display
	 */
	public void showFrame();
	
	/**
	 * Get the primary stage of the view
	 * @return the primaryStage
	 */
	public Stage getPrimaryStage();
}
