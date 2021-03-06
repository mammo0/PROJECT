package client.view;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;

import client.view.components.PhasePane;
import client.view.components.PhasePaneWrapper;
import client.view.components.ResourcePane;
import client.view.components.ResourcePaneWrapper;
import client.view.components.SkillPane;

/**
 * Interface for the components
 * @author Ammon
 *
 */
public interface IComponents {
	/**
	 * Clear all inputs
	 */
	public void clearAll();
	
	/**
	 * Disable or enabling  the writing for a project
	 * @param disable
	 */
	public void disableWrite(boolean disable);
	
	/**
	 * Get the skill panes from the project editor
	 * @return the skill panes
	 */
	public ArrayList<SkillPane> getSkillPanes();
	
	/**
	 * Add a new skill pane
	 * @return the new skill pane
	 */
	public SkillPane addSkillPane();
	
	/**
	 * Get the resource panes from the project editor
	 * @return the resource panes
	 */
	public ArrayList<ResourcePaneWrapper> getResourcePanes();
	
	/**
	 * Add a new resource pane
	 * @return the new resource pane
	 * @param parentSkillID
	 */
	public ResourcePane addResourcePane(int parentSkillID);
	
	/**
	 * Get the phase panes from the project editor.
	 * If no phases exist, it creates an empty main phase.
	 * @return the phase panes
	 */
	public Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> getPhasePanes();
	
	/**
	 * Add a new phase pane wrapper
	 * @param phaseName
	 * @param parentName
	 * @return the new phase pane wrapper
	 */
	public PhasePaneWrapper addPhasePaneWrapper(String phaseName, String parentName);
	
	/**
	 * Add a new phase pane
	 * @param wrapper the wrapper pane
	 * @return the new phase pane
	 */
	public PhasePane addPhasePane(PhasePaneWrapper wrapper);
	
	/**
	 * Get the name of the project
	 * @return the project name
	 */
	public String getProjectName();
	
	/**
	 * Set the name of the project
	 * @param projectName
	 */
	public void setProjectName(String projectName);
	
	/**
	 * Get the responsible person of this project
	 * @return the responsible person
	 */
	public String getProjectResponsible();
	
	/**
	 * Set the responsible person of this project
	 * @param projectResponsible
	 */
	public void setProjectResponsible(String projectResponsible);
	
	/**
	 * Get the project description
	 * @return the description
	 */
	public String getProjectDescription();
	
	/**
	 * Set the project description
	 * @param projectDescription
	 */
	public void setProjectDescription(String projectDescription);
	
	/**
	 * Set the finished time stamp
	 * @param timeStamp
	 */
	public void setProjectTimeStamp(LocalDateTime timeStamp);
	
	/**
	 * Get the real times from the result screen after the project is finished
	 * @return the real times per skill
	 */
	public Hashtable<String, Integer> getRealTimes();
}
