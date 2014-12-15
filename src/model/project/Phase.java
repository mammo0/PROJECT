package model.project;

import java.util.Date;
import java.util.Hashtable;

/**
 * This class represents a phase of the project
 * @author Ammon
 *
 */
public class Phase {
	// the name of the phase
	private String phaseName;
	
	// set a parent phase if this phase is a sub phase (default is null)
	private Phase parent = null;
	
	// the start date of the phase
	private Date startDate;
	
	// the end date of the phase
	private Date endDate;
	
	// the risk factor for the phase
	private int riskFactor;
	
	// the required skills of the phase with the required amount
	private Hashtable<Skill, Integer> skills;

	
	
	/**
	 * Get the name of the phase
	 * @return the phaseName
	 */
	public String getPhaseName() {
		return phaseName;
	}

	/**
	 * Get the parent of a sub phase (null if it is a main phase)
	 * @return the parent
	 */
	public Phase getParent() {
		return parent;
	}

	/**
	 * Get the starting date of the phase
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Get the ending date of the phase
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Get the risk factor of the phase
	 * @return the riskFactor
	 */
	public int getRiskFactor() {
		return riskFactor;
	}

	/**
	 * Get the skills required by this phase with their number
	 * @return the skills
	 */
	public Hashtable<Skill, Integer> getSkills() {
		return skills;
	}

	/**
	 * Set the name of the phase
	 * @param phaseName the phaseName to set
	 */
	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	/**
	 * Set the parent of the sub phase
	 * @param parent the parent to set
	 */
	public void setParent(Phase parent) {
		this.parent = parent;
	}

	/**
	 * Set the starting date of the phase
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Set the ending date of the phase
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Set the risk factor of the phase
	 * @param riskFactor the riskFactor to set
	 */
	public void setRiskFactor(int riskFactor) {
		this.riskFactor = riskFactor;
	}

	/**
	 * Add a new skill to the phase with its number
	 * @param amount the amount of that skill
	 * @param skill the skill
	 */
	public void addSkill(int amount, Skill skill) {
		this.skills.put(skill, amount);
	}
}
