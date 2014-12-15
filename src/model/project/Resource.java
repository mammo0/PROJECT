package model.project;

/**
 * This class represents a resource of the project
 * @author Ammon
 *
 */
public class Resource {
	// the ID of the resource
	private int resourceID;
	
	// the skill belonging to the resource
	private Skill skill;
	
	// the availability of the resource in percent over the whole project duration
	private int availability;

	
	
	/**
	 * Get the ID of the resource
	 * @return the resourceID
	 */
	public int getResourceID() {
		return resourceID;
	}

	/**
	 * Get the skill which belongs to the resource
	 * @return the skill
	 */
	public Skill getSkill() {
		return skill;
	}

	/**
	 * Get the availability of the resource
	 * @return the availability
	 */
	public int getAvailability() {
		return availability;
	}

	/**
	 * Set the resource ID
	 * @param resourceID the resourceID to set
	 */
	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}

	/**
	 * Set the skill belonging to the resource
	 * @param skill the skill to set
	 */
	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	/**
	 * Set the availability of the resource
	 * @param availability the availability to set
	 */
	public void setAvailability(int availability) {
		this.availability = availability;
	}
}
