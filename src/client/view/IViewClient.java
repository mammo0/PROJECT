package client.view;

import client.view.controller.ViewController;

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
	 * Set the view controller
	 * @param viewController
	 */
	public void setViewController(ViewController viewController);
}
