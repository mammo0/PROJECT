package client.view;

import java.util.ArrayList;

import client.view.components.SkillPane;

/**
 * Interface for the compontents
 * @author Ammon
 *
 */
public interface IComponents {
	/**
	 * Get the skill panes from the project editor
	 * @return the skill panes
	 */
	public ArrayList<SkillPane> getSkillPanes();
}
