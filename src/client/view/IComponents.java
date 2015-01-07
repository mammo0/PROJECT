package client.view;

import java.util.ArrayList;
import java.util.Hashtable;

import client.view.components.PhasePaneWrapper;
import client.view.components.ResourcePaneWrapper;
import client.view.components.SkillPane;

/**
 * Interface for the components
 * @author Ammon
 *
 */
public interface IComponents {
	/**
	 * Get the skill panes from the project editor
	 * @return the skill panes
	 */
	public ArrayList<SkillPane> getSkillPanes();
	
	/**
	 * Get the resource panes from the project editor
	 * @return the resource panes
	 */
	public ArrayList<ResourcePaneWrapper> getResourcePanes();
	
	/**
	 * Get the phase panes from the project editor
	 * @return the phase panes
	 */
	public Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> getPhasePanes();
	
	/**
	 * Get the name of the project
	 * @return the project name
	 */
	public String getProjectName();
	
	/**
	 * Get the responsible person of this project
	 * @return the responsible person
	 */
	public String getProjectResponsible();
	
	/**
	 * Get the project description
	 * @return the description
	 */
	public String getProjectDescription();
}
