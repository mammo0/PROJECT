package client.core;

import java.util.ArrayList;

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
	 * Get the skills from the skill view
	 * @return the skills
	 */
	public ArrayList<Skill> getSkills();
}
