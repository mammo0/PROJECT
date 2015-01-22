package client.core;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import model.project.Project;
import model.project.Skill;

/**
 * Interface for the Core class
 * @author Ammon
 *
 */
public interface ICoreClient {
	/**
	 * This method starts the client GUI
	 */
	public void startGui();
	
	/**
	 * This method gets called when the GUI is initialized
	 */
	public void guiInitialized();
	
	/**
	 * This method saves and applies the server settings
	 * @param serverAdress the server address
	 * @param serverPort the server port
	 */
	public void saveSettings(String serverAdress, int serverPort);
	
	/**
	 * Get an array list with the pd results
	 * @param withRisk if the risk factor should be considered
	 * @return the pd results
	 */
	public ObservableList<pdTableModel> getPDTable(boolean withRisk);
	
	/**
	 * Get an array list with the cost results
	 * @return the cost results
	 */
	public ObservableList<costTableModel> getCostTable();
	
	/**
	 * Get an array list with the quarter results
	 * @return the quarter results
	 */
	public ObservableList<quarterTableModel> getQuarterTable();
	
	/**
	 * Get the skills from the skill view
	 * @return the skills
	 */
	public ArrayList<Skill> getSkills();
	
	/**
	 * Get the server address
	 * @return the server address
	 */
	public String getServerAddress();
	
	/**
	 * Get the server port
	 * @return the server port
	 */
	public int getServerPort();
	
	/**
	 * Get the project object
	 * @return the project object
	 */
	public Project getProject();
	
	/**
	 * Calculate the project object
	 */
	public boolean calculateProject();
	
	/**
	 * Save all made entries
	 */
	public void saveProject();
	
	/**
	 * Load a project
	 * @param projectName
	 */
	public void loadProject(String projectName);
	
	/**
	 * Delete a project
	 * @param projectName
	 */
	public void deleteProject(String projectName);
	
	/**
	 * Finish the current built project
	 */
	public void finishProject();
	
	/**
	 * Get if the project is finished
	 * @return true if the project is finished
	 */
	public boolean isProjectFinished();
}
