package client.core;

import java.io.File;
import java.time.LocalDate;
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
	public ObservableList<PDTableModel> getPDTable(boolean withRisk);
	
	/**
	 * Get an array list with the overflow results
	 * @return the overflow results
	 */
	public ObservableList<OverflowTableModel> getOverflowTable();
	
	/**
	 * Get an array list with the cost results
	 * @return the cost results
	 */
	public ObservableList<CostTableModel> getCostTable();
	
	/**
	 * Get an array list with the quarter results
	 * @param yearBegin the start year
	 * @param quarterBegin the start quarter
	 * @param yearEnd the end year
	 * @param quarterEnd the end quarter
	 * @return the quarter results
	 */
	public ObservableList<QuarterTableModel> getQuarterTable(int yearBegin, int quarterBegin, int yearEnd, int quarterEnd);
	
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
	 * @return true if it was successful
	 */
	public boolean finishProject();
	
	/**
	 * Get if the project is finished
	 * @return true if the project is finished
	 */
	public boolean isProjectFinished();
	
	/**
	 * Get the project start date
	 * @return the start date
	 */
	public LocalDate getProjectStartDate();
	
	/**
	 * Get the project end date
	 * @return the end date
	 */
	public LocalDate getProjectEndDate();
	
	/**
	 * Get the actual loaded project name
	 * @return the project name
	 */
	public String getLoadedProjectName();
	
	/**
	 * Clear the project object
	 */
	public void clearProject();
	
	/**
	 * Export the project to a csv file
	 * @param saveFile the file to save
	 */
	public void exportCSV(File saveFile);
}
