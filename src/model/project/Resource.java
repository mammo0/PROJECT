package model.project;

import java.io.Serializable;

/**
 * This class represents a resource of the project
 * 
 * @author Ammon
 *
 */
public class Resource implements Serializable {
	// the name of the resource
	private String resourceName;

	// the ID of the resource calculated by the hash value of the resource name
	private int resourceID;

	// the skill ID of the skill belonging to the resource
	private int skillID;

	// the availability of the resource in percent over the whole project
	// duration
	private int availability;

	// the amount of an available skill
	private int skillAmount;

	// flag if the resource is internal or external
	private boolean intern;

	/**
	 * Get the name of the resource
	 * 
	 * @return the resourceName
	 */
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * Get the ID of the resource. It is based on the hash value of the resource
	 * name. Returns -1 if no name is given.
	 * 
	 * @return the resourceID
	 */
	public int getResourceID() {
		if (resourceName == null)
			return -1;
		else {
			resourceID = resourceName.hashCode();
			return resourceID;
		}
	}

	/**
	 * Get the skill ID of the skill which belongs to the resource
	 * 
	 * @return the skill
	 */
	public int getSkillID() {
		return skillID;
	}

	/**
	 * Get the availability of the resource
	 * 
	 * @return the availability
	 */
	public int getAvailability() {
		return availability;
	}

	/**
	 * Get the available amount of a skill
	 * 
	 * @return the skillAmount
	 */
	public int getSkillAmount() {
		return skillAmount;
	}

	/**
	 * Returns if the resource is internal or not
	 * 
	 * @return the intern
	 */
	public boolean isIntern() {
		return intern;
	}

	/**
	 * Set the resource name
	 * 
	 * @param resourceName
	 *            the resourceName to set
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * Set the skill ID of the skill belonging to the resource
	 * 
	 * @param skill
	 *            the skill to set
	 */
	public void setSkill(int skillID) {
		this.skillID = skillID;
	}

	/**
	 * Set the availability of the resource
	 * 
	 * @param availability
	 *            the availability to set
	 */
	public void setAvailability(int availability) {
		this.availability = availability;
	}

	/**
	 * Set the available amount of a skill
	 * 
	 * @param skillAmount
	 *            the skillAmount to set
	 */
	public void setSkillAmount(int skillAmount) {
		this.skillAmount = skillAmount;
	}

	/**
	 * Set if the resource is internal or not
	 * 
	 * @param intern
	 *            the intern to set
	 */
	public void setIntern(boolean intern) {
		this.intern = intern;
	}
}
