package client.view;

import java.util.ArrayList;

import client.view.components.SkillPane;
import client.view.controller.ViewController;

/**
 * Interface for the View class
 * @author Ammon
 *
 */
public interface IViewClient {
	/**
	 * This method shows the JFrame on the display
	 */
	public void showFrame();
	
	/**
	 * Set the view controller
	 * @param viewController
	 */
	public void setViewController(ViewController viewController);
	
	/**
	 * Get the skill panes from the project editor
	 * @return the skill panes
	 */
	public ArrayList<SkillPane> getSkillPanes();
}
